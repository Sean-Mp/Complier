import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator implements TypeVisitor {
    private final symboltable.SymbolTable symbolTable;
    private final StringBuilder out = new StringBuilder();
    private int labelCounter = 0;
    private int tempCounter = 0;
    
    private Map<String, ProcedureNode> procedures = new HashMap<>();
    private Map<String, FunctionNode> functions = new HashMap<>();

    public CodeGenerator(symboltable.SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    private void emit(String line) {
        out.append(line).append("\n");
    }

    private void emitComment(String comment) {
        emit("REM " + comment);
    }

    private String newLabel() {
        return "L" + (labelCounter++);
    }

    private String newTemp() {
        return "t" + (tempCounter++);
    }

    public String getGeneratedCode() {
        return out.toString();
    }

    public void saveToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(getGeneratedCode());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write intermediate code to '" + filePath + "'", e);
        }
    }

    @Override
    public void visit(ProgramNode node) {
        emitComment("=== SPL INTERMEDIATE CODE ===");
        emitComment("Global variables: " + node.getGlobalVars());
        emit("");

        for (ProcedureNode p : node.getProcedures()) {
            procedures.put(p.getName(), p);
            emitComment("Stored procedure: " + p.getName());
        }
        for (FunctionNode f : node.getFunctions()) {
            functions.put(f.getName(), f);
            emitComment("Stored function: " + f.getName());
        }
        emit("");

        emitComment("=== MAIN PROGRAM ===");
        if (node.getMainProgram() != null) {
            node.getMainProgram().accept(this);
        }
    }

    @Override
    public void visit(ProcedureNode node) {
        emitComment("PROC " + node.getName() + " params=" + node.getParameters() + " locals=" + node.getLocalVars());
    }

    @Override
    public void visit(FunctionNode node) {
        emitComment("FUNC " + node.getName() + " params=" + node.getParameters() + " locals=" + node.getLocalVars());
    }

    @Override
    public void visit(MainProgramNode node) {
        emitComment("Main variables: " + node.getVariables());
        emit("");

        List<ASTNode> algo = node.getAlgorithm();
        if (algo != null) {
            for (ASTNode stmt : algo) {
                stmt.accept(this);
            }
        }
    }

    @Override 
    public void visit(AtomNode node) {
    }

    @Override 
    public void visit(UnopNode node) {
        if (node.getOperand() != null) {
            node.getOperand().accept(this);
        }
    }

    @Override 
    public void visit(BinopNode node) {
        if (node.getLeft() != null) node.getLeft().accept(this);
        if (node.getRight() != null) node.getRight().accept(this);
    }

    @Override
    public void visit(AssignmentNode node) {
        String target = node.getVariable();
        String exprTemp = evaluateExpression(node.getExpression());
        emit(target + " = " + exprTemp);
    }

    @Override
    public void visit(LoopNode node) {
        String loopType = node.getLoopType();
        
        if ("while".equalsIgnoreCase(loopType)) {
            String beginLabel = newLabel();
            String bodyLabel = newLabel();
            String endLabel = newLabel();

            emitComment("while loop start");
            emit("REM " + beginLabel);
            translateBoolean(node.getCondition(), bodyLabel, endLabel);
            emit("REM " + bodyLabel);
            for (ASTNode s : node.getBody()) {
                s.accept(this);
            }
            emit("GOTO " + beginLabel);
            emit("REM " + endLabel);
            emitComment("while loop end");
            
        } else if ("do-until".equalsIgnoreCase(loopType) || "do".equalsIgnoreCase(loopType)) {
            String beginLabel = newLabel();
            String endLabel = newLabel();

            emitComment("do-until loop start");
            emit("REM " + beginLabel);
            for (ASTNode s : node.getBody()) {
                s.accept(this);
            }
            translateBoolean(node.getCondition(), endLabel, beginLabel);
            emit("REM " + endLabel);
            emitComment("do-until loop end");
        }
    }

    @Override
    public void visit(BranchNode node) {
        String thenLabel = newLabel();
        String elseLabel = newLabel();
        String endLabel = newLabel();

        List<ASTNode> elseBody = node.getElseBody();
        
        translateBoolean(node.getCondition(), thenLabel, elseLabel);
        
        emit("REM " + elseLabel);
        if (elseBody != null && !elseBody.isEmpty()) {
            for (ASTNode s : elseBody) {
                s.accept(this);
            }
        }
        emit("GOTO " + endLabel);
        
        emit("REM " + thenLabel);
        for (ASTNode s : node.getIfBody()) {
            s.accept(this);
        }
        
        emit("REM " + endLabel);
    }

    @Override
    public void visit(PrintNode node) {
        if (node.isString()) {
            emit("PRINT \"" + node.getOutput() + "\"");
        } else {
            String t = evaluateExpression((ASTNode) node.getOutput());
            emit("PRINT " + t);
        }
    }

    @Override
    public void visit(ProcedureCallNode node) {
        emitComment("Inlining procedure: " + node.getProcedureName());
        inlineProcedure(node.getProcedureName(), node.getArguments());
    }

    @Override
    public void visit(FunctionCallNode node) {
        emitComment("Inlining function: " + node.getFunctionName());
    }

    @Override
    public void visit(HaltNode node) {
        emit("STOP");
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getReturnValue() != null) {
            String retVal = evaluateExpression(node.getReturnValue());
            emit("RETURN " + retVal);
        } else {
            emit("RETURN");
        }
    }

    @Override
    public void visit(BlockNode node) {
        for (ASTNode s : node.getStatements()) {
            s.accept(this);
        }
    }

    // === INLINING IMPLEMENTATION ===
    
    private void inlineProcedure(String procName, List<ASTNode> arguments) {
        ProcedureNode proc = procedures.get(procName);
        if (proc == null) {
            emitComment("ERROR: Procedure " + procName + " not found");
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        List<String> params = proc.getParameters();
        
        // Map parameters to argument values
        for (int i = 0; i < params.size() && i < arguments.size(); i++) {
            String argValue = evaluateExpression(arguments.get(i));
            paramMap.put(params.get(i), argValue);
        }

        // Rename local variables to avoid conflicts
        Map<String, String> localMap = new HashMap<>();
        for (String local : proc.getLocalVars()) {
            String renamed = "v" + (tempCounter++);
            localMap.put(local, renamed);
            emit(renamed + " = 0");
        }

        // Inline the procedure body with substitutions
        for (ASTNode stmt : proc.getAlgorithm()) {
            inlineStatement(stmt, paramMap, localMap);
        }
    }

    private String inlineFunction(String funcName, List<ASTNode> arguments) {
        FunctionNode func = functions.get(funcName);
        if (func == null) {
            emitComment("ERROR: Function " + funcName + " not found");
            return "0";
        }

        Map<String, String> paramMap = new HashMap<>();
        List<String> params = func.getParameters();
        
        // Map parameters to argument values
        for (int i = 0; i < params.size() && i < arguments.size(); i++) {
            String argValue = evaluateExpression(arguments.get(i));
            paramMap.put(params.get(i), argValue);
        }

        // Rename local variables
        Map<String, String> localMap = new HashMap<>();
        for (String local : func.getLocalVars()) {
            String renamed = "v" + (tempCounter++);
            localMap.put(local, renamed);
            emit(renamed + " = 0");
        }

        // Result variable for return value
        String resultVar = "r" + (tempCounter++);
        emit(resultVar + " = 0");

        // Inline function body
        for (ASTNode stmt : func.getAlgorithm()) {
            if (stmt instanceof ReturnNode) {
                String retVal = evaluateExpressionWithSubst(
                    ((ReturnNode) stmt).getReturnValue(), 
                    paramMap, 
                    localMap
                );
                emit(resultVar + " = " + retVal);
            } else {
                inlineStatement(stmt, paramMap, localMap);
            }
        }

        return resultVar;
    }

    private void inlineStatement(ASTNode stmt, Map<String, String> paramMap, Map<String, String> localMap) {
        if (stmt instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode) stmt;
            String target = substituteVar(assign.getVariable(), paramMap, localMap);
            String value = evaluateExpressionWithSubst(assign.getExpression(), paramMap, localMap);
            emit(target + " = " + value);
        }
        else if (stmt instanceof PrintNode) {
            PrintNode print = (PrintNode) stmt;
            if (print.isString()) {
                emit("PRINT \"" + print.getOutput() + "\"");
            } else {
                String value = evaluateExpressionWithSubst((ASTNode) print.getOutput(), paramMap, localMap);
                emit("PRINT " + value);
            }
        }
        else if (stmt instanceof HaltNode) {
            // Skip HALT when inlining - functions/procedures should return naturally
            return;
        }
        else if (stmt instanceof LoopNode) {
            LoopNode loop = (LoopNode) stmt;
            if ("while".equalsIgnoreCase(loop.getLoopType())) {
                String beginLabel = newLabel();
                String bodyLabel = newLabel();
                String endLabel = newLabel();
                
                emit("REM " + beginLabel);
                translateBooleanWithSubst(loop.getCondition(), bodyLabel, endLabel, paramMap, localMap);
                emit("REM " + bodyLabel);
                for (ASTNode s : loop.getBody()) {
                    inlineStatement(s, paramMap, localMap);
                }
                emit("GOTO " + beginLabel);
                emit("REM " + endLabel);
            }
        }
        else if (stmt instanceof BranchNode) {
            BranchNode branch = (BranchNode) stmt;
            String thenLabel = newLabel();
            String elseLabel = newLabel();
            String endLabel = newLabel();
            
            translateBooleanWithSubst(branch.getCondition(), thenLabel, elseLabel, paramMap, localMap);
            
            emit("REM " + elseLabel);
            if (branch.getElseBody() != null) {
                for (ASTNode s : branch.getElseBody()) {
                    inlineStatement(s, paramMap, localMap);
                }
            }
            emit("GOTO " + endLabel);
            
            emit("REM " + thenLabel);
            for (ASTNode s : branch.getIfBody()) {
                inlineStatement(s, paramMap, localMap);
            }
            emit("REM " + endLabel);
        }
    }

    private String substituteVar(String var, Map<String, String> paramMap, Map<String, String> localMap) {
        if (paramMap.containsKey(var)) return paramMap.get(var);
        if (localMap.containsKey(var)) return localMap.get(var);
        return var; // Global or main variable
    }

    private String evaluateExpressionWithSubst(ASTNode expr, Map<String, String> paramMap, Map<String, String> localMap) {
        if (expr == null) return "0";
        
        if (expr instanceof AtomNode) {
            Object v = ((AtomNode) expr).getValue();
            if (v instanceof Integer) {
                return String.valueOf(v);
            } else {
                return substituteVar(String.valueOf(v), paramMap, localMap);
            }
        }
        
        if (expr instanceof UnopNode) {
            UnopNode u = (UnopNode) expr;
            String operand = evaluateExpressionWithSubst(u.getOperand(), paramMap, localMap);
            String op = u.getOperator();
            
            if ("neg".equalsIgnoreCase(op)) {
                String temp = newTemp();
                emit(temp + " = -" + operand);
                return temp;
            }
            return operand;
        }
        
        if (expr instanceof BinopNode) {
            BinopNode b = (BinopNode) expr;
            String left = evaluateExpressionWithSubst(b.getLeft(), paramMap, localMap);
            String right = evaluateExpressionWithSubst(b.getRight(), paramMap, localMap);
            String op = mapBinOp(b.getOperator());
            String temp = newTemp();
            emit(temp + " = " + left + " " + op + " " + right);
            return temp;
        }
        
        if (expr instanceof FunctionCallNode) {
            FunctionCallNode fc = (FunctionCallNode) expr;
            List<ASTNode> args = fc.getArguments();
            return inlineFunction(fc.getFunctionName(), args);
        }
        
        return "0";
    }

    private void translateBooleanWithSubst(ASTNode cond, String trueLabel, String falseLabel, 
                                          Map<String, String> paramMap, Map<String, String> localMap) {
        if (cond == null) {
            emit("GOTO " + falseLabel);
            return;
        }
        
        if (cond instanceof UnopNode) {
            UnopNode u = (UnopNode) cond;
            if ("not".equalsIgnoreCase(u.getOperator())) {
                translateBooleanWithSubst(u.getOperand(), falseLabel, trueLabel, paramMap, localMap);
                return;
            }
        }
        
        if (cond instanceof BinopNode) {
            BinopNode b = (BinopNode) cond;
            String op = b.getOperator();
            
            if (isComparison(op)) {
                String left = evaluateExpressionWithSubst(b.getLeft(), paramMap, localMap);
                String right = evaluateExpressionWithSubst(b.getRight(), paramMap, localMap);
                String cmpOp = mapCmpOp(op);
                emit("IF " + left + " " + cmpOp + " " + right + " THEN GOTO " + trueLabel);
                emit("GOTO " + falseLabel);
                return;
            }
        }
        
        String temp = evaluateExpressionWithSubst(cond, paramMap, localMap);
        emit("IF " + temp + " <> 0 THEN GOTO " + trueLabel);
        emit("GOTO " + falseLabel);
    }

    private String evaluateExpression(ASTNode expr) {
        if (expr == null) return "0";
        
        if (expr instanceof AtomNode) {
            Object v = ((AtomNode) expr).getValue();
            return String.valueOf(v);
        }
        
        if (expr instanceof UnopNode) {
            UnopNode u = (UnopNode) expr;
            String operand = evaluateExpression(u.getOperand());
            String op = u.getOperator();
            
            if ("neg".equalsIgnoreCase(op) || "-".equals(op)) {
                String temp = newTemp();
                emit(temp + " = -" + operand);
                return temp;
            }
            return operand;
        }
        
        if (expr instanceof BinopNode) {
            BinopNode b = (BinopNode) expr;
            String left = evaluateExpression(b.getLeft());
            String right = evaluateExpression(b.getRight());
            String op = mapBinOp(b.getOperator());
            String temp = newTemp();
            emit(temp + " = " + left + " " + op + " " + right);
            return temp;
        }
        
        if (expr instanceof FunctionCallNode) {
            FunctionCallNode fc = (FunctionCallNode) expr;
            return inlineFunction(fc.getFunctionName(), fc.getArguments());
        }
        
        return "0";
    }

    private void translateBoolean(ASTNode cond, String trueLabel, String falseLabel) {
        if (cond == null) {
            emit("GOTO " + falseLabel);
            return;
        }
        
        if (cond instanceof UnopNode) {
            UnopNode u = (UnopNode) cond;
            if ("not".equalsIgnoreCase(u.getOperator())) {
                translateBoolean(u.getOperand(), falseLabel, trueLabel);
                return;
            }
        }
        
        if (cond instanceof BinopNode) {
            BinopNode b = (BinopNode) cond;
            String op = b.getOperator();
            
            if ("and".equalsIgnoreCase(op)) {
                String midLabel = newLabel();
                translateBoolean(b.getLeft(), midLabel, falseLabel);
                emit("REM " + midLabel);
                translateBoolean(b.getRight(), trueLabel, falseLabel);
                return;
            }
            
            if ("or".equalsIgnoreCase(op)) {
                String midLabel = newLabel();
                translateBoolean(b.getLeft(), trueLabel, midLabel);
                emit("REM " + midLabel);
                translateBoolean(b.getRight(), trueLabel, falseLabel);
                return;
            }
            
            if (isComparison(op)) {
                String left = evaluateExpression(b.getLeft());
                String right = evaluateExpression(b.getRight());
                String cmpOp = mapCmpOp(op);
                emit("IF " + left + " " + cmpOp + " " + right + " THEN GOTO " + trueLabel);
                emit("GOTO " + falseLabel);
                return;
            }
        }
        
        String temp = evaluateExpression(cond);
        emit("IF " + temp + " <> 0 THEN GOTO " + trueLabel);
        emit("GOTO " + falseLabel);
    }

    private boolean isComparison(String op) {
        if (op == null) return false;
        switch (op.toLowerCase()) {
            case "=": case "eq":
            case "<>": case "!=": case "neq":
            case "<": case "lt":
            case "<=": case "le":
            case ">": case "gt":
            case ">=": case "ge":
                return true;
            default:
                return false;
        }
    }

    private String mapBinOp(String op) {
        if (op == null) return "+";
        switch (op.toLowerCase()) {
            case "+": case "plus": return "+";
            case "-": case "minus": return "-";
            case "*": case "mult": return "*";
            case "/": case "div": return "/";
            default: return op;
        }
    }

    private String mapCmpOp(String op) {
        if (op == null) return "=";
        switch (op.toLowerCase()) {
            case "=": case "eq": return "=";
            case "<>": case "!=": case "neq": return "<>";
            case "<": case "lt": return "<";
            case "<=": case "le": return "<=";
            case ">": case "gt": return ">";
            case ">=": case "ge": return ">=";
            default: return op;
        }
    }
}
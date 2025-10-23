import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// Code generator implementing the same visitor interface as TypeAnalyzer
public class CodeGenerator implements TypeVisitor {
    private final symboltable.SymbolTable symbolTable;
    private final StringBuilder out = new StringBuilder();
    private int labelCounter = 0;
    private int tempCounter = 0;
    
    // Store procedure/function bodies for inlining
    private Map<String, ProcedureNode> procedures = new HashMap<>();
    private Map<String, FunctionNode> functions = new HashMap<>();

    public CodeGenerator(symboltable.SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    // ===== Utilities =====
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

    // ===== Visitor implementations =====
    @Override
    public void visit(ProgramNode node) {
        emitComment("=== SPL INTERMEDIATE CODE ===");
        emitComment("Global variables: " + node.getGlobalVars());
        emit("");

        // Store procedures and functions for inlining (don't generate code yet)
        for (ProcedureNode p : node.getProcedures()) {
            procedures.put(p.getName(), p);
            emitComment("Stored procedure: " + p.getName());
        }
        for (FunctionNode f : node.getFunctions()) {
            functions.put(f.getName(), f);
            emitComment("Stored function: " + f.getName());
        }
        emit("");

        // Generate main program algorithm
        emitComment("=== MAIN PROGRAM ===");
        if (node.getMainProgram() != null) {
            node.getMainProgram().accept(this);
        }
        emit("STOP");
    }

    @Override
    public void visit(ProcedureNode node) {
        // Metadata only - body stored for inlining
        emitComment("PROC " + node.getName() + " params=" + node.getParameters() + " locals=" + node.getLocalVars());
    }

    @Override
    public void visit(FunctionNode node) {
        // Metadata only - body stored for inlining
        emitComment("FUNC " + node.getName() + " params=" + node.getParameters() + " locals=" + node.getLocalVars());
    }

    @Override
    public void visit(MainProgramNode node) {
        emitComment("Main variables: " + node.getVariables());
        emit("");

        // Translate only the algorithm
        List<ASTNode> algo = node.getAlgorithm();
        if (algo != null) {
            for (ASTNode stmt : algo) {
                stmt.accept(this);
            }
        }
    }

    @Override 
    public void visit(AtomNode node) {
        // Atoms are handled in expression contexts
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
            // WHILE loop: test before body
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
            // DO-UNTIL loop: body executes at least once, test after
            String beginLabel = newLabel();
            String endLabel = newLabel();

            emitComment("do-until loop start");
            emit("REM " + beginLabel);
            for (ASTNode s : node.getBody()) {
                s.accept(this);
            }
            // For do-until, exit when condition is TRUE (opposite of while)
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
        
        // Translate condition: if true goto then, if false goto else
        translateBoolean(node.getCondition(), thenLabel, elseLabel);
        
        // ELSE body comes first (executed when condition is false)
        emit("REM " + elseLabel);
        if (elseBody != null && !elseBody.isEmpty()) {
            for (ASTNode s : elseBody) {
                s.accept(this);
            }
        }
        emit("GOTO " + endLabel);
        
        // THEN body
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
        emitComment("Procedure call: " + node.getProcedureName());
        emit("CALL " + node.getProcedureName());
        // Note: In the final phase, CALL will be replaced by inlining
    }

    @Override
    public void visit(FunctionCallNode node) {
        emitComment("Function call: " + node.getFunctionName());
        emit("CALL " + node.getFunctionName());
        // Note: In the final phase, CALL will be replaced by inlining
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

    // ===== Helpers for expression and boolean translation =====
    private String evaluateExpression(ASTNode expr) {
        if (expr == null) {
            return "0";
        }
        
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
            if ("not".equalsIgnoreCase(op)) {
                String temp = newTemp();
                emit(temp + " = NOT " + operand);
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
            emit("CALL " + fc.getFunctionName());
            String temp = newTemp();
            emit(temp + " = RETVAL");
            return temp;
        }
        
        // Default fallback
        String temp = newTemp();
        emit(temp + " = 0");
        return temp;
    }

    private void translateBoolean(ASTNode cond, String trueLabel, String falseLabel) {
        if (cond == null) {
            emit("GOTO " + falseLabel);
            return;
        }
        
        if (cond instanceof BinopNode) {
            BinopNode b = (BinopNode) cond;
            String op = b.getOperator();
            
            // Handle AND (short-circuit evaluation)
            if ("and".equalsIgnoreCase(op)) {
                String midLabel = newLabel();
                translateBoolean(b.getLeft(), midLabel, falseLabel);
                emit("REM " + midLabel);
                translateBoolean(b.getRight(), trueLabel, falseLabel);
                return;
            }
            
            // Handle OR (short-circuit evaluation)
            if ("or".equalsIgnoreCase(op)) {
                String midLabel = newLabel();
                translateBoolean(b.getLeft(), trueLabel, midLabel);
                emit("REM " + midLabel);
                translateBoolean(b.getRight(), trueLabel, falseLabel);
                return;
            }
            
            // Handle comparison operators
            if (isComparison(op)) {
                String left = evaluateExpression(b.getLeft());
                String right = evaluateExpression(b.getRight());
                String cmpOp = mapCmpOp(op);
                emit("IF " + left + " " + cmpOp + " " + right + " THEN GOTO " + trueLabel);
                emit("GOTO " + falseLabel);
                return;
            }
        }
        
        // Handle NOT
        if (cond instanceof UnopNode) {
            UnopNode u = (UnopNode) cond;
            if ("not".equalsIgnoreCase(u.getOperator())) {
                // NOT swaps true and false labels
                translateBoolean(u.getOperand(), falseLabel, trueLabel);
                return;
            }
        }
        
        // Default: treat as boolean expression (non-zero = true)
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
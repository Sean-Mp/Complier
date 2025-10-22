import symboltable.*;
import java.util.*;

public class TypeAnalyzer implements TypeVisitor {
    private SymbolTable symbolTable;
    private boolean hasErrors = false;
    
    public TypeAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public boolean hasErrors() {
        return hasErrors;
    }
    
    private void reportError(String message, ASTNode node) {
        System.err.println("Type Error at line " + node.getLine() + ": " + message);
        hasErrors = true;
    }
    
    private String getNodeType(ASTNode node) {
        if (node instanceof AtomNode) return ((AtomNode) node).getType();
        if (node instanceof UnopNode) return ((UnopNode) node).getType();
        if (node instanceof BinopNode) return ((BinopNode) node).getType();
        if (node instanceof FunctionCallNode) return "numeric";
        return "error";
    }
    
    @Override
    public void visit(ProgramNode node) {
        // Visit procedures - verify they're type-less
        for (ProcedureNode proc : node.getProcedures()) {
            Symbol procSym = symbolTable.lookup(proc.getName());
            if (procSym == null) {
                reportError("Procedure '" + proc.getName() + "' not found in symbol table", proc);
            } else if (procSym.getType().equals("numeric")) {
                reportError("Procedure name '" + proc.getName() + "' must be type-less (cannot be a variable)", proc);
            }
            proc.accept(this);
        }
        
        // Visit functions - verify they're type-less
        for (FunctionNode func : node.getFunctions()) {
            Symbol funcSym = symbolTable.lookup(func.getName());
            if (funcSym == null) {
                reportError("Function '" + func.getName() + "' not found in symbol table", func);
            } else if (funcSym.getType().equals("numeric")) {
                reportError("Function name '" + func.getName() + "' must be type-less (cannot be a variable)", func);
            }
            func.accept(this);
        }
        
        node.getMainProgram().accept(this);
    }
    
    @Override
    public void visit(ProcedureNode node) {
        // Enter scope to access parameters and local variables
        symbolTable.enterScope();
        
        // Re-add parameters to current scope
        for (String param : node.getParameters()) {
            symbolTable.addSymbol(param, "numeric");
        }
        
        // Re-add local variables to current scope
        for (String local : node.getLocalVars()) {
            symbolTable.addSymbol(local, "numeric");
        }
        
        // Check algorithm
        for (ASTNode stmt : node.getAlgorithm()) {
            stmt.accept(this);
        }
        
        symbolTable.exitScope();
    }
    
    @Override
    public void visit(FunctionNode node) {
        // Enter scope to access parameters and local variables
        symbolTable.enterScope();
        
        // Re-add parameters to current scope
        for (String param : node.getParameters()) {
            symbolTable.addSymbol(param, "numeric");
        }
        
        // Re-add local variables to current scope
        for (String local : node.getLocalVars()) {
            symbolTable.addSymbol(local, "numeric");
        }
        
        // Check algorithm
        for (ASTNode stmt : node.getAlgorithm()) {
            stmt.accept(this);
        }
        
        // Check return value - must be numeric
        if (node.getReturnValue() != null) {
            node.getReturnValue().accept(this);
            String returnType = getNodeType(node.getReturnValue());
            if (!returnType.equals("numeric")) {
                reportError("Function '" + node.getName() + "' must return numeric value", node);
            }
        } else {
            reportError("Function '" + node.getName() + "' missing return statement", node);
        }
        
        symbolTable.exitScope();
    }
    
    @Override
    public void visit(MainProgramNode node) {
        // Enter scope to access main variables
        symbolTable.enterScope();
        
        // Re-add main variables to current scope
        for (String var : node.getVariables()) {
            symbolTable.addSymbol(var, "numeric");
        }
        
        // Check algorithm
        for (ASTNode stmt : node.getAlgorithm()) {
            stmt.accept(this);
        }
        
        symbolTable.exitScope();
    }
    
    @Override
    public void visit(AtomNode node) {
        if (node.getValue() instanceof Integer) {
            node.setType("numeric");
        } else if (node.getValue() instanceof String) {
            String varName = (String) node.getValue();
            Symbol symbol = symbolTable.lookup(varName);
            if (symbol != null && symbol.getType().equals("numeric")) {
                node.setType("numeric");
            } else {
                reportError("Variable '" + varName + "' not declared or not numeric", node);
                node.setType("error");
            }
        }
    }
    
    @Override
    public void visit(UnopNode node) {
        node.getOperand().accept(this);
        String operandType = getNodeType(node.getOperand());
        
        if (node.getOperator().equals("neg")) {
            if (operandType.equals("numeric")) {
                node.setType("numeric");
            } else {
                reportError("'neg' operator requires numeric operand", node);
                node.setType("error");
            }
        } else if (node.getOperator().equals("not")) {
            if (operandType.equals("boolean")) {
                node.setType("boolean");
            } else {
                reportError("'not' operator requires boolean operand", node);
                node.setType("error");
            }
        }
    }
    
    @Override
    public void visit(BinopNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        
        String leftType = getNodeType(node.getLeft());
        String rightType = getNodeType(node.getRight());
        
        switch (node.getOperator()) {
            case "plus": case "minus": case "mult": case "div":
                if (leftType.equals("numeric") && rightType.equals("numeric")) {
                    node.setType("numeric");
                } else {
                    reportError("Arithmetic operator '" + node.getOperator() + "' requires numeric operands", node);
                    node.setType("error");
                }
                break;
                
            case "or": case "and":
                if (leftType.equals("boolean") && rightType.equals("boolean")) {
                    node.setType("boolean");
                } else {
                    reportError("Boolean operator '" + node.getOperator() + "' requires boolean operands", node);
                    node.setType("error");
                }
                break;
                
            case "eq": case ">":
                if (leftType.equals("numeric") && rightType.equals("numeric")) {
                    node.setType("boolean");
                } else {
                    reportError("Comparison operator '" + node.getOperator() + "' requires numeric operands", node);
                    node.setType("error");
                }
                break;
        }
    }
    
    @Override
    public void visit(AssignmentNode node) {
        node.getExpression().accept(this);
        String exprType = getNodeType(node.getExpression());
        
        Symbol varSymbol = symbolTable.lookup(node.getVariable());
        if (varSymbol == null) {
            reportError("Variable '" + node.getVariable() + "' not declared", node);
        } else if (varSymbol.isFunction()) {
            reportError("Cannot assign to function/procedure name '" + node.getVariable() + "'", node);
        } else if (!exprType.equals("numeric")) {
            reportError("Assignment requires numeric expression", node);
        }
    }
    
    @Override
    public void visit(LoopNode node) {
        node.getCondition().accept(this);
        String condType = getNodeType(node.getCondition());
        
        if (!condType.equals("boolean")) {
            reportError("Loop condition must be boolean", node);
        }
        
        for (ASTNode stmt : node.getBody()) {
            stmt.accept(this);
        }
    }
    
    @Override
    public void visit(BranchNode node) {
        node.getCondition().accept(this);
        String condType = getNodeType(node.getCondition());
        
        if (!condType.equals("boolean")) {
            reportError("Branch condition must be boolean", node);
        }
        
        for (ASTNode stmt : node.getIfBody()) {
            stmt.accept(this);
        }
        
        if (node.hasElse()) {
            for (ASTNode stmt : node.getElseBody()) {
                stmt.accept(this);
            }
        }
    }
    
    @Override
    public void visit(PrintNode node) {
        if (!node.isString()) {
            ASTNode outputNode = (ASTNode) node.getOutput();
            outputNode.accept(this);
            String outputType = getNodeType(outputNode);
            if (!outputType.equals("numeric") && !outputType.equals("error")) {
                reportError("Print output must be numeric or string", node);
            }
        }
    }
    
    @Override
    public void visit(ProcedureCallNode node) {
        // Verify procedure exists and is type-less
        Symbol procSym = symbolTable.lookup(node.getProcedureName());
        if (procSym == null) {
            reportError("Procedure '" + node.getProcedureName() + "' not declared", node);
        } else if (!procSym.isFunction()) {
            reportError("'" + node.getProcedureName() + "' is not a procedure", node);
        }
        
        // Check arguments are numeric
        for (ASTNode arg : node.getArguments()) {
            arg.accept(this);
            String argType = getNodeType(arg);
            if (!argType.equals("numeric") && !argType.equals("error")) {
                reportError("Procedure argument must be numeric", node);
            }
        }
    }
    
    @Override
    public void visit(FunctionCallNode node) {
        // Verify function exists and is type-less
        Symbol funcSym = symbolTable.lookup(node.getFunctionName());
        if (funcSym == null) {
            reportError("Function '" + node.getFunctionName() + "' not declared", node);
        } else if (!funcSym.isFunction()) {
            reportError("'" + node.getFunctionName() + "' is not a function", node);
        }
        
        // Check arguments are numeric
        for (ASTNode arg : node.getArguments()) {
            arg.accept(this);
            String argType = getNodeType(arg);
            if (!argType.equals("numeric") && !argType.equals("error")) {
                reportError("Function argument must be numeric", node);
            }
        }
    }
    
    @Override
    public void visit(HaltNode node) {
        // Always valid
    }
    
    @Override
    public void visit(ReturnNode node) {
        node.getReturnValue().accept(this);
        String returnType = getNodeType(node.getReturnValue());
        if (!returnType.equals("numeric")) {
            reportError("Return value must be numeric", node);
        }
    }
    
    @Override
    public void visit(BlockNode node) {
        for (ASTNode stmt : node.getStatements()) {
            stmt.accept(this);
        }
    }
}
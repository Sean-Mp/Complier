package symboltable;

import java.util.Stack;

/**
 * SemanticAnalyzer integrates the symbol table with the parser
 * to perform semantic analysis during parsing.
 */
public class SemanticAnalyzer {
    private final SymbolTable symbolTable;
    private final Stack<String> currentFunction;
    private int labelCount;
    private int tempVarCount;

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        this.currentFunction = new Stack<>();
        this.labelCount = 0;
        this.tempVarCount = 0;
        
        // Initialize with global scope
        symbolTable.enterScope();
    }

    // ===== Scope Management =====
    
    public void enterScope() {
        symbolTable.enterScope();
    }
    
    public void exitScope() {
        // Before exiting scope, we might want to do some cleanup
        symbolTable.exitScope();
    }
    
    // ===== Symbol Management =====
    
    public Symbol declareVariable(String name, String type) {
        if (symbolTable.existsInCurrentScope(name)) {
            // Variable already declared in this scope
            return null;
        }
        return symbolTable.addSymbol(name, type);
    }
    
    public Symbol declareArray(String name, String type, int size) {
        Symbol arraySymbol = declareVariable(name, type);
        if (arraySymbol != null) {
            arraySymbol.setArray(true);
            arraySymbol.setArraySize(size);
        }
        return arraySymbol;
    }
    
    public Symbol declareFunction(String name, String returnType) {
        Symbol funcSymbol = symbolTable.addSymbol(name, "FUNCTION");
        if (funcSymbol != null) {
            funcSymbol.setFunction(true);
            funcSymbol.setReturnType(returnType);
            currentFunction.push(name);
            // Enter function scope
            symbolTable.enterScope();
        }
        return funcSymbol;
    }
    
    public void endFunction() {
        if (!currentFunction.isEmpty()) {
            symbolTable.exitScope(); // Exit function scope
            currentFunction.pop();
        }
    }
    
    public Symbol lookup(String name) {
        return symbolTable.lookup(name);
    }
    
    // ===== Type Checking =====
    
    public boolean isTypeCompatible(String type1, String type2) {
        // Add your type compatibility rules here
        return type1.equals(type2);
    }
    
    // ===== Code Generation Helpers =====
    
    public String newLabel() {
        return "L" + (labelCount++);
    }
    
    public String newTempVar() {
        return "_t" + (tempVarCount++);
    }
    
    // ===== Debugging =====
    
    public void printSymbolTable() {
        symbolTable.printSymbolTable();
    }
    
    // ===== Getters =====
    
    public String getCurrentFunction() {
        return currentFunction.isEmpty() ? null : currentFunction.peek();
    }
    
    public int getCurrentScopeLevel() {
        return symbolTable.getCurrentLevel();
    }
}

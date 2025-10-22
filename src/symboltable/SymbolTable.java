package symboltable;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private final Stack<Scope> scopeStack;
    private final List<Scope> allScopes;
    private int currentLevel;
    private int scopeCounter = 0; // Unique ID for each scope

    public SymbolTable() {
        this.scopeStack = new Stack<>();
        this.allScopes = new ArrayList<>();
        this.currentLevel = -1;
        enterScope(); // Create global scope
    }

    public void enterScope() {
        Scope parent = scopeStack.isEmpty() ? null : scopeStack.peek();
        currentLevel++;
        Scope newScope = new Scope(parent, currentLevel);
        scopeStack.push(newScope);
        allScopes.add(newScope);
        scopeCounter++;
    }

    public void exitScope() {
        if (currentLevel <= 0) {
            throw new IllegalStateException("Cannot exit global scope");
        }
        scopeStack.pop();
        currentLevel--;
    }

    public Symbol addSymbol(String name, String type) {
        Symbol symbol = new Symbol(name, type, currentLevel);
        if (scopeStack.peek().addSymbol(symbol)) {
            return symbol;
        }
        return null;
    }

    public Symbol lookup(String name) {
        return scopeStack.peek().lookup(name);
    }

    public boolean existsInCurrentScope(String name) {
        return scopeStack.peek().containsInCurrentScope(name);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Scope getCurrentScope() {
        return scopeStack.peek();
    }

    public void printSymbolTable() {
        System.out.println("\n====== SYMBOL TABLE ======");
        
        // Print global scope (level 0)
        System.out.println("\nGlobal Scope (Level 0):");
        System.out.println("Name      Type      Scope    Array    Func     Return");
        System.out.println("--------------------------------------------------");
        for (Scope scope : allScopes) {
            if (scope.getLevel() == 0) {
                for (Symbol symbol : scope.getSymbols().values()) {
                    System.out.println(symbol);
                }
            }
        }
        
        // Print each local scope separately
        int scopeNum = 1;
        for (Scope scope : allScopes) {
            if (scope.getLevel() > 0 && !scope.getSymbols().isEmpty()) {
                System.out.println("\nLocal Scope #" + scopeNum + " (Level " + scope.getLevel() + "):");
                System.out.println("Name      Type      Scope    Array    Func     Return");
                System.out.println("--------------------------------------------------");
                for (Symbol symbol : scope.getSymbols().values()) {
                    System.out.println(symbol);
                }
                scopeNum++;
            }
        }
        
        System.out.println("\n=========================\n");
    }
}
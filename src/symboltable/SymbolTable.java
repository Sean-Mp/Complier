package symboltable;

import java.util.Stack;

/**
 * SymbolTable manages scopes and symbol lookups in the compiler.
 * It maintains a stack of scopes to handle nested scoping.
 */
public class SymbolTable {
    private final Stack<Scope> scopeStack;
    private int currentLevel;

    public SymbolTable() {
        this.scopeStack = new Stack<>();
        this.currentLevel = -1;
        enterScope(); // Create global scope
    }

    /**
     * Enters a new scope
     */
    public void enterScope() {
        Scope parent = scopeStack.isEmpty() ? null : scopeStack.peek();
        currentLevel++;
        scopeStack.push(new Scope(parent, currentLevel));
    }

    /**
     * Exits the current scope
     * @throws IllegalStateException if trying to exit the global scope
     */
    public void exitScope() {
        if (currentLevel <= 0) {
            throw new IllegalStateException("Cannot exit global scope");
        }
        scopeStack.pop();
        currentLevel--;
    }

    /**
     * Adds a symbol to the current scope
     * @param name The name of the symbol
     * @param type The type of the symbol
     * @return The created Symbol, or null if a symbol with the same name exists in the current scope
     */
    public Symbol addSymbol(String name, String type) {
        Symbol symbol = new Symbol(name, type, currentLevel);
        if (scopeStack.peek().addSymbol(symbol)) {
            return symbol;
        }
        return null;
    }

    /**
     * Looks up a symbol in the current and all enclosing scopes
     * @param name The name of the symbol to find
     * @return The Symbol if found, null otherwise
     */
    public Symbol lookup(String name) {
        return scopeStack.peek().lookup(name);
    }

    /**
     * Checks if a symbol exists in the current scope only
     * @param name The name of the symbol to check
     * @return true if the symbol exists in the current scope
     */
    public boolean existsInCurrentScope(String name) {
        return scopeStack.peek().containsInCurrentScope(name);
    }

    /**
     * Gets the current scope level
     * @return The current scope level (0 for global)
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets the current scope
     * @return The current Scope object
     */
    public Scope getCurrentScope() {
        return scopeStack.peek();
    }

    /**
     * Prints the entire symbol table for debugging
     */
    public void printSymbolTable() {
        System.out.println("\n====== SYMBOL TABLE ======");
        for (int i = 0; i <= currentLevel; i++) {
            System.out.println("\nScope Level " + i + ":");
            System.out.println("Name      Type      Scope    Array    Func     Return");
            System.out.println("--------------------------------------------------");
            
            for (Scope scope : scopeStack) {
                if (scope.getLevel() == i) {
                    for (Symbol symbol : scope.getSymbols().values()) {
                        System.out.println(symbol);
                    }
                    break;
                }
            }
        }
        System.out.println("\n=========================\n");
    }
}

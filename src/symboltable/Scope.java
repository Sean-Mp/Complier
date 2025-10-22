package symboltable;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Scope parentScope;
    private final int level;
    private int nextOffset = 0;  // For calculating variable offsets within this scope

    public Scope(Scope parent, int level) {
        this.parentScope = parent;
        this.level = level;
    }

    /**
     * Add a symbol to the current scope
     * @param symbol The symbol to add
     * @return true if added successfully, false if symbol already exists in this scope
     */
    public boolean addSymbol(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false; // Symbol already exists in this scope
        }
        
        // Set the offset for the symbol
        symbol.setOffset(nextOffset);
        
        // Update next offset (assuming each variable takes 4 bytes)
        nextOffset += 4;
        if (symbol.isArray()) {
            nextOffset += (symbol.getArraySize() - 1) * 4;
        }
        
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    /**
     * Look up a symbol in the current and parent scopes
     * @param name The name of the symbol to find
     * @return The Symbol if found, null otherwise
     */
    public Symbol lookup(String name) {
        // Check current scope first
        Symbol symbol = symbols.get(name);
        
        // If not found and there's a parent scope, check there
        if (symbol == null && parentScope != null) {
            return parentScope.lookup(name);
        }
        
        return symbol;
    }

    /**
     * Check if a symbol exists in the current scope only (not in parent scopes)
     */
    public boolean containsInCurrentScope(String name) {
        return symbols.containsKey(name);
    }

    public int getLevel() {
        return level;
    }

    public Scope getParentScope() {
        return parentScope;
    }

    /**
     * Get all symbols in this scope (for debugging and testing)
     */
    public Map<String, Symbol> getSymbols() {
        return new HashMap<>(symbols);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Scope Level %d:\n", level));
        sb.append("Name      Type      Scope    Array    Func     Return\n");
        sb.append("--------------------------------------------------\n");
        
        for (Symbol symbol : symbols.values()) {
            sb.append(symbol).append("\n");
        }
        
        return sb.toString();
    }
}

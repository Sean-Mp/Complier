package symboltable;

public class Symbol {
    private final String name;
    private final String type;
    private final int scopeLevel;
    private int offset;
    private boolean isArray;
    private int arraySize;  // For arrays, 0 for non-arrays
    private boolean isFunction;
    private String returnType; // For functions

    public Symbol(String name, String type, int scopeLevel) {
        this.name = name;
        this.type = type;
        this.scopeLevel = scopeLevel;
        this.isArray = false;
        this.isFunction = false;
        this.arraySize = 0;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getType() { return type; }
    public int getScopeLevel() { return scopeLevel; }
    public int getOffset() { return offset; }
    public boolean isArray() { return isArray; }
    public int getArraySize() { return arraySize; }
    public boolean isFunction() { return isFunction; }
    public String getReturnType() { return returnType; }

    public void setOffset(int offset) { this.offset = offset; }
    public void setArray(boolean isArray) { this.isArray = isArray; }
    public void setArraySize(int size) { 
        this.isArray = true;
        this.arraySize = size; 
    }
    public void setFunction(boolean isFunction) { this.isFunction = isFunction; }
    public void setReturnType(String returnType) { 
        this.isFunction = true;
        this.returnType = returnType; 
    }

    @Override
    public String toString() {
        return String.format("%-10s %-10s %-8d %-8s %-10s %-8s", 
            name, 
            type, 
            scopeLevel,
            isArray ? "Y(" + arraySize + ")" : "N",
            isFunction ? "Y" : "N",
            returnType != null ? returnType : "-");
    }
}

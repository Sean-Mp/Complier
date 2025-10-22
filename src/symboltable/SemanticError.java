package symboltable;

public class SemanticError extends RuntimeException {
    private final int line;
    private final int column;
    
    public SemanticError(String message, int line, int column) {
        super(String.format("Semantic Error at line %d, column %d: %s", line, column, message));
        this.line = line;
        this.column = column;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
}

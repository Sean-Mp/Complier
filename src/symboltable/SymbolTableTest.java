package symboltable;

public class SymbolTableTest {
    public static void main(String[] args) {
        // Create a new symbol table
        SymbolTable symbolTable = new SymbolTable();
        
        // Add some variables to the global scope
        symbolTable.addSymbol("x", "int");
        symbolTable.addSymbol("y", "float");
        symbolTable.addSymbol("arr", "int").setArray(true);
        symbolTable.lookup("arr").setArraySize(10);
        
        // Print the symbol table
        System.out.println("After adding to global scope:");
        symbolTable.printSymbolTable();
        
        // Enter a new scope (e.g., a function or block)
        symbolTable.enterScope();
        
        // Add variables to the new scope
        symbolTable.addSymbol("i", "int");
        symbolTable.addSymbol("temp", "float");
        
        // Print the symbol table
        System.out.println("After entering new scope:");
        symbolTable.printSymbolTable();
        
        // Look up a symbol
        Symbol x = symbolTable.lookup("x");
        System.out.println("Lookup 'x': " + (x != null ? "Found" : "Not found"));
        
        // Exit the current scope
        symbolTable.exitScope();
        
        // Print the symbol table again
        System.out.println("After exiting scope:");
        symbolTable.printSymbolTable();
    }
}

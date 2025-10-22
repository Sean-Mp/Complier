import java_cup.runtime.*;
import java.io.*;
import symboltable.*;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream inputStream;
            if (args.length > 0) {
                inputStream = new FileInputStream(args[0]);
                System.out.println("Parsing file: " + args[0]);
            } else {
                System.out.println("Reading from stdin...");
                inputStream = System.in;
            }
            
            // Initialize lexer and parser
            Lexer lexer = new Lexer(new InputStreamReader(inputStream));
            Parser parser = new Parser(lexer);
            
            System.out.println("Starting parse...");
            parser.parse();
            System.out.println("✓ Parsing completed successfully!");
            
            // Print the symbol table from the parser
            System.out.println("\n=== SYMBOL TABLE ===");
            SymbolTable symbolTable = parser.getSymbolTable();
            symbolTable.printSymbolTable();
            
        } catch (Exception e) {
            System.err.println("Error during parsing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
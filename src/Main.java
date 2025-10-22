import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import symboltable.*;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream inputStream;
            if (args.length > 0) {
                inputStream = new FileInputStream(args[0]);
                System.out.println("Reading file: " + args[0]);
            } else {
                System.out.println("Reading from stdin...");
                inputStream = System.in;
            }
            
            // Phase 1: Lexical Analysis
            System.out.println("\n=== LEXICAL ANALYSIS ===");
            Lexer lexer = new Lexer(new InputStreamReader(inputStream));
            
            // Pre-scan to verify lexical correctness
            List<java_cup.runtime.Symbol> tokens = new ArrayList<>();
            java_cup.runtime.Symbol token;
            while ((token = lexer.next_token()).sym != sym.EOF) {
                tokens.add(token);
            }
            System.out.println("✓ Lexical analysis complete - " + tokens.size() + " tokens recognized");
            
            // Phase 2: Syntax Analysis (Parsing)
            System.out.println("\n=== SYNTAX ANALYSIS ===");
            // Re-open input for parsing
            if (args.length > 0) {
                inputStream = new FileInputStream(args[0]);
            } else {
                System.err.println("Error: Cannot re-read from stdin. Please provide a file.");
                return;
            }
            
            Lexer parserLexer = new Lexer(new InputStreamReader(inputStream));
            Parser parser = new Parser(parserLexer);
            parser.parse();
            System.out.println("✓ Parsing completed successfully!");
            
            // Phase 3: Symbol Table
            System.out.println("\n=== SYMBOL TABLE ===");
            SymbolTable symbolTable = parser.getSymbolTable();
            symbolTable.printSymbolTable();
            
            // Phase 4: Type Checking
            System.out.println("\n=== TYPE CHECKING ===");
            ProgramNode syntaxTree = parser.getSyntaxTree();
            
            if (syntaxTree != null) {
                TypeAnalyzer typeAnalyzer = new TypeAnalyzer(symbolTable);
                syntaxTree.accept(typeAnalyzer);
                
                if (!typeAnalyzer.hasErrors()) {
                    System.out.println("✓ Type checking passed - no errors detected");
                } else {
                    System.out.println("✗ Type checking failed - errors were detected");
                }
            } else {
                System.err.println("Error: No syntax tree generated");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
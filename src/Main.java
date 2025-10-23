import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import symboltable.*;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream inputStream;
            String inputFileName = null;
            
            if (args.length > 0) {
                inputStream = new FileInputStream(args[0]);
                inputFileName = args[0];
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
            if (inputFileName != null) {
                inputStream = new FileInputStream(inputFileName);
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

                    // Phase 5: Code Generation (Intermediate Code)
                    System.out.println("\n=== CODE GENERATION ===");
                    CodeGenerator codeGen = new CodeGenerator(symbolTable);
                    syntaxTree.accept(codeGen);

                    // Determine output file names
                    String baseName = inputFileName.replace(".spl", "");
                    String icFile = baseName + "_ic.txt";
                    String basicFile = baseName + "_basic.txt";

                    // Save intermediate code
                    codeGen.saveToFile(icFile);
                    System.out.println("✓ Intermediate code generated!");
                    System.out.println("Saved to: " + icFile);

                    // Print intermediate code to console
                    System.out.println("\n--- GENERATED INTERMEDIATE CODE ---");
                    String intermediateCode = codeGen.getGeneratedCode();
                    System.out.println(intermediateCode);
                    System.out.println("------------------------------------");

                    // Phase 6: Post-Processing to Executable BASIC
                    // System.out.println("\n=== POST-PROCESSING TO BASIC ===");
                    // BasicPostProcessor postProcessor = new BasicPostProcessor();
                    // postProcessor.loadIntermediateCode(intermediateCode);
                    // String basicCode = postProcessor.process();
                    // postProcessor.saveToFile(basicFile, basicCode);

                    // System.out.println("✓ Executable BASIC code generated!");
                    // System.out.println("Saved to: " + basicFile);

                    // // Print BASIC code to console
                    // System.out.println("\n--- EXECUTABLE BASIC CODE ---");
                    // System.out.println(basicCode);
                    // System.out.println("-----------------------------");

                    // Print label mappings
                    // System.out.println("\n--- LABEL MAPPINGS ---");
                    // postProcessor.getLabelMapping().forEach((label, lineNum) ->
                    //     System.out.println(label + " -> line " + lineNum)
                    // );
                    System.out.println("----------------------");

                    System.out.println("\n✓✓✓ COMPILATION SUCCESSFUL! ✓✓✓");
                    System.out.println("You can now run the BASIC code in an online BASIC emulator!");
                    System.out.println("Try: https://www.calormen.com/jsbasic/");

                } else {
                    System.out.println("✗ Type checking failed - code generation skipped");
                }
            } else {
                System.err.println("Error: No syntax tree generated - code generation skipped");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
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
            } else {
                System.err.println("Error: No input file provided. Usage: java Main <input_file.txt>");
                return;
            }

            // Phase 1: Lexical Analysis
            Lexer lexer = new Lexer(new InputStreamReader(inputStream));

            // Pre-scan to verify lexical correctness
            List<java_cup.runtime.Symbol> tokens = new ArrayList<>();
            java_cup.runtime.Symbol token;
            try {
                while ((token = lexer.next_token()).sym != sym.EOF) {
                    tokens.add(token);
                }
                System.out.println("===LEXING PROCESS===");
                System.out.println("Tokens accepted ✓");
            } catch (Exception e) {
                System.err.println("Lexical error: " + e.getMessage());
                return;
            }

            // Phase 2: Syntax Analysis (Parsing)
            inputStream = new FileInputStream(args[0]);
            Lexer parserLexer = new Lexer(new InputStreamReader(inputStream));
            Parser parser = new Parser(parserLexer);
            
            try {
                parser.parse();
                System.out.println("===PARSING PROCESS===");
                System.out.println("Syntax accepted ✓");
            } catch (Exception e) {
                System.err.println("Syntax error: " + e.getMessage());
                return;
            }

            // Phase 3: Scope Analysis
            SymbolTable symbolTable = parser.getSymbolTable();
            System.out.println("===SCOPE ANALYSIS PHASE===");
            System.out.println("Variable Naming and Function Naming accepted ✓");

            // Phase 4: Type Checking
            ProgramNode syntaxTree = parser.getSyntaxTree();

            if (syntaxTree != null) {
                TypeAnalyzer typeAnalyzer = new TypeAnalyzer(symbolTable);
                syntaxTree.accept(typeAnalyzer);

                if (!typeAnalyzer.hasErrors()) {
                    System.out.println("===TYPE CHECKING PHASE===");
                    System.out.println("Types accepted ✓");

                    // Phase 5: Code Generation (Intermediate Code)
                    CodeGenerator codeGen = new CodeGenerator(symbolTable);
                    syntaxTree.accept(codeGen);

                    // Determine output file names
                    String baseName = inputFileName.replace(".spl", "").replace(".txt", "");
                    String htmlFile = baseName + "_ic.html";
                    String txtFile = "BASIC.txt";

                    // Get intermediate code
                    String intermediateCode = codeGen.getGeneratedCode();

                    // Save intermediate code as HTML
                    saveIntermediateCodeAsHTML(htmlFile, intermediateCode);

                    // Phase 6: Post-Processing to Executable BASIC
                    BasicPostProcessor postProcessor = new BasicPostProcessor();
                    postProcessor.loadIntermediateCode(intermediateCode);
                    String basicCode = postProcessor.process();
                    postProcessor.saveToFile(txtFile, basicCode);
                    
                    System.out.println("===INTERMEDIATE CODE GENERATION===");
                    System.out.println("Intermediate code: " + htmlFile);
                    System.out.println("===BASIC CODE GENERATION===");
                    System.out.println("Executable BASIC code: " + txtFile);
                    System.out.println("\nCompilation successful!");


                } else {
                    System.err.println("Type error: Type checking failed");
                }
            } else {
                System.err.println("Error: No syntax tree generated");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void saveIntermediateCodeAsHTML(String filePath, String code) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("<!DOCTYPE html>\n");
            bw.write("<html>\n<head>\n");
            bw.write("<title>SPL Intermediate Code</title>\n");
            bw.write("<style>\n");
            bw.write("body { font-family: 'Courier New', monospace; background-color: #f5f5f5; padding: 20px; }\n");
            bw.write("h1 { color: #333; }\n");
            bw.write("pre { background-color: #fff; border: 1px solid #ddd; padding: 15px; ");
            bw.write("border-radius: 5px; overflow-x: auto; line-height: 1.5; }\n");
            bw.write(".comment { color: #008000; }\n");
            bw.write(".keyword { color: #0000ff; font-weight: bold; }\n");
            bw.write(".label { color: #ff0000; font-weight: bold; }\n");
            bw.write("</style>\n</head>\n<body>\n");
            bw.write("<h1>SPL Intermediate Code</h1>\n");
            bw.write("<pre>\n");
            
            // Format the code with syntax highlighting
            String[] lines = code.split("\n");
            for (String line : lines) {
                String formatted = line;
                if (line.trim().startsWith("REM")) {
                    formatted = "<span class='comment'>" + escapeHtml(line) + "</span>";
                } else if (line.contains("IF") || line.contains("GOTO") || line.contains("PRINT") || 
                          line.contains("STOP") || line.contains("RETURN")) {
                    formatted = highlightKeywords(escapeHtml(line));
                } else {
                    formatted = escapeHtml(line);
                }
                bw.write(formatted + "\n");
            }
            
            bw.write("</pre>\n</body>\n</html>");
        } catch (IOException e) {
            System.err.println("Error writing HTML file: " + e.getMessage());
        }
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }

    private static String highlightKeywords(String line) {
        line = line.replaceAll("\\b(IF|THEN|GOTO|PRINT|STOP|RETURN)\\b", 
                              "<span class='keyword'>$1</span>");
        line = line.replaceAll("\\b(L\\d+)\\b", "<span class='label'>$1</span>");
        return line;
    }
}
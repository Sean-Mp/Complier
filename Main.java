import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    
    public static void main(String[] args) {
        // Create test program
        createSampleProgram("test_program.txt");
        
        System.out.println("=== LEXER TEST ===\n");
        System.out.println("Reading and tokenizing: test_program.txt\n");
        
        // Tokenize the file
        FileReader fileReader = new FileReader("test_program.txt");
        
        System.out.println("\nLexer completed successfully!");
        
        // TODO: Once parser is complete, add:
        // System.out.println("\n=== PARSER TEST ===\n");
        // SLRParser parser = new SLRParser();
        // parser.parse(fileReader.getTokenizer().getTokenList());
    }
    
    private static void createSampleProgram(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("// Sample SPL Program\n");
            writer.write("glob { x y z }\n");
            writer.write("proc {\n");
            writer.write("  printnum(n) {\n");
            writer.write("    local { }\n");
            writer.write("    print n\n");
            writer.write("  }\n");
            writer.write("}\n");
            writer.write("func {\n");
            writer.write("  add(a b) {\n");
            writer.write("    local { result }\n");
            writer.write("    result = (a plus b);\n");
            writer.write("    return result\n");
            writer.write("  }\n");
            writer.write("}\n");
            writer.write("main {\n");
            writer.write("  var { num1 num2 sum }\n");
            writer.write("  num1 = 5;\n");
            writer.write("  num2 = 10;\n");
            writer.write("  sum = add(num1 num2);\n");
            writer.write("  print \"Result\";\n");
            writer.write("  printnum(sum);\n");
            writer.write("  halt\n");
            writer.write("}\n");
            writer.close();
            System.out.println("Sample program created: " + filename + "\n");
        } catch (IOException e) {
            System.err.println("Error creating sample program: " + e.getMessage());
        }
    }
}
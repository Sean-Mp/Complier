import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;

public class FullTest {
    public static void main(String[] args) {
        // Create a simple test program
        createTestProgram("simple_test.spl");
        
        System.out.println("=== STEP 1: LEXING ===\n");
        FileReader reader = new FileReader("simple_test.spl");
        
        System.out.println("\n=== STEP 2: PARSING ===\n");
        SLRParser parser = new SLRParser();
        boolean success = parser.parse(reader.getTokenizer().getTokenList());
        
        if (success) {
            System.out.println("\n✓ Program is valid!");
        } else {
            System.out.println("\n✗ Program has errors!");
        }
    }
    
    private static void createTestProgram(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            // Simple program to test ALGO conflict
            writer.write("halt ;\n");
            writer.write("print \"test\" ;\n");
            writer.write("halt\n");
            writer.close();
            System.out.println("Test program created: " + filename + "\n");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
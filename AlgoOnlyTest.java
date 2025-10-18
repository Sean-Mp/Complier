import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;

public class AlgoOnlyTest {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  ALGO SUBSET COMPREHENSIVE TEST");
        System.out.println("===========================================\n");
        
        // Create test that only uses ALGO/INSTR (what parser supports)
        String testProgram = 
            "// Test all instruction types\n" +
            "\n" +
            "// Simple assignments\n" +
            "x = 5 ;\n" +
            "y = 10 ;\n" +
            "z = 0 ;\n" +
            "\n" +
            "// Arithmetic expressions\n" +
            "result = (x plus y) ;\n" +
            "result = (x minus y) ;\n" +
            "result = (x mult y) ;\n" +
            "result = (x div 2) ;\n" +
            "\n" +
            "// Nested expressions\n" +
            "result = ((x plus y) mult (y minus 3)) ;\n" +
            "result = (((x plus 1) mult 2) div (y minus 1)) ;\n" +
            "\n" +
            "// Unary operations\n" +
            "result = (neg x) ;\n" +
            "flag = (not z) ;\n" +
            "result = (neg (neg x)) ;\n" +
            "\n" +
            "// Comparisons\n" +
            "flag = (x > 5) ;\n" +
            "flag = (x eq y) ;\n" +
            "flag = ((x plus 1) > (y minus 1)) ;\n" +
            "\n" +
            "// Boolean operations\n" +
            "flag = ((x > 5) and (y > 5)) ;\n" +
            "flag = ((x eq 0) or (y eq 0)) ;\n" +
            "flag = ((x > 0) and ((y > 0) or (z eq 0))) ;\n" +
            "\n" +
            "// Print statements\n" +
            "print \"start\" ;\n" +
            "print x ;\n" +
            "print y ;\n" +
            "print \"values\" ;\n" +
            "print result ;\n" +
            "\n" +
            "// While loop\n" +
            "counter = 5 ;\n" +
            "while (counter > 0) {\n" +
            "  print counter ;\n" +
            "  counter = (counter minus 1) ;\n" +  // ADDED SEMICOLON
            "  print \"looping\"\n" +
            "} ;\n" +
            "\n" +
            "// Do-until loop\n" +
            "counter = 0 ;\n" +
            "do {\n" +
            "  counter = (counter plus 1) ;\n" +
            "  print counter ;\n" +
            "  print \"until\"\n" +
            "} until (counter > 3) ;\n" +
            "\n" +
            "// If without else\n" +
            "if (x > 0) {\n" +
            "  print \"positive\" ;\n" +
            "  result = x\n" +  // Last statement in block - no semicolon needed
            "} ;\n" +
            "\n" +
            "// If-else\n" +
            "if (x eq y) {\n" +
            "  print \"equal\" ;\n" +
            "  result = 0\n" +  // Last statement in block - no semicolon needed
            "} else {\n" +
            "  print \"notequal\" ;\n" +
            "  result = (x minus y)\n" +  // Last statement in block - no semicolon needed
            "} ;\n" +
            "\n" +
            "// Nested if in while\n" +
            "counter = 3 ;\n" +
            "while (counter > 0) {\n" +
            "  if (counter eq 2) {\n" +
            "    print \"two\"\n" +
            "  } else {\n" +
            "    print counter\n" +
            "  } ;\n" +
            "  counter = (counter minus 1)\n" +  // Last statement - no semicolon
            "} ;\n" +
            "\n" +
            "// Procedure call (no return)\n" +
            "initialize() ;\n" +
            "display(x y z) ;\n" +
            "\n" +
            "// Function call assignments\n" +
            "result = add(x y) ;\n" +
            "sum = compute(10 20 30) ;\n" +
            "\n" +
            "// Final sequence testing ALGO conflict\n" +
            "print \"done\" ;\n" +
            "print result ;\n" +
            "print sum ;\n" +
            "halt\n";  // Last instruction - no semicolon needed
        
        String filename = "algo_test.spl";
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(testProgram);
            writer.close();
            System.out.println("✓ Test file created: " + filename);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
        
        System.out.println("\n=== LEXICAL ANALYSIS ===");
        FileReader reader = new FileReader(filename);
        
        if (reader.getTokenizer() == null) {
            System.err.println("✗ Lexer failed");
            return;
        }
        
        System.out.println("✓ Tokens: " + reader.getTokenizer().getTokenList().size());
        
        System.out.println("\n=== SYNTAX ANALYSIS ===");
        SLRParser parser = new SLRParser();
        boolean success = parser.parse(reader.getTokenizer().getTokenList());
        
        System.out.println("\n" + "=".repeat(50));
        if (success) {
            System.out.println("✓✓✓ PARSING SUCCESS ✓✓✓");
        } else {
            System.out.println("✗✗✗ PARSING FAILED ✗✗✗");
        }
        System.out.println("=".repeat(50));
    }
}
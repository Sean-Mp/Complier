import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ComprehensiveTest {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  COMPREHENSIVE SPL COMPILER TEST");
        System.out.println("===========================================\n");
        
        // Test 1: Simple ALGO sequence
        runTest("Test 1: Simple Instruction Sequence", "test1.spl", 
            "halt ;\nprint \"done\" ;\nhalt\n");
        
        // Test 2: Assignment
        runTest("Test 2: Variable Assignment", "test2.spl",
            "x = 5 ;\nprint x ;\nhalt\n");
        
        // Test 3: Arithmetic expression
        runTest("Test 3: Arithmetic Expression", "test3.spl",
            "result = (a plus b) ;\nprint result ;\nhalt\n");
        
        // Test 4: Comparison
        runTest("Test 4: Comparison Operation", "test4.spl",
            "result = (x > 5) ;\nprint result ;\nhalt\n");
        
        // Test 5: Unary operation
        runTest("Test 5: Unary Operation", "test5.spl",
            "result = (neg x) ;\nprint result ;\nhalt\n");
        
        // Test 6: Complex expression
        runTest("Test 6: Complex Expression", "test6.spl",
            "result = ((a plus b) mult (c minus d)) ;\nprint result ;\nhalt\n");
        
        // Test 7: Boolean operations
        runTest("Test 7: Boolean Operations", "test7.spl",
            "result = (a and b) ;\nresult = (x or y) ;\nprint result ;\nhalt\n");
        
        // Test 8: Multiple statements
        runTest("Test 8: Multiple Statements", "test8.spl",
            "x = 1 ;\ny = 2 ;\nz = 3 ;\nprint x ;\nprint y ;\nprint z ;\nhalt\n");
        
        // Test 9: String output
        runTest("Test 9: String Output", "test9.spl",
            "print \"hello\" ;\nprint \"world\" ;\nhalt\n");
        
        // Test 10: Mixed output
        runTest("Test 10: Mixed Output", "test10.spl",
            "print \"Result\" ;\nprint x ;\nprint \"Done\" ;\nhalt\n");
        
        System.out.println("\n===========================================");
        System.out.println("  ALL TESTS COMPLETED");
        System.out.println("===========================================");
    }
    
    private static void runTest(String testName, String filename, String code) {
        System.out.println("\n--- " + testName + " ---");
        System.out.println("Code:");
        System.out.println(code);
        
        // Create test file
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error creating test file: " + e.getMessage());
            return;
        }
        
        // Lexing phase
        System.out.println("\n[LEXER]");
        FileReader reader = new FileReader(filename);
        List<Token> tokens = reader.getTokenizer().getTokenList();
        System.out.println("Tokens generated: " + tokens.size());
        
        // Parsing phase
        System.out.println("\n[PARSER]");
        SLRParser parser = new SLRParser();
        boolean success = parser.parse(reader.getTokenizer().getTokenList());
        
        if (success) {
            System.out.println("✓ TEST PASSED");
        } else {
            System.out.println("✗ TEST FAILED");
        }
        
        System.out.println("-------------------------------------------");
    }
}
import Lexer.*;
import Parser.*;
import java.util.*;

public class SimpleParserTest {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  SIMPLE PARSER TESTS");
        System.out.println("===========================================\n");
        
        // Test 1: Just halt
        testParse("Test 1: Single halt", 
            new String[]{"halt"}, 
            new State[]{State.KEYWORD});
        
        // Test 2: halt ; halt (conflict test)
        testParse("Test 2: Two halts with semicolon", 
            new String[]{"halt", ";", "halt"}, 
            new State[]{State.KEYWORD, State.SYMBOL, State.KEYWORD});
        
        // Test 3: Simple print
        testParse("Test 3: Print string", 
            new String[]{"print", "\"hello\""}, 
            new State[]{State.KEYWORD, State.STRING});
        
        // Test 4: Simple assignment
        testParse("Test 4: Variable assignment", 
            new String[]{"x", "=", "5"}, 
            new State[]{State.VARIABLE, State.SYMBOL, State.NUMBER});
        
        // Test 5: Expression assignment
        testParse("Test 5: Expression with parentheses", 
            new String[]{"result", "=", "(", "a", "plus", "b", ")"}, 
            new State[]{State.VARIABLE, State.SYMBOL, State.SYMBOL, 
                       State.VARIABLE, State.OPERATOR, State.VARIABLE, State.SYMBOL});
        
        System.out.println("\n===========================================");
        System.out.println("  TESTS COMPLETED");
        System.out.println("===========================================");
    }
    
    private static void testParse(String testName, String[] values, State[] states) {
        System.out.println("\n--- " + testName + " ---");
        
        // Create token list
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            tokens.add(new Token(values[i], states[i]));
        }
        
        System.out.print("Input: ");
        for (String val : values) {
            System.out.print(val + " ");
        }
        System.out.println();
        
        // Parse
        SLRParser parser = new SLRParser();
        boolean success = parser.parse(tokens);
        
        if (success) {
            System.out.println("✓ TEST PASSED");
        } else {
            System.out.println("✗ TEST FAILED");
        }
        
        System.out.println("-------------------------------------------");
    }
}
import Lexer.*;
import java.util.List;

public class LexerTestSuite {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   SPL LEXER TEST SUITE");
        System.out.println("========================================\n");
        
        testKeywords();
        testOperators();
        testSymbols();
        testNumbers();
        testVariables();
        testStrings();
        testComplexExpressions();
        testErrorCases();
        
        System.out.println("\n========================================");
        System.out.printf("  RESULTS: %d passed, %d failed\n", testsPassed, testsFailed);
        System.out.println("========================================");
    }
    
    private static void testKeywords() {
        System.out.println("Testing Keywords...");
        String[] keywords = {"glob", "proc", "func", "main", "local", "var", 
                            "halt", "print", "while", "do", "until", "if", 
                            "else", "return"};
        
        for (String keyword : keywords) {
            testToken(keyword, State.KEYWORD, "keyword: " + keyword);
        }
        System.out.println();
    }
    
    private static void testOperators() {
        System.out.println("Testing Operators...");
        String[] operators = {"eq", ">", "or", "and", "plus", "minus", 
                             "mult", "div", "neg", "not"};
        
        for (String op : operators) {
            testToken(op, State.OPERATOR, "operator: " + op);
        }
        System.out.println();
    }
    
    private static void testSymbols() {
        System.out.println("Testing Symbols...");
        String[] symbols = {"(", ")", "{", "}", ";", "="};
        
        for (String sym : symbols) {
            testToken(sym, State.SYMBOL, "symbol: " + sym);
        }
        System.out.println();
    }
    
    private static void testNumbers() {
        System.out.println("Testing Numbers...");
        testToken("0", State.NUMBER, "zero");
        testToken("5", State.NUMBER, "single digit");
        testToken("42", State.NUMBER, "two digits");
        testToken("12345", State.NUMBER, "multiple digits");
        testToken("100", State.NUMBER, "number with zeros");
        System.out.println();
    }
    
    private static void testVariables() {
        System.out.println("Testing Variables...");
        testToken("x", State.VARIABLE, "single letter");
        testToken("variable", State.VARIABLE, "multiple letters");
        testToken("x1", State.VARIABLE, "letter with digit");
        testToken("abc123", State.VARIABLE, "letters with digits");
        testToken("myvar", State.VARIABLE, "common variable name");
        System.out.println();
    }
    
    private static void testStrings() {
        System.out.println("Testing Strings...");
        testToken("\"hello\"", State.STRING, "simple string");
        testToken("\"12345\"", State.STRING, "numeric string");
        testToken("\"a1b2c3\"", State.STRING, "alphanumeric string");
        testToken("\"test\"", State.STRING, "word string");
        System.out.println();
    }
    
    private static void testComplexExpressions() {
        System.out.println("Testing Complex Expressions...");
        
        testLine("x = 5", new String[]{"x", "=", "5"}, 
                new State[]{State.VARIABLE, State.SYMBOL, State.NUMBER},
                "simple assignment");
        
        testLine("if (x > 0)", new String[]{"if", "(", "x", ">", "0", ")"}, 
                new State[]{State.KEYWORD, State.SYMBOL, State.VARIABLE, 
                           State.OPERATOR, State.NUMBER, State.SYMBOL},
                "if condition");
        
        testLine("result = (a plus b)", 
                new String[]{"result", "=", "(", "a", "plus", "b", ")"}, 
                new State[]{State.VARIABLE, State.SYMBOL, State.SYMBOL, 
                           State.VARIABLE, State.OPERATOR, State.VARIABLE, State.SYMBOL},
                "arithmetic expression");
        
        System.out.println();
    }
    
    private static void testErrorCases() {
        System.out.println("Testing Error Cases...");
        
        testInvalidToken("01", "leading zero");
        testInvalidToken("Var", "uppercase variable");
        testInvalidToken("_var", "underscore prefix");
        
        System.out.println();
    }
    
    private static void testToken(String input, State expectedState, String description) {
        try {
            Tokenizer tokenizer = new Tokenizer();
            tokenizer.processToken(input);
            List<Token> tokens = tokenizer.getTokenList();
            
            if (tokens.size() == 1 && tokens.get(0).getState() == expectedState) {
                pass(description);
            } else {
                fail(description + " - got: " + (tokens.isEmpty() ? "no token" : tokens.get(0).getState()));
            }
        } catch (TokenException e) {
            fail(description + " - " + e.getMessage());
        }
    }
    
    private static void testLine(String line, String[] expectedValues, 
                                State[] expectedStates, String description) {
        try {
            Tokenizer tokenizer = new Tokenizer();
            tokenizer.tokenizeLine(line, 1);
            List<Token> tokens = tokenizer.getTokenList();
            
            if (tokens.size() != expectedValues.length) {
                fail(description + " - expected " + expectedValues.length + 
                     " tokens, got " + tokens.size());
                return;
            }
            
            boolean allMatch = true;
            for (int i = 0; i < tokens.size(); i++) {
                if (!tokens.get(i).getValue().equals(expectedValues[i]) ||
                    tokens.get(i).getState() != expectedStates[i]) {
                    allMatch = false;
                    break;
                }
            }
            
            if (allMatch) {
                pass(description);
            } else {
                fail(description + " - token mismatch");
            }
        } catch (TokenException e) {
            fail(description + " - " + e.getMessage());
        }
    }
    
    private static void testInvalidToken(String input, String description) {
        try {
            Tokenizer tokenizer = new Tokenizer();
            tokenizer.processToken(input);
            fail(description + " - should have thrown exception");
        } catch (TokenException e) {
            pass(description + " - correctly rejected");
        }
    }
    
    private static void pass(String test) {
        System.out.println("  [PASS] " + test);
        testsPassed++;
    }
    
    private static void fail(String test) {
        System.out.println("  [FAIL] " + test);
        testsFailed++;
    }
}
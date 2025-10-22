import Lexer.*;
import Parser.*;
import java.util.*;

public class ParserTest {
    public static void main(String[] args) {
        // Test 1: Simple ALGO sequence (tests conflict resolution)
        System.out.println("=== TEST 1: Simple instruction sequence ===");
        List<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("halt", State.KEYWORD));
        tokens1.add(new Token(";", State.SYMBOL));
        tokens1.add(new Token("print", State.KEYWORD));
        tokens1.add(new Token("x", State.VARIABLE));
        tokens1.add(new Token(";", State.SYMBOL));
        tokens1.add(new Token("halt", State.KEYWORD));
        
        SLRParser parser1 = new SLRParser();
        parser1.parse(tokens1);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Test 2: Single instruction
        System.out.println("=== TEST 2: Single instruction ===");
        List<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("halt", State.KEYWORD));
        
        SLRParser parser2 = new SLRParser();
        parser2.parse(tokens2);
    }
}
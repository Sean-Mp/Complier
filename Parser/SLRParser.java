package Parser;

import Lexer.*;
import java.util.*;

public class SLRParser {
    
    private ParseTable table;
    private Stack<Integer> stateStack;
    private Stack<String> symbolStack;
    
    public SLRParser() {
        this.table = new ParseTable();
        this.stateStack = new Stack<>();
        this.symbolStack = new Stack<>();
    }
    
    public boolean parse(List<Token> tokens) {
        // Add end marker
        tokens.add(new Token("$", State.SYMBOL));
        
        // Initialize stacks
        stateStack.push(0);
        symbolStack.push("$");
        
        int tokenIndex = 0;
        int step = 1;
        
        System.out.println("\n=== PARSING TRACE ===");
        System.out.printf("%-5s | %-20s | %-20s | %-15s | %s%n", 
                         "Step", "State Stack", "Symbol Stack", "Input", "Action");
        System.out.println("------+----------------------+----------------------+-----------------+---------------");
        
        while (tokenIndex < tokens.size()) {
            int currentState = stateStack.peek();
            Token currentToken = tokens.get(tokenIndex);
            String terminal = mapTokenToTerminal(currentToken);
            
            // Get remaining input
            StringBuilder remainingInput = new StringBuilder();
            for (int i = tokenIndex; i < Math.min(tokenIndex + 5, tokens.size()); i++) {
                remainingInput.append(tokens.get(i).getValue()).append(" ");
            }
            
            // Look up action
            ParseTable.Action action = table.getAction(currentState, terminal);
            
            // Print trace
            System.out.printf("%-5d | %-20s | %-20s | %-15s | %s%n",
                            step++,
                            truncate(stateStack.toString(), 20),
                            truncate(symbolStack.toString(), 20),
                            truncate(remainingInput.toString(), 15),
                            action.toString());
            
            if (action.type == ParseTable.ActionType.SHIFT) {
                stateStack.push(action.value);
                symbolStack.push(terminal);
                tokenIndex++;
                
            } else if (action.type == ParseTable.ActionType.REDUCE) {
                reduce(action.value);
                
            } else if (action.type == ParseTable.ActionType.ACCEPT) {
                System.out.println("\n✓ Parse successful!");
                return true;
                
            } else {
                System.out.println("\n✗ Syntax error at: " + currentToken.getValue());
                System.out.println("   State: " + currentState);
                System.out.println("   Expected one of: " + getExpectedTokens(currentState));
                return false;
            }
        }
        
        return false;
    }
    
    private void reduce(int productionNum) {
        String lhs = Grammar.getLHS(productionNum);
        int popCount = Grammar.getProductionLength(productionNum);
        
        // Pop RHS symbols from stacks
        for (int i = 0; i < popCount; i++) {
            stateStack.pop();
            if (!symbolStack.isEmpty() && !symbolStack.peek().equals("$")) {
                symbolStack.pop();
            }
        }
        
        // Push LHS
        symbolStack.push(lhs);
        
        // Consult GOTO table
        int topState = stateStack.peek();
        int gotoState = table.getGoto(topState, lhs);
        
        if (gotoState == -1) {
            System.err.println("ERROR: No GOTO entry for state " + topState + ", " + lhs);
        } else {
            stateStack.push(gotoState);
        }
    }
    
    private String mapTokenToTerminal(Token token) {
        // Map lexer tokens to grammar terminals
        switch (token.getState()) {
            case KEYWORD:
                return token.getValue(); // halt, print, if, etc.
            case VARIABLE:
                return "user-defined-name";
            case NUMBER:
                return "number";
            case STRING:
                return "string";
            case OPERATOR:
                return token.getValue(); // eq, plus, etc.
            case SYMBOL:
                return token.getValue(); // ( ) { } ; =
            default:
                return token.getValue();
        }
    }
    
    private String truncate(String str, int maxLen) {
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }
    
    private String getExpectedTokens(int state) {
        // Return list of valid tokens for this state (for error messages)
        return "[implement based on ACTION table]";
    }
    
    public static void main(String[] args) {
        // Test the parser
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("halt", State.KEYWORD));
        tokens.add(new Token(";", State.SYMBOL));
        tokens.add(new Token("print", State.KEYWORD));
        tokens.add(new Token("x", State.VARIABLE));
        tokens.add(new Token(";", State.SYMBOL));
        tokens.add(new Token("halt", State.KEYWORD));
        
        SLRParser parser = new SLRParser();
        System.out.println("Testing parser with: halt ; print x ; halt");
        parser.parse(tokens);
    }
}
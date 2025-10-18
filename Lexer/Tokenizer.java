package Lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tokenizer {
    
    private List<Token> tokenList;
    private int line;
    private int column;

    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "glob", "proc", "func", "main", "local", "var", "halt", "print",
        "while", "do", "until", "if", "else", "return"
    ));

    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList(
        "eq", ">", "or", "and", "plus", "minus", "mult", "div",
        "neg", "not"
    ));

    private static final Set<String> SYMBOLS = new HashSet<>(Arrays.asList(
        "(", ")", "{", "}", ";", "="
    ));

    public Tokenizer() {
        this.tokenList = new ArrayList<>();
        this.line = 1;
        this.column = 1;
    }

    public List<Token> getTokenList(){
        return tokenList;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Process a single token and add it to the token list
     * @param token The token string to process
     * @throws TokenException if the token is invalid
     */
    public void processToken(String token) throws TokenException {
        if (token == null || token.isEmpty()) {
            return; // Skip empty tokens
        }

        // Check if it's a keyword
        if (KEYWORDS.contains(token)) {
            tokenList.add(new Token(token, State.KEYWORD));
            return;
        }

        // Check if it's an operator
        if (OPERATORS.contains(token)) {
            tokenList.add(new Token(token, State.OPERATOR));
            return;
        }

        // Check if it's a symbol
        if (SYMBOLS.contains(token)) {
            tokenList.add(new Token(token, State.SYMBOL));
            return;
        }

        // Check if it's a string (quoted text)
        if (token.startsWith("\"") && token.endsWith("\"")) {
            String content = token.substring(1, token.length() - 1);
            if (content.length() > 15) {
                throw new TokenException("String exceeds maximum length of 15 characters", token, line, column);
            }
            // Validate string contains only letters and digits
            if (!content.matches("[a-zA-Z0-9]*")) {
                throw new TokenException("String contains invalid characters", token, line, column);
            }
            tokenList.add(new Token(token, State.STRING));
            return;
        }

        // Check if it's a number: 0 | [1-9][0-9]*
        if (token.matches("0|[1-9][0-9]*")) {
            tokenList.add(new Token(token, State.NUMBER));
            return;
        }

        // Check if it's a valid user-defined name: [a-z][a-z]*[0-9]*
        if (token.matches("[a-z][a-z0-9]*")) {
            tokenList.add(new Token(token, State.VARIABLE));
            return;
        }

        // If we reach here, the token is invalid
        throw new TokenException("Invalid token", token, line, column);
    }

    /**
     * Tokenize an entire line of input
     * @param line The line to tokenize
     * @param lineNumber The line number for error reporting
     * @throws TokenException if any token is invalid
     */
    public void tokenizeLine(String line, int lineNumber) throws TokenException {
        this.line = lineNumber;
        this.column = 1;

        if (line == null || line.trim().isEmpty()) {
            return;
        }

        // Remove comments (anything after //)
        int commentIndex = line.indexOf("//");
        if (commentIndex != -1) {
            line = line.substring(0, commentIndex);
        }

        line = line.trim();
        if (line.isEmpty()) {
            return;
        }

        int i = 0;
        while (i < line.length()) {
            char c = line.charAt(i);

            // Skip whitespace
            if (Character.isWhitespace(c)) {
                column++;
                i++;
                continue;
            }

            // Handle string literals
            if (c == '"') {
                int start = i;
                i++; // Move past opening quote
                while (i < line.length() && line.charAt(i) != '"') {
                    i++;
                }
                if (i >= line.length()) {
                    throw new TokenException("Unterminated string", line.substring(start), lineNumber, column);
                }
                i++; // Move past closing quote
                String token = line.substring(start, i);
                processToken(token);
                column += token.length();
                continue;
            }

            // Handle symbols (single character)
            if (SYMBOLS.contains(String.valueOf(c))) {
                processToken(String.valueOf(c));
                column++;
                i++;
                continue;
            }

            // Handle multi-character tokens (keywords, operators, variables, numbers)
            int start = i;
            while (i < line.length() && !Character.isWhitespace(line.charAt(i)) 
                   && !SYMBOLS.contains(String.valueOf(line.charAt(i)))
                   && line.charAt(i) != '"') {
                i++;
            }
            String token = line.substring(start, i);
            processToken(token);
            column += token.length();
        }
    }

    /**
     * Print all tokens for debugging
     */
    public void printTokens() {
        System.out.println("\n=== TOKEN LIST ===");
        for (int i = 0; i < tokenList.size(); i++) {
            Token t = tokenList.get(i);
            System.out.printf("[%d] %-12s : %s%n", i, t.getState(), t.getValue());
        }
        System.out.println("==================\n");
    }
}
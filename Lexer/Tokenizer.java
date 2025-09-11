package Lexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tokenizer {
    
    private List<Token> tokenList;

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


    public List<Token> getTokenList(){
        return tokenList;
    }

    //Function will take a token, process it, if valid add it to the token list, else throw a token exception
    public void processToken(String token) throws TokenException
    {

    }
}

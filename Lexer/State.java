package Lexer;

public enum State {
    KEYWORD,
    VARIABLE,   //user-defined name
    NUMBER,
    STRING,
    SYMBOL,
    OPERATOR
}

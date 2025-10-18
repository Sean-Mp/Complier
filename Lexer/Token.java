package Lexer;

public class Token{
    private State state;
    private String value;

    public Token(String value, State state){
        this.value = value;
        this.state = state;
    }

    public String getValue()
    {
        return value;
    }

    public State getState()
    {
        return state;
    }
}
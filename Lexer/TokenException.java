package Lexer;

public class TokenException extends Exception{
    private final String token;
    private final int line;
    private final int column;

    public TokenException(String message, String token, int line, int column){
        super(message + "(token=\"" + token + "at line = " + line + ", column=" + column + ")");
        this.token = token;
        this.line = line;
        this.column = column;
    }
    public String getToken()
    {
        return token;
    }
    public int getLine()
    {
        return line;
    }
    public int getColumn()
    {
        return column;
    }
}

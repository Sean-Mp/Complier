import java.io.*;
import java_cup.runtime.*;

public class DebugLexer {
    public static void main(String[] args) throws Exception {
        String input = "glob { var globalcounter } proc { greet (name) { local { message } message = \"Hello\" print message print name } } func { multiply (x y) { local { result } result = ( x mult y ) return result } } main { var { a } a = 5 greet (\"World\") halt }";
        
        Lexer lexer = new Lexer(new StringReader(input));
        
        Symbol token;
        int tokenCount = 0;
        do {
            token = lexer.next_token();
            System.out.println("Token " + tokenCount + ": sym=" + token.sym + ", value='" + token.value + "'");
            tokenCount++;
        } while (token.sym != sym.EOF && tokenCount < 50);
    }
}

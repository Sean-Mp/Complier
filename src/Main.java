import java_cup.runtime.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream inputStream;
            if (args.length > 0) {
                inputStream = new FileInputStream(args[0]);
                System.out.println("Parsing file: " + args[0]);
            } else {
                System.out.println("Reading from stdin...");
                inputStream = System.in;
            }
            
            Lexer lexer = new Lexer(new InputStreamReader(inputStream));
            Parser parser = new Parser(lexer);
            
            System.out.println("Starting parse...");
            parser.parse();
            System.out.println("✓ Parsing completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during parsing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
package Lexer;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {
    private Tokenizer tokenizer;

    public FileReader(String fileName) {
        try {
            tokenizer = new Tokenizer();
            BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                try {
                    tokenizer.tokenizeLine(line, lineNumber);
                } catch (TokenException e) {
                    System.err.println("Error on line " + lineNumber + ": " + e.getMessage());
                    reader.close();
                    throw e;
                }
                lineNumber++;
            }
            reader.close();
            
            tokenizer.printTokens();
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (TokenException e) {
            System.err.println("Tokenization failed: " + e.getMessage());
        }
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }
}
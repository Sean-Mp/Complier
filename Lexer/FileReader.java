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
                    System.err.println("Error at line " + lineNumber + ": " + e.getMessage());
                }
                lineNumber++;
            }
            reader.close();
            
            System.out.println("Successfully tokenized " + tokenizer.getTokenList().size() + " tokens");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    public Tokenizer getTokenizer() {
        return tokenizer;
    }
}
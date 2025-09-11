package Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileReader{
    public FileReader(String fileName)
    {
        try{
            Tokenizer tokenizer = new Tokenizer();
            BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
            String line;

            while((line = reader.readLine()) != null)
            {
                //TODO: tokenize line 
            }
            reader.close();
        }
        catch(IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }
        // catch(TokenException e)
        // {
        //     System.out.println("Token error: " + e.getMessage());
        // }
    }
}
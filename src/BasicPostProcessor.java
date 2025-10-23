import java.io.*;
import java.util.*;

/**
 * Post-processor that converts intermediate code to executable BASIC
 * - Adds line numbers (10, 20, 30, ...)
 * - Resolves label references (REM Lx -> line numbers)
 * - Converts GOTO Lx and THEN Lx to use line numbers
 */
public class BasicPostProcessor {
    private List<String> intermediateCode;
    private Map<String, Integer> labelToLineNumber;
    private int lineNumberIncrement = 10;
    private int currentLineNumber = 10;

    public BasicPostProcessor() {
        this.intermediateCode = new ArrayList<>();
        this.labelToLineNumber = new HashMap<>();
    }

    /**
     * Read intermediate code from file
     */
    public void readIntermediateCode(String filePath) throws IOException {
        intermediateCode.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                intermediateCode.add(line);
            }
        }
    }

    /**
     * Load intermediate code from string
     */
    public void loadIntermediateCode(String code) {
        intermediateCode.clear();
        String[] lines = code.split("\n");
        for (String line : lines) {
            intermediateCode.add(line);
        }
    }

    /**
     * Process intermediate code to executable BASIC
     */
    public String process() {
        if (intermediateCode.isEmpty()) {
            return "";
        }

        // First pass: assign line numbers and map labels
        firstPass();

        // Second pass: resolve label references
        return secondPass();
    }

    /**
     * First pass: assign line numbers to each line and map label definitions
     */
    private void firstPass() {
        labelToLineNumber.clear();
        currentLineNumber = 10;

        for (String line : intermediateCode) {
            String trimmed = line.trim();
            
            // Check if this line defines a label (REM Lx)
            if (trimmed.startsWith("REM L")) {
                String[] parts = trimmed.split("\\s+");
                if (parts.length >= 2) {
                    String label = parts[1];
                    labelToLineNumber.put(label, currentLineNumber);
                }
            }
            
            currentLineNumber += lineNumberIncrement;
        }
    }

    /**
     * Second pass: generate BASIC code with line numbers and resolved labels
     */
    private String secondPass() {
        StringBuilder output = new StringBuilder();
        currentLineNumber = 10;

        for (String line : intermediateCode) {
            String trimmed = line.trim();
            
            // Skip empty lines but keep line numbering consistent
            if (trimmed.isEmpty()) {
                currentLineNumber += lineNumberIncrement;
                continue;
            }

            // Build the numbered line
            StringBuilder numberedLine = new StringBuilder();
            numberedLine.append(currentLineNumber).append(" ");

            // Resolve label references in the line
            String resolvedLine = resolveLabelReferences(trimmed);
            numberedLine.append(resolvedLine);

            output.append(numberedLine).append("\n");
            currentLineNumber += lineNumberIncrement;
        }

        return output.toString();
    }

    /**
     * Resolve GOTO Lx and THEN Lx to use line numbers
     */
    private String resolveLabelReferences(String line) {
        String result = line;

        // Pattern: GOTO Lx
        result = resolvePattern(result, "GOTO\\s+(L\\d+)");

        // Pattern: THEN GOTO Lx (from IF statements)
        result = resolvePattern(result, "THEN\\s+GOTO\\s+(L\\d+)");

        return result;
    }

    /**
     * Replace label references with line numbers using regex pattern
     */
    private String resolvePattern(String line, String pattern) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(line);
        
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String label = m.group(1);
            Integer lineNum = labelToLineNumber.get(label);
            
            if (lineNum != null) {
                // Replace the label with the line number
                String replacement = m.group(0).replace(label, lineNum.toString());
                m.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(replacement));
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * Save executable BASIC code to file
     */
    public void saveToFile(String outputPath, String basicCode) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write(basicCode);
        }
    }

    /**
     * Main processing method: read IC, process, and save BASIC
     */
    public void processFile(String inputPath, String outputPath) throws IOException {
        readIntermediateCode(inputPath);
        String basicCode = process();
        saveToFile(outputPath, basicCode);
    }

    /**
     * Set the line number increment (default is 10)
     */
    public void setLineNumberIncrement(int increment) {
        this.lineNumberIncrement = increment;
    }

    /**
     * Get the mapping of labels to line numbers (for debugging)
     */
    public Map<String, Integer> getLabelMapping() {
        return new HashMap<>(labelToLineNumber);
    }
    
    /**
     * Standalone utility to test the post-processor
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java BasicPostProcessor <input_ic_file> <output_basic_file>");
            System.out.println("Example: java BasicPostProcessor program_ic.txt program_basic.txt");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            BasicPostProcessor processor = new BasicPostProcessor();
            
            System.out.println("Reading intermediate code from: " + inputFile);
            processor.processFile(inputFile, outputFile);
            
            System.out.println("✓ Post-processing complete!");
            System.out.println("Executable BASIC code saved to: " + outputFile);
            System.out.println("\nLabel mappings:");
            processor.getLabelMapping().forEach((label, lineNum) -> 
                System.out.println("  " + label + " -> line " + lineNum)
            );
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
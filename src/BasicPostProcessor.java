import java.io.*;
import java.util.*;

public class BasicPostProcessor {
    private List<String> intermediateCode;
    private Map<String, Integer> labelToLineNumber;
    private Set<String> allVariables;
    private int lineNumberIncrement = 10;
    private int currentLineNumber;

    public BasicPostProcessor() {
        this.intermediateCode = new ArrayList<>();
        this.labelToLineNumber = new HashMap<>();
        this.allVariables = new HashSet<>();
    }

    public void readIntermediateCode(String filePath) throws IOException {
        intermediateCode.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                intermediateCode.add(line);
            }
        }
    }

    public void loadIntermediateCode(String code) {
        intermediateCode.clear();
        String[] lines = code.split("\n");
        for (String line : lines) {
            intermediateCode.add(line);
        }
    }

    public String process() {
        if (intermediateCode.isEmpty()) {
            return "";
        }
        collectVariables();
        firstPass();
        return secondPass();
    }

    private void collectVariables() {
        allVariables.clear();
        for (String line : intermediateCode) {
            String trimmed = line.trim();
            
            // Collect from assignments
            if (trimmed.matches("^[a-zA-Z][a-zA-Z0-9]* = .*")) {
                String varName = trimmed.split("=")[0].trim();
                allVariables.add(varName);
            }
            
            // Collect global variables from comment line
            if (trimmed.startsWith("REM Global variables:")) {
                String varsStr = trimmed.substring("REM Global variables:".length()).trim();
                if (varsStr.startsWith("[") && varsStr.endsWith("]")) {
                    varsStr = varsStr.substring(1, varsStr.length() - 1).trim();
                    if(!varsStr.isEmpty())
                    {
                        String[] vars = varsStr.split(",");
                        for (String var : vars) {
                            String trimmedVar = var.trim();
                            if(!trimmedVar.isEmpty())
                            {
                                allVariables.add(var.trim());
                            }
                        }
                    }
                }
            }
            
            // Collect main variables from comment line
            if (trimmed.startsWith("REM Main variables:")) {
                String varsStr = trimmed.substring("REM Main variables:".length()).trim();
                if (varsStr.startsWith("[") && varsStr.endsWith("]")) {
                    varsStr = varsStr.substring(1, varsStr.length() - 1).trim();
                    if(!varsStr.isEmpty())
                    {
                        String[] vars = varsStr.split(",");
                        for (String var : vars) {
                            String trimmedVar = var.trim();
                            if(!trimmedVar.isEmpty())
                            {
                                allVariables.add(var.trim());
                            }
                        }
                    }
                }
            }
            
            // Collect from PRINT statements
            if (trimmed.matches("PRINT [a-zA-Z][a-zA-Z0-9]*")) {
                String varName = trimmed.substring(5).trim();
                if (!varName.startsWith("\"")) {
                    allVariables.add(varName);
                }
            }
        }
    }

    private void firstPass() {
        labelToLineNumber.clear();
        
        // Reserve line numbers for variable initialization
        int varInitLines = allVariables.size();
        currentLineNumber = (varInitLines + 1) * lineNumberIncrement;
        
        for (String line : intermediateCode) {
            String trimmed = line.trim();
            
            if (trimmed.isEmpty() || trimmed.startsWith("REM ")) {
                if (trimmed.startsWith("REM L")) {
                    String[] parts = trimmed.split("\\s+");
                    if (parts.length >= 2) {
                        String label = parts[1];
                        labelToLineNumber.put(label, currentLineNumber);
                    }
                }
                currentLineNumber += lineNumberIncrement;
            } else {
                currentLineNumber += lineNumberIncrement;
            }
        }
    }

    private String secondPass() {
        StringBuilder output = new StringBuilder();
        
        // Initialize all variables starting from line 10
        currentLineNumber = 10;
        List<String> sortedVars = new ArrayList<>(allVariables);
        Collections.sort(sortedVars);
        
        for (String var : sortedVars) {
            output.append(currentLineNumber).append(" ").append(var).append(" = 0\n");
            currentLineNumber += lineNumberIncrement;
        }

        // Reset line counter for actual code (matching firstPass)
        int varInitLines = allVariables.size();
        currentLineNumber = (varInitLines + 1) * lineNumberIncrement;

        for (String line : intermediateCode) {
            String trimmed = line.trim();
            
            if (trimmed.isEmpty()) {
                currentLineNumber += lineNumberIncrement;
                continue;
            }

            if (trimmed.startsWith("REM ") && !trimmed.startsWith("REM L")) {
                currentLineNumber += lineNumberIncrement;
                continue;
            }

            StringBuilder numberedLine = new StringBuilder();
            numberedLine.append(currentLineNumber).append(" ");

            String resolvedLine = resolveLabelReferences(trimmed);
            
            if (resolvedLine.contains("CALL ")) {
                numberedLine.append("REM ").append(resolvedLine);
            } else if (resolvedLine.matches(".*= RETVAL.*")) {
                String varName = resolvedLine.split("=")[0].trim();
                numberedLine.append(varName).append(" = 0");
            } else if (resolvedLine.startsWith("RETURN")) {
                numberedLine.append("REM ").append(resolvedLine);
            } else {
                numberedLine.append(resolvedLine);
            }

            output.append(numberedLine).append("\n");
            currentLineNumber += lineNumberIncrement;
        }

        return output.toString();
    }

    private String resolveLabelReferences(String line) {
        String result = line;
        result = resolvePattern(result, "GOTO\\s+(L\\d+)");
        result = resolvePattern(result, "THEN\\s+GOTO\\s+(L\\d+)");
        result = resolvePattern(result, "THEN\\s+(L\\d+)");
        return result;
    }

    private String resolvePattern(String line, String pattern) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(line);
        
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String label = m.group(1);
            Integer lineNum = labelToLineNumber.get(label);
            
            if (lineNum != null) {
                String replacement = m.group(0).replace(label, lineNum.toString());
                m.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(replacement));
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public void saveToFile(String outputPath, String basicCode) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write(basicCode);
        }
    }

    public void processFile(String inputPath, String outputPath) throws IOException {
        readIntermediateCode(inputPath);
        String basicCode = process();
        saveToFile(outputPath, basicCode);
    }

    public void setLineNumberIncrement(int increment) {
        this.lineNumberIncrement = increment;
    }

    public Map<String, Integer> getLabelMapping() {
        return new HashMap<>(labelToLineNumber);
    }
}
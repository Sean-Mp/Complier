import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;

public class ComprehensiveSPLTest {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  COMPREHENSIVE SPL GRAMMAR TEST");
        System.out.println("===========================================\n");
        
        // Create a comprehensive test program
        String testProgram = createComprehensiveProgram();
        
        // Write to file
        String filename = "comprehensive_test.spl";
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(testProgram);
            writer.close();
            System.out.println("Test program created: " + filename);
            System.out.println("\n--- PROGRAM CONTENTS ---");
            System.out.println(testProgram);
            System.out.println("--- END PROGRAM ---\n");
        } catch (IOException e) {
            System.err.println("Error creating test file: " + e.getMessage());
            return;
        }
        
        // Test lexer
        System.out.println("\n=== PHASE 1: LEXICAL ANALYSIS ===\n");
        FileReader reader = new FileReader(filename);
        
        if (reader.getTokenizer() == null) {
            System.err.println("Lexer failed!");
            return;
        }
        
        System.out.println("\n✓ Lexer completed successfully");
        System.out.println("Total tokens: " + reader.getTokenizer().getTokenList().size());
        
        // Test parser
        System.out.println("\n=== PHASE 2: SYNTAX ANALYSIS ===\n");
        SLRParser parser = new SLRParser();
        boolean success = parser.parse(reader.getTokenizer().getTokenList());
        
        System.out.println("\n===========================================");
        if (success) {
            System.out.println("  ✓✓✓ ALL TESTS PASSED ✓✓✓");
            System.out.println("  The comprehensive SPL program is valid!");
        } else {
            System.out.println("  ✗✗✗ PARSING FAILED ✗✗✗");
        }
        System.out.println("===========================================");
    }
    
    private static String createComprehensiveProgram() {
        StringBuilder program = new StringBuilder();
        
        // Add header comment
        program.append("// Comprehensive SPL Test Program\n");
        program.append("// Tests all grammar productions\n\n");
        
        // GLOBAL VARIABLES - tests: VARIABLES, VAR
        program.append("glob {\n");
        program.append("  counter\n");
        program.append("  total\n");
        program.append("  max\n");
        program.append("}\n\n");
        
        // PROCEDURES - tests: PROCDEFS, PDEF, BODY, PARAM, MAXTHREE
        program.append("proc {\n");
        program.append("  // Procedure with no parameters\n");
        program.append("  initialize() {\n");
        program.append("    local { }\n");
        program.append("    counter = 0 ;\n");
        program.append("    total = 0 ;\n");
        program.append("    max = 100 ;\n");
        program.append("    print \"initialized\"\n");
        program.append("  }\n\n");
        
        program.append("  // Procedure with one parameter\n");
        program.append("  printnum(n) {\n");
        program.append("    local { }\n");
        program.append("    print n\n");
        program.append("  }\n\n");
        
        program.append("  // Procedure with three parameters and local variables\n");
        program.append("  display(a b c) {\n");
        program.append("    local { temp1 temp2 }\n");
        program.append("    temp1 = a ;\n");
        program.append("    temp2 = (b plus c) ;\n");
        program.append("    print temp1 ;\n");
        program.append("    print temp2\n");
        program.append("  }\n");
        program.append("}\n\n");
        
        // FUNCTIONS - tests: FUNCDEFS, FDEF, return
        program.append("func {\n");
        program.append("  // Function with arithmetic expression\n");
        program.append("  add(x y) {\n");
        program.append("    local { result }\n");
        program.append("    result = (x plus y) ;\n");
        program.append("    return result\n");
        program.append("  }\n\n");
        
        program.append("  // Function with complex expression\n");
        program.append("  compute(a b c) {\n");
        program.append("    local { temp1 temp2 answer }\n");
        program.append("    temp1 = (a mult b) ;\n");
        program.append("    temp2 = (temp1 plus c) ;\n");
        program.append("    answer = (temp2 div 2) ;\n");
        program.append("    return answer\n");
        program.append("  }\n\n");
        
        program.append("  // Function with unary operation\n");
        program.append("  negate(val) {\n");
        program.append("    local { }\n");
        program.append("    return (neg val)\n");
        program.append("  }\n\n");
        
        program.append("  // Function with boolean operations\n");
        program.append("  checkrange(num) {\n");
        program.append("    local { toobig }\n");
        program.append("    toobig = (num > max) ;\n");
        program.append("    return toobig\n");
        program.append("  }\n");
        program.append("}\n\n");
        
        // MAIN PROGRAM - tests: MAINPROG, ALGO, all INSTR types
        program.append("main {\n");
        program.append("  var {\n");
        program.append("    num1\n");
        program.append("    num2\n");
        program.append("    sum\n");
        program.append("    product\n");
        program.append("    result\n");
        program.append("    flag\n");
        program.append("  }\n\n");
        
        // Test: procedure call with no arguments
        program.append("  // Initialize global variables\n");
        program.append("  initialize() ;\n\n");
        
        // Test: simple assignment (ASSIGN ::= VAR = TERM with ATOM)
        program.append("  // Test simple assignments\n");
        program.append("  num1 = 10 ;\n");
        program.append("  num2 = 20 ;\n\n");
        
        // Test: print with string (OUTPUT ::= string)
        program.append("  // Test string output\n");
        program.append("  print \"testing\" ;\n");
        program.append("  print \"numbers\" ;\n\n");
        
        // Test: print with variable (OUTPUT ::= ATOM ::= VAR)
        program.append("  // Test variable output\n");
        program.append("  print num1 ;\n");
        program.append("  print num2 ;\n\n");
        
        // Test: function call assignment (ASSIGN ::= VAR = NAME ( INPUT ))
        program.append("  // Test function calls with different INPUT variations\n");
        program.append("  sum = add(num1 num2) ;\n");
        program.append("  print sum ;\n\n");
        
        // Test: arithmetic expressions with TERM
        program.append("  // Test arithmetic operations\n");
        program.append("  result = (num1 plus num2) ;\n");
        program.append("  product = (num1 mult num2) ;\n");
        program.append("  result = (product minus num1) ;\n");
        program.append("  result = (result div 5) ;\n\n");
        
        // Test: nested expressions
        program.append("  // Test nested expressions\n");
        program.append("  result = ((num1 plus num2) mult (num2 minus 5)) ;\n\n");
        
        // Test: unary operations
        program.append("  // Test unary operations\n");
        program.append("  result = (neg num1) ;\n");
        program.append("  flag = (not 0) ;\n\n");
        
        // Test: comparison operations
        program.append("  // Test comparison operations\n");
        program.append("  flag = (num1 > 5) ;\n");
        program.append("  flag = (num1 eq num2) ;\n\n");
        
        // Test: boolean operations
        program.append("  // Test boolean operations\n");
        program.append("  flag = ((num1 > 5) and (num2 > 10)) ;\n");
        program.append("  flag = ((num1 eq 0) or (num2 eq 0)) ;\n\n");
        
        // Test: WHILE loop (LOOP ::= while TERM { ALGO })
        program.append("  // Test while loop\n");
        program.append("  counter = 0 ;\n");
        program.append("  while (counter > (neg 5)) {\n");
        program.append("    print counter ;\n");
        program.append("    counter = (counter minus 1) ;\n");
        program.append("    print \"loop\"\n");
        program.append("  }\n\n");
        
        // Test: DO-UNTIL loop (LOOP ::= do { ALGO } until TERM)
        program.append("  // Test do-until loop\n");
        program.append("  counter = 0 ;\n");
        program.append("  do {\n");
        program.append("    counter = (counter plus 1) ;\n");
        program.append("    print counter\n");
        program.append("  } until (counter > 3) ;\n\n");
        
        // Test: IF without else (BRANCH ::= if TERM { ALGO })
        program.append("  // Test if without else\n");
        program.append("  if (num1 > 5) {\n");
        program.append("    print \"large\" ;\n");
        program.append("    print num1\n");
        program.append("  }\n\n");
        
        // Test: IF with else (BRANCH ::= if TERM { ALGO } else { ALGO })
        program.append("  // Test if-else\n");
        program.append("  if (num1 eq num2) {\n");
        program.append("    print \"equal\"\n");
        program.append("  } else {\n");
        program.append("    print \"notequal\" ;\n");
        program.append("    result = (num1 minus num2)\n");
        program.append("  }\n\n");
        
        // Test: nested control structures
        program.append("  // Test nested control structures\n");
        program.append("  if (num1 > 0) {\n");
        program.append("    counter = 0 ;\n");
        program.append("    while (counter > (neg 3)) {\n");
        program.append("      if (counter eq 0) {\n");
        program.append("        print \"zero\"\n");
        program.append("      } else {\n");
        program.append("        print counter\n");
        program.append("      }\n");
        program.append("      counter = (counter minus 1)\n");
        program.append("    }\n");
        program.append("  }\n\n");
        
        // Test: procedure calls with different numbers of arguments
        program.append("  // Test procedure calls\n");
        program.append("  printnum(sum) ;\n");
        program.append("  display(num1 num2 sum) ;\n\n");
        
        // Test: complex function call with three arguments
        program.append("  // Test complex function call\n");
        program.append("  result = compute(10 20 30) ;\n");
        program.append("  print result ;\n\n");
        
        // Test: function returning result used in expression
        program.append("  // Test function result in expression\n");
        program.append("  result = (add(5 10) plus add(15 20)) ;\n");
        program.append("  print result ;\n\n");
        
        // Final output and halt
        program.append("  // Final output\n");
        program.append("  print \"completed\" ;\n");
        program.append("  print total ;\n");
        program.append("  halt\n");
        program.append("}\n");
        
        return program.toString();
    }
}
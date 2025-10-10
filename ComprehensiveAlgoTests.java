import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;

public class ComprehensiveAlgoTests {
    
    public static void runTest(String testName, String testProgram, String filename) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  RUNNING: " + testName);
        System.out.println("=".repeat(60));
        
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(testProgram);
            writer.close();
            System.out.println("✓ Test file created: " + filename);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
        
        System.out.println("\n=== LEXICAL ANALYSIS ===");
        FileReader reader = new FileReader(filename);
        
        if (reader.getTokenizer() == null) {
            System.err.println("✗ Lexer failed");
            return;
        }
        
        System.out.println("✓ Tokens: " + reader.getTokenizer().getTokenList().size());
        
        System.out.println("\n=== SYNTAX ANALYSIS ===");
        SLRParser parser = new SLRParser();
        boolean success = parser.parse(reader.getTokenizer().getTokenList());
        
        System.out.println("\n" + "=".repeat(50));
        if (success) {
            System.out.println("✓✓✓ " + testName + " - PARSING SUCCESS ✓✓✓");
        } else {
            System.out.println("✗✗✗ " + testName + " - PARSING FAILED ✗✗✗");
        }
        System.out.println("=".repeat(50));
    }
    
    public static void main(String[] args) {
        // Run all tests
        runTest("ORIGINAL COMPREHENSIVE TEST", originalTest, "test_original.spl");
        runTest("EDGE CASES TEST", edgeCaseTest, "test_edge_cases.spl"); 
        runTest("CONTROL FLOW TEST", controlFlowTest, "test_control_flow.spl");
        runTest("SIMULATION TEST", simulationTest, "test_simulation.spl");
        runTest("MINIMAL TEST", minimalTest, "test_minimal.spl");
    }
    
    // Paste all the test strings here...
    static String originalTest =
            "// Test all instruction types\n" +
            "\n" +
            "// Simple assignments\n" +
            "x = 5 ;\n" +
            "y = 10 ;\n" +
            "z = 0 ;\n" +
            "\n" +
            "// Arithmetic expressions\n" +
            "result = (x plus y) ;\n" +
            "result = (x minus y) ;\n" +
            "result = (x mult y) ;\n" +
            "result = (x div 2) ;\n" +
            "\n" +
            "// Nested expressions\n" +
            "result = ((x plus y) mult (y minus 3)) ;\n" +
            "result = (((x plus 1) mult 2) div (y minus 1)) ;\n" +
            "\n" +
            "// Unary operations\n" +
            "result = (neg x) ;\n" +
            "flag = (not z) ;\n" +
            "result = (neg (neg x)) ;\n" +
            "\n" +
            "// Comparisons\n" +
            "flag = (x > 5) ;\n" +
            "flag = (x eq y) ;\n" +
            "flag = ((x plus 1) > (y minus 1)) ;\n" +
            "\n" +
            "// Boolean operations\n" +
            "flag = ((x > 5) and (y > 5)) ;\n" +
            "flag = ((x eq 0) or (y eq 0)) ;\n" +
            "flag = ((x > 0) and ((y > 0) or (z eq 0))) ;\n" +
            "\n" +
            "// Print statements\n" +
            "print \"start\" ;\n" +
            "print x ;\n" +
            "print y ;\n" +
            "print \"values\" ;\n" +
            "print result ;\n" +
            "\n" +
            "// While loop\n" +
            "counter = 5 ;\n" +
            "while (counter > 0) {\n" +
            "  print counter ;\n" +
            "  counter = (counter minus 1) ;\n" +  // ADDED SEMICOLON
            "  print \"looping\"\n" +
            "} ;\n" +
            "\n" +
            "// Do-until loop\n" +
            "counter = 0 ;\n" +
            "do {\n" +
            "  counter = (counter plus 1) ;\n" +
            "  print counter ;\n" +
            "  print \"until\"\n" +
            "} until (counter > 3) ;\n" +
            "\n" +
            "// If without else\n" +
            "if (x > 0) {\n" +
            "  print \"positive\" ;\n" +
            "  result = x\n" +  // Last statement in block - no semicolon needed
            "} ;\n" +
            "\n" +
            "// If-else\n" +
            "if (x eq y) {\n" +
            "  print \"equal\" ;\n" +
            "  result = 0\n" +  // Last statement in block - no semicolon needed
            "} else {\n" +
            "  print \"notequal\" ;\n" +
            "  result = (x minus y)\n" +  // Last statement in block - no semicolon needed
            "} ;\n" +
            "\n" +
            "// Nested if in while\n" +
            "counter = 3 ;\n" +
            "while (counter > 0) {\n" +
            "  if (counter eq 2) {\n" +
            "    print \"two\"\n" +
            "  } else {\n" +
            "    print counter\n" +
            "  } ;\n" +
            "  counter = (counter minus 1)\n" +  // Last statement - no semicolon
            "} ;\n" +
            "\n" +
            "// Procedure call (no return)\n" +
            "initialize() ;\n" +
            "display(x y z) ;\n" +
            "\n" +
            "// Function call assignments\n" +
            "result = add(x y) ;\n" +
            "sum = compute(10 20 30) ;\n" +
            "\n" +
            "// Final sequence testing ALGO conflict\n" +
            "print \"done\" ;\n" +
            "print result ;\n" +
            "print sum ;\n" +
            "halt\n";  // Last instruction - no semicolon needed

        static String edgeCaseTest = 
            "// Edge cases\n" +
            "\n" +
            "// Complex nested\n" +
            "r = (((x plus (y mult 2)) div (z minus 1)) plus (neg x)) ;\n" +
            "f = ((not (x eq 0)) and ((y > 5) or (z eq 1))) ;\n" +
            "\n" +
            "// Multiple unary\n" +
            "r = (neg (neg (neg x))) ;\n" +
            "f = (not (not (x eq y))) ;\n" +
            "\n" +
            "// Deep nesting\n" +
            "r = ((((x plus 1) mult (y minus 1)) div ((z plus 2) minus 1)) plus x) ;\n" +
            "\n" +
            "// Mixed arithmetic\n" +
            "f = (((x plus y) > (z mult 2)) and ((x minus y) eq 0)) ;\n" +
            "\n" +
            "// Complex boolean\n" +
            "f = ((x > 0) and (y > 0) and (z eq 0) or (x eq y)) ;\n" +
            "\n" +
            "// Procedure calls\n" +
            "setup() ;\n" +
            "init(a b c) ;\n" +
            "proc(data) ;\n" +
            "clean() ;\n" +
            "\n" +
            "// Function calls\n" +
            "v = calc(b o) ;\n" +
            "t = sum(v 10 20) ;\n" +
            "fn = comp(t (x plus y) z) ;\n" +
            "\n" +
            "print r ;\n" +
            "print f ;\n" +
            "halt\n";

        static String controlFlowTest = 
            "// Control flow\n" +
            "\n" +
            "c = 5 ;\n" +
            "t = 0 ;\n" +
            "msg = 999 ;\n" +
            "\n" +
            "// Nested while\n" +
            "o = 2 ;\n" +
            "while (o > 0) {\n" +
            "  i = 3 ;\n" +
            "  print msg ;\n" +
            "  \n" +
            "  while (i > 0) {\n" +
            "    print msg ;\n" +
            "    t = (t plus 1) ;\n" +
            "    i = (i minus 1)\n" +
            "  } ;\n" +
            "  \n" +
            "  o = (o minus 1)\n" +
            "} ;\n" +
            "\n" +
            "// Do-until\n" +
            "v = 0 ;\n" +
            "do {\n" +
            "  v = (v plus 1) ;\n" +
            "  print v ;\n" +
            "  \n" +
            "  if (v eq 3) {\n" +
            "    print v ;\n" +
            "    r = (v mult 2)\n" +
            "  } else {\n" +
            "    if (v > 3) {\n" +
            "      print v\n" +
            "    } else {\n" +
            "      print v\n" +
            "    }\n" +
            "  }\n" +
            "} until (v > 5) ;\n" +
            "\n" +
            "// If-else chain\n" +
            "s = 75 ;\n" +
            "if (s > 90) {\n" +
            "  g = 1 ;\n" +
            "  print g\n" +
            "} else {\n" +
            "  if (s > 80) {\n" +
            "    g = 2 ;\n" +
            "    print g\n" +
            "  } else {\n" +
            "    if (s > 70) {\n" +
            "      g = 3 ;\n" +
            "      print g\n" +
            "    } else {\n" +
            "      g = 4 ;\n" +
            "      print g\n" +
            "    }\n" +
            "  }\n" +
            "} ;\n" +
            "\n" +
            "print t ;\n" +
            "halt\n";

        static String simulationTest = 
            "// Simulation\n" +
            "\n" +
            "s = 100 ;\n" +
            "p = 25 ;\n" +
            "sd = 0 ;\n" +
            "r = 0 ;\n" +
            "msg1 = 111 ;\n" +
            "msg2 = 222 ;\n" +
            "\n" +
            "print msg1 ;\n" +
            "\n" +
            "// Process sales\n" +
            "while (s > 0) {\n" +
            "  sl = 5 ;\n" +
            "  \n" +
            "  if (sl > s) {\n" +
            "    sl = s ;\n" +
            "    print msg2\n" +
            "  } ;\n" +
            "  \n" +
            "  s = (s minus sl) ;\n" +
            "  sd = (sd plus sl) ;\n" +
            "  br = (sl mult p) ;\n" +
            "  r = (r plus br) ;\n" +
            "  \n" +
            "  print sd ;\n" +
            "  print sl ;\n" +
            "  print br ;\n" +
            "  print s\n" +
            "} ;\n" +
            "\n" +
            "// Calculate stats\n" +
            "ap = (r div sd) ;\n" +
            "e = ((sd mult 100) div 100) ;\n" +
            "\n" +
            "print msg1 ;\n" +
            "print sd ;\n" +
            "print r ;\n" +
            "print ap ;\n" +
            "\n" +
            "// Restock logic\n" +
            "if (e > 80) {\n" +
            "  print e ;\n" +
            "  oq = 150 ;\n" +
            "  proc(oq)\n" +
            "} else {\n" +
            "  if (e > 50) {\n" +
            "    print e ;\n" +
            "    oq = 80\n" +
            "  } else {\n" +
            "    print e ;\n" +
            "    oq = 0\n" +
            "  }\n" +
            "} ;\n" +
            "\n" +
            "halt\n";

        static String minimalTest = 
            "// Minimal test\n" +
            "\n" +
            "// Empty calls\n" +
            "np() ;\n" +
            "\n" +
            "// Single param\n" +
            "r = id(x) ;\n" +
            "inc(y) ;\n" +
            "\n" +
            "// Two params  \n" +
            "sm = add(a b) ;\n" +
            "cmp(x y) ;\n" +
            "\n" +
            "// Three params\n" +
            "t = sum3(1 2 3) ;\n" +
            "pr3(a b c) ;\n" +
            "\n" +
            "// Minimal loops\n" +
            "c = 0 ;\n" +
            "msg = 555 ;\n" +
            "while (c > 0) {\n" +
            "  print msg\n" +
            "} ;\n" +
            "\n" +
            "// Single do-until\n" +
            "do {\n" +
            "  print msg\n" +
            "} until (1 > 0) ;\n" +
            "\n" +
            "// Simple if\n" +
            "if (1 > 0) {\n" +
            "  print msg\n" +
            "} ;\n" +
            "\n" +
            "// Print types\n" +
            "print msg ;\n" +
            "print 42 ;\n" +
            "print v ;\n" +
            "\n" +
            "halt\n";
}
import Lexer.*;
import Parser.*;
import java.io.FileWriter;
import java.io.IOException;

public class CompleteSPLTest {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  COMPLETE SPL PROGRAM TESTS");
        System.out.println("===========================================\n");
        
        // Run all complete SPL tests
        runCompleteTest("BASIC COMPLETE SPL", basicCompleteTest(), "complete_basic.spl");
        runCompleteTest("COMPREHENSIVE COMPLETE SPL", comprehensiveCompleteTest(), "complete_comprehensive.spl");
        runCompleteTest("CONTROL FLOW COMPLETE SPL", controlFlowCompleteTest(), "complete_control_flow.spl");
        runCompleteTest("PROCEDURE/FUNCTION COMPLETE SPL", procedureFunctionTest(), "complete_proc_func.spl");
        runCompleteTest("MINIMAL COMPLETE SPL", minimalCompleteTest(), "complete_minimal.spl");
    }
    
    public static void runCompleteTest(String testName, String testProgram, String filename) {
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
    
    // Test 1: Basic Complete SPL Program
    static String basicCompleteTest() {
        return 
            "glob { x y z result }\n" +
            "proc { \n" +
            "  init ( ) { local { } x = 0 ; y = 0 ; z = 0 }\n" +
            "  show ( a b ) { local { } print a ; print b }\n" +
            "}\n" +
            "func {\n" +
            "  add ( x y ) { local { } return x }\n" +
            "  mult ( a b ) { local { } return a }\n" +
            "}\n" +
            "main {\n" +
            "  var { x y z result }\n" +
            "  x = 5 ;\n" +
            "  y = 10 ;\n" +
            "  z = 0 ;\n" +
            "  result = (x plus y) ;\n" +
            "  result = (x mult y) ;\n" +
            "  print x ;\n" +
            "  print y ;\n" +
            "  print result ;\n" +
            "  init() ;\n" +
            "  show(x y) ;\n" +
            "  result = add(x y) ;\n" +
            "  result = mult(x y) ;\n" +
            "  halt\n" +
            "}\n";
    }
    
    // Test 2: Comprehensive Complete SPL Program
    static String comprehensiveCompleteTest() {
        return 
            "glob { x y z result flag counter sum avg }\n" +
            "proc { \n" +
            "  setup ( ) { local { } x = 0 ; y = 0 ; z = 0 }\n" +
            "  display ( a b c ) { local { } print a ; print b ; print c }\n" +
            "  reset ( ) { local { } counter = 0 ; sum = 0 }\n" +
            "}\n" +
            "func {\n" +
            "  calculate ( x y ) { local { r } r = (x plus y) ; return r }\n" +
            "  average ( a b ) { local { r } r = ((a plus b) div 2) ; return r }\n" +
            "  max ( x y ) { local { r } if (x > y) { r = x } else { r = y } ; return r }\n" +
            "}\n" +
            "main {\n" +
            "  var { x y z result flag counter sum avg }\n" +
            "  \n" +
            "  // Initialize\n" +
            "  x = 15 ;\n" +
            "  y = 25 ;\n" +
            "  z = 0 ;\n" +
            "  \n" +
            "  // Arithmetic operations\n" +
            "  result = (x plus y) ;\n" +
            "  result = (x minus y) ;\n" +
            "  result = (x mult y) ;\n" +
            "  result = (x div 3) ;\n" +
            "  \n" +
            "  // Complex expressions\n" +
            "  result = ((x plus y) mult (y minus 5)) ;\n" +
            "  result = (((x plus 1) mult 2) div (y minus 1)) ;\n" +
            "  \n" +
            "  // Unary operations\n" +
            "  result = (neg x) ;\n" +
            "  flag = (not z) ;\n" +
            "  \n" +
            "  // Comparisons\n" +
            "  flag = (x > 10) ;\n" +
            "  flag = (x eq y) ;\n" +
            "  flag = ((x plus 1) > (y minus 1)) ;\n" +
            "  \n" +
            "  // Boolean operations\n" +
            "  flag = ((x > 10) and (y > 10)) ;\n" +
            "  flag = ((x eq 0) or (y eq 0)) ;\n" +
            "  flag = ((x > 0) and ((y > 0) or (z eq 0))) ;\n" +
            "  \n" +
            "  // Procedure calls\n" +
            "  setup() ;\n" +
            "  display(x y z) ;\n" +
            "  reset() ;\n" +
            "  \n" +
            "  // Function calls\n" +
            "  result = calculate(x y) ;\n" +
            "  avg = average(x y) ;\n" +
            "  result = max(x y) ;\n" +
            "  \n" +
            "  // Print results\n" +
            "  print result ;\n" +
            "  print avg ;\n" +
            "  print flag ;\n" +
            "  \n" +
            "  halt\n" +
            "}\n";
    }
    
    // Test 3: Control Flow Complete SPL Program
    static String controlFlowCompleteTest() {
        return 
            "glob { counter total value score grade }\n" +
            "proc { \n" +
            "  start ( ) { local { } counter = 0 ; total = 0 }\n" +
            "  end ( ) { local { } print total }\n" +
            "}\n" +
            "func {\n" +
            "  check ( n ) { local { r } if (n > 10) { r = 1 } else { r = 0 } ; return r }\n" +
            "}\n" +
            "main {\n" +
            "  var { counter total value score grade }\n" +
            "  \n" +
            "  start() ;\n" +
            "  \n" +
            "  // While loop\n" +
            "  counter = 5 ;\n" +
            "  while (counter > 0) {\n" +
            "    print counter ;\n" +
            "    total = (total plus counter) ;\n" +
            "    counter = (counter minus 1)\n" +
            "  } ;\n" +
            "  \n" +
            "  // Do-until loop\n" +
            "  value = 0 ;\n" +
            "  do {\n" +
            "    value = (value plus 1) ;\n" +
            "    print value ;\n" +
            "    if (value eq 3) {\n" +
            "      print value\n" +
            "    } else {\n" +
            "      if (value > 3) {\n" +
            "        print value\n" +
            "      } else {\n" +
            "        print value\n" +
            "      }\n" +
            "    }\n" +
            "  } until (value > 5) ;\n" +
            "  \n" +
            "  // If-else chain\n" +
            "  score = 85 ;\n" +
            "  if (score > 90) {\n" +
            "    grade = 1 ;\n" +
            "    print grade\n" +
            "  } else {\n" +
            "    if (score > 80) {\n" +
            "      grade = 2 ;\n" +
            "      print grade\n" +
            "    } else {\n" +
            "      if (score > 70) {\n" +
            "        grade = 3 ;\n" +
            "        print grade\n" +
            "      } else {\n" +
            "        grade = 4 ;\n" +
            "        print grade\n" +
            "      }\n" +
            "    }\n" +
            "  } ;\n" +
            "  \n" +
            "  // Function call in condition\n" +
            "  if (check(total) eq 1) {\n" +
            "    print total\n" +
            "  } ;\n" +
            "  \n" +
            "  end() ;\n" +
            "  halt\n" +
            "}\n";
    }
    
    // Test 4: Procedure and Function Focused Test
    static String procedureFunctionTest() {
        return 
            "glob { a b c d res1 res2 res3 }\n" +
            "proc { \n" +
            "  proc1 ( ) { local { } a = 1 ; b = 2 }\n" +
            "  proc2 ( x ) { local { } print x }\n" +
            "  proc3 ( x y ) { local { } print x ; print y }\n" +
            "  proc4 ( x y z ) { local { } print x ; print y ; print z }\n" +
            "}\n" +
            "func {\n" +
            "  func1 ( ) { local { r } r = 100 ; return r }\n" +
            "  func2 ( x ) { local { r } r = (x plus 10) ; return r }\n" +
            "  func3 ( x y ) { local { r } r = (x plus y) ; return r }\n" +
            "  func4 ( x y z ) { local { r } r = ((x plus y) plus z) ; return r }\n" +
            "}\n" +
            "main {\n" +
            "  var { a b c d res1 res2 res3 }\n" +
            "  \n" +
            "  // Test all procedure patterns\n" +
            "  proc1() ;\n" +
            "  proc2(a) ;\n" +
            "  proc3(a b) ;\n" +
            "  proc4(a b c) ;\n" +
            "  \n" +
            "  // Test all function patterns\n" +
            "  res1 = func1() ;\n" +
            "  res2 = func2(a) ;\n" +
            "  res3 = func3(a b) ;\n" +
            "  res1 = func4(a b c) ;\n" +
            "  \n" +
            "  // Print results\n" +
            "  print res1 ;\n" +
            "  print res2 ;\n" +
            "  print res3 ;\n" +
            "  \n" +
            "  halt\n" +
            "}\n";
    }
    
    // Test 5: Minimal Complete SPL Program
    static String minimalCompleteTest() {
        return 
            "glob { x }\n" +
            "proc { }\n" +
            "func { }\n" +
            "main {\n" +
            "  var { x }\n" +
            "  x = 42 ;\n" +
            "  print x ;\n" +
            "  halt\n" +
            "}\n";
    }
}
package Parser;

import java.util.*;

public class ParseTable {
    
    public enum ActionType {
        SHIFT, REDUCE, ACCEPT, ERROR
    }
    
    public static class Action {
        public ActionType type;
        public int value;
        
        public Action(ActionType type, int value) {
            this.type = type;
            this.value = value;
        }
        
        @Override
        public String toString() {
            switch (type) {
                case SHIFT: return "s" + value;
                case REDUCE: return "r" + value;
                case ACCEPT: return "acc";
                default: return "error";
            }
        }
    }
    
    private Map<Integer, Map<String, Action>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;
    
    public ParseTable() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
        buildTables();
    }
    
    private void buildTables() {
        buildState0();
        buildState1();   // After halt
        buildState2();   // After print
        buildState3();   // After INSTR (CONFLICT STATE - ALWAYS SHIFT ON ;)
        buildState6();   // After OUTPUT
        buildState7();   // After ; (continuing ALGO)
        buildState8();   // After INSTR ; ALGO
        buildState10();  // ALGO complete
        buildState11();  // After user-defined-name
        buildState12();  // After VAR =
        buildState13();  // After number
        buildState14();  // After complete TERM in assignment
        buildState15();  // After (
        buildState16();  // After UNOP
        buildState17();  // After first TERM in parentheses
        buildState18();  // After BINOP
        buildState19();  // After TERM BINOP TERM
        buildState40();  // After NAME (
        buildState41();  // After INPUT
        buildState42();  // After ) from function call
        buildState43();  // ATOM in expression - will reduce to TERM
        buildState44();  // After ) in unary
        buildState45();  // After ) in binary
        buildState50();  // VAR/number/string as ATOM/OUTPUT (not in expression)
        buildState51();  // STRING as OUTPUT
        buildState52();  // user-defined-name in expression (reduces to VAR)
        buildState53();  // VAR in expression (reduces to ATOM)
        buildState46();  // After ( UNOP TERM )
        
        // Control flow states
        buildState20();  // After while
        buildState21();  // After TERM in while
        buildState22();  // After { in while
        buildState23();  // After ALGO in while
        buildState24();  // After } in while
        
        buildState25();  // After do
        buildState26();  // After { in do
        buildState27();  // After ALGO in do
        buildState28();  // After } in do
        buildState29();  // After until
        buildState30();  // After TERM in until
        
        buildState31();  // After if
        buildState32();  // After TERM in if
        buildState33();  // After { in if
        buildState34();  // After ALGO in if
        buildState35();  // After } in if
        buildState36();  // After else
        buildState37();  // After { in else
        buildState38();  // After ALGO in else
        buildState39();  // After } in else
    }
    
    private void buildState0() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 11));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(0, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 10);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotoTable.put(0, gotos);
    }
    
    private void buildState1() {
        // After halt
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 22)); // INSTR -> halt
        actions.put("$", new Action(ActionType.REDUCE, 22));
        actions.put("}", new Action(ActionType.REDUCE, 22));
        actionTable.put(1, actions);
    }
    
    private void buildState2() {
        // After print - expect OUTPUT
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 50));
        actions.put("string", new Action(ActionType.SHIFT, 51));
        actions.put("number", new Action(ActionType.SHIFT, 50));
        actionTable.put(2, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("OUTPUT", 6);
        gotos.put("ATOM", 50);  // ATOM will reduce to OUTPUT
        gotos.put("VAR", 50);
        gotoTable.put(2, gotos);
    }
    
    private void buildState3() {
        // CRITICAL: CONFLICT STATE - ALWAYS SHIFT ON ;
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.SHIFT, 7)); // SHIFT wins!
        actions.put("}", new Action(ActionType.REDUCE, 20)); // ALGO -> INSTR
        actions.put("$", new Action(ActionType.REDUCE, 20));
        actionTable.put(3, actions);
    }
    
    private void buildState6() {
        // After print OUTPUT
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 23)); // INSTR -> print OUTPUT
        actions.put("}", new Action(ActionType.REDUCE, 23));
        actions.put("$", new Action(ActionType.REDUCE, 23));
        actionTable.put(6, actions);
    }
    
    private void buildState7() {
        // After semicolon - expect another instruction
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 11));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(7, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 8);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotoTable.put(7, gotos);
    }
    
    private void buildState8() {
        // After INSTR ; ALGO
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.REDUCE, 21)); // ALGO -> INSTR ; ALGO
        actions.put("$", new Action(ActionType.REDUCE, 21));
        actionTable.put(8, actions);
    }
    
    private void buildState10() {
        // ALGO complete
        Map<String, Action> actions = new HashMap<>();
        actions.put("$", new Action(ActionType.ACCEPT, 0));
        actions.put("}", new Action(ActionType.REDUCE, 20));
        actionTable.put(10, actions);
    }
    
    private void buildState11() {
        // After user-defined-name (could be VAR or NAME)
        Map<String, Action> actions = new HashMap<>();
        actions.put("=", new Action(ActionType.SHIFT, 12));
        actions.put("(", new Action(ActionType.SHIFT, 40));
        // If followed by operators in expression context, it's an ATOM -> shift to expression handler
        actionTable.put(11, actions);
    }
    
    private void buildState12() {
        // After VAR = (in assignment)
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52)); // Will reduce to VAR then ATOM
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(12, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 14);
        gotos.put("ATOM", 43);  // ATOM goes to state 43 which reduces to TERM
        gotos.put("VAR", 53);   // VAR goes to state 53 to reduce to ATOM
        gotoTable.put(12, gotos);
    }
    
    private void buildState13() {
        // After number - this is always an ATOM
        Map<String, Action> actions = new HashMap<>();
        // In expression context - reduce to ATOM first
        actions.put(";", new Action(ActionType.REDUCE, 19)); // ATOM -> number
        actions.put("}", new Action(ActionType.REDUCE, 19));
        actions.put(")", new Action(ActionType.REDUCE, 19));
        actions.put("plus", new Action(ActionType.REDUCE, 19));
        actions.put("minus", new Action(ActionType.REDUCE, 19));
        actions.put("mult", new Action(ActionType.REDUCE, 19));
        actions.put("div", new Action(ActionType.REDUCE, 19));
        actions.put("eq", new Action(ActionType.REDUCE, 19));
        actions.put(">", new Action(ActionType.REDUCE, 19));
        actions.put("and", new Action(ActionType.REDUCE, 19));
        actions.put("or", new Action(ActionType.REDUCE, 19));
        actions.put("$", new Action(ActionType.REDUCE, 19)); // End of input
        actions.put("{", new Action(ActionType.REDUCE, 19)); // Before loop body
        actionTable.put(13, actions);
    }
    
    private void buildState14() {
        // After complete TERM in assignment (VAR = TERM)
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 29)); // ASSIGN -> VAR = TERM
        actions.put("}", new Action(ActionType.REDUCE, 29));
        actions.put("$", new Action(ActionType.REDUCE, 29));
        actionTable.put(14, actions);
    }
    
    private void buildState15() {
        // After ( - expect UNOP or TERM
        Map<String, Action> actions = new HashMap<>();
        actions.put("neg", new Action(ActionType.SHIFT, 16));
        actions.put("not", new Action(ActionType.SHIFT, 16));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52)); // Will reduce to VAR
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15)); // Nested expression
        actionTable.put(15, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("UNOP", 16);
        gotos.put("TERM", 17);
        gotos.put("ATOM", 43);  // ATOM goes to state 43 to reduce to TERM
        gotos.put("VAR", 53);   // VAR goes to 53 to reduce to ATOM
        gotoTable.put(15, gotos);
    }
    
    private void buildState16() {
        // After UNOP - expect TERM
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(16, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 44);  // After UNOP TERM, go to state that shifts )
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(16, gotos);
    }
    
    private void buildState17() {
        // After first TERM in ( TERM ... )
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 44)); // Just TERM, close it
        // Or continue with BINOP
        actions.put("plus", new Action(ActionType.SHIFT, 18));
        actions.put("minus", new Action(ActionType.SHIFT, 18));
        actions.put("mult", new Action(ActionType.SHIFT, 18));
        actions.put("div", new Action(ActionType.SHIFT, 18));
        actions.put("eq", new Action(ActionType.SHIFT, 18));
        actions.put(">", new Action(ActionType.SHIFT, 18));
        actions.put("and", new Action(ActionType.SHIFT, 18));
        actions.put("or", new Action(ActionType.SHIFT, 18));
        actionTable.put(17, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("BINOP", 18);
        gotoTable.put(17, gotos);
    }
    
    private void buildState18() {
        // After TERM BINOP - expect another TERM
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(18, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 19);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(18, gotos);
    }
    
    private void buildState19() {
        // After TERM BINOP TERM - must close with )
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 45));
        actionTable.put(19, actions);
    }
    
    private void buildState40() {
        // After NAME ( - expect INPUT
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 50));
        actions.put("number", new Action(ActionType.SHIFT, 50));
        actions.put(")", new Action(ActionType.REDUCE, 36)); // INPUT -> epsilon
        actionTable.put(40, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("INPUT", 41);
        gotos.put("ATOM", 41);  // Single ATOM as INPUT
        gotoTable.put(40, gotos);
    }
    
    private void buildState41() {
        // After INPUT
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 42));
        actionTable.put(41, actions);
    }
    
    private void buildState42() {
        // After NAME ( INPUT )
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 24)); // INSTR -> NAME ( INPUT )
        actions.put("}", new Action(ActionType.REDUCE, 24));
        actions.put("$", new Action(ActionType.REDUCE, 24));
        actionTable.put(42, actions);
    }
    
    private void buildState43() {
        // ATOM in expression context - reduce to TERM
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.REDUCE, 40)); // TERM -> ATOM
        actions.put("plus", new Action(ActionType.REDUCE, 40));
        actions.put("minus", new Action(ActionType.REDUCE, 40));
        actions.put("mult", new Action(ActionType.REDUCE, 40));
        actions.put("div", new Action(ActionType.REDUCE, 40));
        actions.put("eq", new Action(ActionType.REDUCE, 40));
        actions.put(">", new Action(ActionType.REDUCE, 40));
        actions.put("and", new Action(ActionType.REDUCE, 40));
        actions.put("or", new Action(ActionType.REDUCE, 40));
        actions.put(";", new Action(ActionType.REDUCE, 40)); // End of assignment
        actions.put("}", new Action(ActionType.REDUCE, 40));
        actions.put("{", new Action(ActionType.REDUCE, 40)); // Before loop body
        actions.put("$", new Action(ActionType.REDUCE, 40)); // End of input
        actionTable.put(43, actions);
    }
    
    private void buildState44() {
        // After ( UNOP TERM - need to shift )
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 46)); // NEW STATE for after ( UNOP TERM )
        actionTable.put(44, actions);
    }
    
    private void buildState45() {
        // After ( TERM BINOP TERM )
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 42)); // TERM -> ( TERM BINOP TERM )
        actions.put("}", new Action(ActionType.REDUCE, 42));
        actions.put(")", new Action(ActionType.REDUCE, 42));
        actions.put("{", new Action(ActionType.REDUCE, 42));
        actions.put("$", new Action(ActionType.REDUCE, 42)); // End of input
        actions.put("plus", new Action(ActionType.REDUCE, 42));
        actions.put("minus", new Action(ActionType.REDUCE, 42));
        actions.put("mult", new Action(ActionType.REDUCE, 42));
        actions.put("div", new Action(ActionType.REDUCE, 42));
        actions.put("eq", new Action(ActionType.REDUCE, 42));
        actions.put(">", new Action(ActionType.REDUCE, 42));
        actions.put("and", new Action(ActionType.REDUCE, 42));
        actions.put("or", new Action(ActionType.REDUCE, 42));
        actionTable.put(45, actions);
    }
    
    private void buildState50() {
        // VAR or number in OUTPUT/INPUT context (not expression)
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 34)); // OUTPUT -> ATOM
        actions.put("}", new Action(ActionType.REDUCE, 34));
        actions.put("$", new Action(ActionType.REDUCE, 34)); // End of input
        actions.put(")", new Action(ActionType.REDUCE, 37)); // INPUT -> ATOM
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 50)); // More ATOMs for INPUT
        actions.put("number", new Action(ActionType.SHIFT, 50));
        actionTable.put(50, actions);
    }
    
    private void buildState51() {
        // string as OUTPUT
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 35)); // OUTPUT -> string
        actions.put("}", new Action(ActionType.REDUCE, 35));
        actions.put("$", new Action(ActionType.REDUCE, 35)); // End of input
        actionTable.put(51, actions);
    }
    
    private void buildState52() {
        // user-defined-name in expression context - reduce to VAR
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 3)); // VAR -> user-defined-name
        actions.put("}", new Action(ActionType.REDUCE, 3));
        actions.put(")", new Action(ActionType.REDUCE, 3));
        actions.put("$", new Action(ActionType.REDUCE, 3));
        actions.put("{", new Action(ActionType.REDUCE, 3));
        actions.put("plus", new Action(ActionType.REDUCE, 3));
        actions.put("minus", new Action(ActionType.REDUCE, 3));
        actions.put("mult", new Action(ActionType.REDUCE, 3));
        actions.put("div", new Action(ActionType.REDUCE, 3));
        actions.put("eq", new Action(ActionType.REDUCE, 3));
        actions.put(">", new Action(ActionType.REDUCE, 3));
        actions.put("and", new Action(ActionType.REDUCE, 3));
        actions.put("or", new Action(ActionType.REDUCE, 3));
        actionTable.put(52, actions);
    }
    
    private void buildState53() {
        // VAR in expression context - reduce to ATOM
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 18)); // ATOM -> VAR
        actions.put("}", new Action(ActionType.REDUCE, 18));
        actions.put(")", new Action(ActionType.REDUCE, 18));
        actions.put("$", new Action(ActionType.REDUCE, 18));
        actions.put("{", new Action(ActionType.REDUCE, 18));
        actions.put("plus", new Action(ActionType.REDUCE, 18));
        actions.put("minus", new Action(ActionType.REDUCE, 18));
        actions.put("mult", new Action(ActionType.REDUCE, 18));
        actions.put("div", new Action(ActionType.REDUCE, 18));
        actions.put("eq", new Action(ActionType.REDUCE, 18));
        actions.put(">", new Action(ActionType.REDUCE, 18));
        actions.put("and", new Action(ActionType.REDUCE, 18));
        actions.put("or", new Action(ActionType.REDUCE, 18));
        actionTable.put(53, actions);
    }
    
    private void buildState46() {
        // After ( UNOP TERM ) - now reduce
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 41)); // TERM -> ( UNOP TERM )
        actions.put("}", new Action(ActionType.REDUCE, 41));
        actions.put(")", new Action(ActionType.REDUCE, 41));
        actions.put("{", new Action(ActionType.REDUCE, 41));
        actions.put("$", new Action(ActionType.REDUCE, 41));
        actions.put("plus", new Action(ActionType.REDUCE, 41));
        actions.put("minus", new Action(ActionType.REDUCE, 41));
        actions.put("mult", new Action(ActionType.REDUCE, 41));
        actions.put("div", new Action(ActionType.REDUCE, 41));
        actions.put("eq", new Action(ActionType.REDUCE, 41));
        actions.put(">", new Action(ActionType.REDUCE, 41));
        actions.put("and", new Action(ActionType.REDUCE, 41));
        actions.put("or", new Action(ActionType.REDUCE, 41));
        actionTable.put(46, actions);
    }
    
    // Control flow states
    private void buildState20() {
        // After while
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(20, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 21);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(20, gotos);
    }
    
    private void buildState21() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 22));
        actionTable.put(21, actions);
    }
    
    private void buildState22() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 11));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(22, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 23);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotoTable.put(22, gotos);
    }
    
    private void buildState23() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 24));
        actionTable.put(23, actions);
    }
    
    private void buildState24() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 30)); // LOOP -> while TERM { ALGO }
        actions.put("}", new Action(ActionType.REDUCE, 30));
        actionTable.put(24, actions);
    }
    
    private void buildState25() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 26));
        actionTable.put(25, actions);
    }
    
    private void buildState26() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 11));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(26, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 27);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotoTable.put(26, gotos);
    }
    
    private void buildState27() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 28));
        actionTable.put(27, actions);
    }
    
    private void buildState28() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("until", new Action(ActionType.SHIFT, 29));
        actionTable.put(28, actions);
    }
    
    private void buildState29() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(29, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 30);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(29, gotos);
    }
    
    private void buildState30() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 31)); // LOOP -> do { ALGO } until TERM
        actions.put("}", new Action(ActionType.REDUCE, 31));
        actionTable.put(30, actions);
    }
    
    private void buildState31() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(31, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 32);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(31, gotos);
    }
    
    private void buildState32() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 33));
        actionTable.put(32, actions);
    }
    
    private void buildState33() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 11));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(33, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 34);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotoTable.put(33, gotos);
    }
    
    private void buildState34() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 35));
        actionTable.put(34, actions);
    }
    
    private void buildState35() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("else", new Action(ActionType.SHIFT, 36));
        actions.put(";", new Action(ActionType.REDUCE, 32)); // BRANCH -> if TERM { ALGO }
        actions.put("}", new Action(ActionType.REDUCE, 32));
        actionTable.put(35, actions);
    }
    
    private void buildState36() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 37));
        actionTable.put(36, actions);
    }
    
    private void buildState37() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 11));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(37, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 38);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotoTable.put(37, gotos);
    }
    
    private void buildState38() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 39));
        actionTable.put(38, actions);
    }
    
    private void buildState39() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 33)); // BRANCH -> if TERM { ALGO } else { ALGO }
        actions.put("}", new Action(ActionType.REDUCE, 33));
        actionTable.put(39, actions);
    }
    
    public Action getAction(int state, String terminal) {
        if (!actionTable.containsKey(state)) {
            return new Action(ActionType.ERROR, -1);
        }
        
        Map<String, Action> stateActions = actionTable.get(state);
        if (!stateActions.containsKey(terminal)) {
            return new Action(ActionType.ERROR, -1);
        }
        
        return stateActions.get(terminal);
    }
    
    public int getGoto(int state, String nonTerminal) {
        if (!gotoTable.containsKey(state)) {
            return -1;
        }
        
        Map<String, Integer> stateGotos = gotoTable.get(state);
        if (!stateGotos.containsKey(nonTerminal)) {
            return -1;
        }
        
        return stateGotos.get(nonTerminal);
    }
}
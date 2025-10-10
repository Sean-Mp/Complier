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
        buildState1();
        buildState2();
        buildState3();
        buildState6();
        buildState7();
        buildState8();
        buildState10();
        buildState11();
        buildState12();
        buildState13();
        buildState14();
        buildState15();
        buildState16();
        buildState17();
        buildState18();
        buildState19();
        buildState20();
        buildState21();
        buildState22();
        buildState23();
        buildState24();
        buildState25();
        buildState26();
        buildState27();
        buildState28();
        buildState29();
        buildState30();
        buildState31();
        buildState32();
        buildState33();
        buildState34();
        buildState35();
        buildState36();
        buildState37();
        buildState38();
        buildState39();
        buildState40();
        buildState41();
        buildState42();
        buildState43();
        buildState44();
        buildState45();
        buildState46();
        buildState50();
        buildState51();
        buildState52();
        buildState53();
        buildState80();
        buildState81();
        buildState82();
        buildState83();
        buildState84();
        buildState85();
        buildState86();
        buildState90();
        buildState91();
        buildState92();
        buildState93();
        buildState94();
        buildState95();
        buildState96();
        buildState97();
        buildState98();
        buildState99();
        buildState100();
        buildState101();
        buildState102();
        buildState103();
        buildState104();
        buildState105();
        buildState106();
        buildState107();
        buildState108();
        buildState109();
        buildState110();
        buildState111();
        buildState112();
        buildState113();
        buildState114();
        buildState115();
        buildState116();
        buildState117();
        buildState118();
        buildState119();
        buildState120();
        buildState121();
        buildState122();
        buildState123();
        buildState124();
        buildState125();
        buildState126();
        buildState127();
        buildState128();
        buildState129();
        buildState130();
        buildState131();
        buildState132();
        buildState133();
        buildState134();
        buildState135();
        buildState136();
    }
    
    private void buildState0() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("glob", new Action(ActionType.SHIFT, 90));
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(0, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("SPL_PROG", 134);
        gotos.put("ALGO", 10);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotos.put("TERM", 14);
        gotoTable.put(0, gotos);
    }
    
    private void buildState1() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 22));
        actions.put("$", new Action(ActionType.REDUCE, 22));
        actions.put("}", new Action(ActionType.REDUCE, 22));
        actionTable.put(1, actions);
    }
    
    private void buildState2() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 50));
        actions.put("string", new Action(ActionType.SHIFT, 51));
        actions.put("number", new Action(ActionType.SHIFT, 50));
        actionTable.put(2, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("OUTPUT", 6);
        gotos.put("ATOM", 50);
        gotos.put("VAR", 50);
        gotoTable.put(2, gotos);
    }
    
    private void buildState3() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.SHIFT, 7));
        actions.put("}", new Action(ActionType.REDUCE, 20));
        actions.put("$", new Action(ActionType.REDUCE, 20));
        actionTable.put(3, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 8);
        gotos.put("TERM", 14);
        gotoTable.put(3, gotos);
    }
    
    private void buildState6() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 23));
        actions.put("}", new Action(ActionType.REDUCE, 23));
        actions.put("$", new Action(ActionType.REDUCE, 23));
        actionTable.put(6, actions);
    }
    
    private void buildState7() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actions.put("return", new Action(ActionType.REDUCE, 20)); // ADD THIS - empty ALGO before return
        actionTable.put(7, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 8);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotos.put("TERM", 14);
        gotoTable.put(7, gotos);
    }

    private void buildState8() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.REDUCE, 21));
        actions.put("$", new Action(ActionType.REDUCE, 21));
        actions.put(";", new Action(ActionType.REDUCE, 21));
        actions.put("return", new Action(ActionType.SHIFT, 121)); // ADD THIS - shift to return state
        actionTable.put(8, actions);
    }
    
    private void buildState10() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("$", new Action(ActionType.ACCEPT, 0));
        actions.put("}", new Action(ActionType.REDUCE, 20));
        actions.put(";", new Action(ActionType.REDUCE, 20));
        actionTable.put(10, actions);
    }
    
    private void buildState11() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("=", new Action(ActionType.SHIFT, 12));
        actions.put("(", new Action(ActionType.SHIFT, 40));
        actionTable.put(11, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ASSIGN", 3);
        gotoTable.put(11, gotos);
    }
    
    private void buildState12() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 81));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(12, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 14);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(12, gotos);
    }
    
    private void buildState13() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 19));
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
        actions.put("$", new Action(ActionType.REDUCE, 19));
        actions.put("{", new Action(ActionType.REDUCE, 19));
        actionTable.put(13, actions);
    }
    
    private void buildState14() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 54));
        actions.put("}", new Action(ActionType.REDUCE, 54));
        actions.put("$", new Action(ActionType.REDUCE, 54));
        actionTable.put(14, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 14);
        gotoTable.put(14, gotos);
    }
    
    private void buildState15() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("neg", new Action(ActionType.SHIFT, 16));
        actions.put("not", new Action(ActionType.SHIFT, 16));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(15, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("UNOP", 16);
        gotos.put("TERM", 17);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(15, gotos);
    }
    
    private void buildState16() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 52));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(16, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 44);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotoTable.put(16, gotos);
    }
    
    private void buildState17() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 44));
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
        gotos.put("TERM", 19);
        gotoTable.put(17, gotos);
    }
    
    private void buildState18() {
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
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 45));
        actions.put("and", new Action(ActionType.SHIFT, 18));
        actions.put("or", new Action(ActionType.SHIFT, 18));
        actions.put("plus", new Action(ActionType.SHIFT, 18));
        actions.put("minus", new Action(ActionType.SHIFT, 18));
        actions.put("mult", new Action(ActionType.SHIFT, 18));
        actions.put("div", new Action(ActionType.SHIFT, 18));
        actions.put("eq", new Action(ActionType.SHIFT, 18));
        actions.put(">", new Action(ActionType.SHIFT, 18));
        actionTable.put(19, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 45);
        gotoTable.put(19, gotos);
    }
    
    private void buildState20() {
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
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
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
        gotos.put("TERM", 14);
        gotoTable.put(22, gotos);
    }
    
    private void buildState23() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 24));
        actionTable.put(23, actions);
    }
    
    private void buildState24() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 30));
        actions.put("}", new Action(ActionType.REDUCE, 30));
        actions.put("$", new Action(ActionType.REDUCE, 30));
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
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
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
        gotos.put("TERM", 14);
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
        actions.put(";", new Action(ActionType.REDUCE, 31));
        actions.put("}", new Action(ActionType.REDUCE, 31));
        actions.put("$", new Action(ActionType.REDUCE, 31));
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
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
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
        gotos.put("TERM", 14);
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
        actions.put(";", new Action(ActionType.REDUCE, 32));
        actions.put("}", new Action(ActionType.REDUCE, 32));
        actions.put("$", new Action(ActionType.REDUCE, 32));
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
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
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
        gotos.put("TERM", 14);
        gotoTable.put(37, gotos);
    }
    
    private void buildState38() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 39));
        actionTable.put(38, actions);
    }
    
    private void buildState39() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 33));
        actions.put("}", new Action(ActionType.REDUCE, 33));
        actions.put("$", new Action(ActionType.REDUCE, 33));
        actionTable.put(39, actions);
    }
        
    private void buildState40() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 50));
        actions.put("number", new Action(ActionType.SHIFT, 50));
        actions.put(")", new Action(ActionType.REDUCE, 36));
        actionTable.put(40, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("INPUT", 41);
        gotos.put("ATOM", 50);
        gotos.put("VAR", 50);
        // CRITICAL FIX: Add BODY goto
        gotos.put("BODY", 115);
        gotoTable.put(40, gotos);
    }
    
    private void buildState41() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 42));
        actionTable.put(41, actions);
    }
    
    private void buildState42() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 55));
        actions.put("}", new Action(ActionType.REDUCE, 55));
        actions.put("$", new Action(ActionType.REDUCE, 55));
        actions.put("{", new Action(ActionType.SHIFT, 105));
        actionTable.put(42, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("BODY", 115);
        gotoTable.put(42, gotos);
    }
    
    private void buildState43() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.REDUCE, 40));
        actions.put("plus", new Action(ActionType.REDUCE, 40));
        actions.put("minus", new Action(ActionType.REDUCE, 40));
        actions.put("mult", new Action(ActionType.REDUCE, 40));
        actions.put("div", new Action(ActionType.REDUCE, 40));
        actions.put("eq", new Action(ActionType.REDUCE, 40));
        actions.put(">", new Action(ActionType.REDUCE, 40));
        actions.put("and", new Action(ActionType.REDUCE, 40));
        actions.put("or", new Action(ActionType.REDUCE, 40));
        actions.put(";", new Action(ActionType.REDUCE, 40));
        actions.put("}", new Action(ActionType.REDUCE, 40));
        actions.put("{", new Action(ActionType.REDUCE, 40));
        actions.put("$", new Action(ActionType.REDUCE, 40));
        actionTable.put(43, actions);
    }
    
    private void buildState44() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 46));
        actions.put("and", new Action(ActionType.SHIFT, 18));
        actions.put("or", new Action(ActionType.SHIFT, 18));
        actions.put("plus", new Action(ActionType.SHIFT, 18));
        actions.put("minus", new Action(ActionType.SHIFT, 18));
        actions.put("mult", new Action(ActionType.SHIFT, 18));
        actions.put("div", new Action(ActionType.SHIFT, 18));
        actions.put("eq", new Action(ActionType.SHIFT, 18));
        actions.put(">", new Action(ActionType.SHIFT, 18));
        actionTable.put(44, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 46);
        gotoTable.put(44, gotos);
    }
    
    private void buildState45() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 42));
        actions.put("}", new Action(ActionType.REDUCE, 42));
        actions.put(")", new Action(ActionType.REDUCE, 42));
        actions.put("{", new Action(ActionType.REDUCE, 42));
        actions.put("$", new Action(ActionType.REDUCE, 42));
        actions.put("plus", new Action(ActionType.REDUCE, 42));
        actions.put("minus", new Action(ActionType.REDUCE, 42));
        actions.put("mult", new Action(ActionType.REDUCE, 42));
        actions.put("div", new Action(ActionType.REDUCE, 42));
        actions.put("eq", new Action(ActionType.REDUCE, 42));
        actions.put(">", new Action(ActionType.REDUCE, 42));
        actions.put("and", new Action(ActionType.REDUCE, 42));
        actions.put("or", new Action(ActionType.REDUCE, 42));
        actionTable.put(45, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 45);
        gotoTable.put(45, gotos);
    }
    
    private void buildState46() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 41));
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
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 46);
        gotoTable.put(46, gotos);
    }
    
    private void buildState50() {
        Map<String, Action> actions = new HashMap<>();
        // OUTPUT context
        actions.put(";", new Action(ActionType.REDUCE, 34));
        actions.put("}", new Action(ActionType.REDUCE, 34));
        actions.put("$", new Action(ActionType.REDUCE, 34));
        // INPUT context - continue reading parameters
        actions.put(")", new Action(ActionType.REDUCE, 37));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 50));
        actions.put("number", new Action(ActionType.SHIFT, 50));
        actionTable.put(50, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("INPUT", 41);
        gotos.put("ATOM", 50);
        gotos.put("VAR", 50);
        // CRITICAL FIX: Add BODY and OUTPUT gotos for state 50
        gotos.put("BODY", 115);
        gotos.put("OUTPUT", 6);
        gotoTable.put(50, gotos);
    }
    
    private void buildState51() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 35));
        actions.put("}", new Action(ActionType.REDUCE, 35));
        actions.put("$", new Action(ActionType.REDUCE, 35));
        actionTable.put(51, actions);
    }
    
    private void buildState52() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 3));
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
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 18));
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
    
    private void buildState80() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("=", new Action(ActionType.SHIFT, 81));
        actions.put("(", new Action(ActionType.SHIFT, 40));
        actionTable.put(80, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ASSIGN", 3);
        gotos.put("INSTR", 3);
        gotos.put("ALGO", 10);
        gotos.put("TERM", 14);
        gotoTable.put(80, gotos);
    }
    
    private void buildState81() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 82));
        actions.put("number", new Action(ActionType.SHIFT, 13));
        actions.put("(", new Action(ActionType.SHIFT, 15));
        actionTable.put(81, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("TERM", 14);
        gotos.put("ATOM", 43);
        gotos.put("VAR", 53);
        gotos.put("ASSIGN", 3);
        gotos.put("ALGO", 10);
        gotoTable.put(81, gotos);
    }
    
    private void buildState82() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("(", new Action(ActionType.SHIFT, 83));
        actions.put(";", new Action(ActionType.REDUCE, 3));
        actions.put("}", new Action(ActionType.REDUCE, 3));
        actions.put("plus", new Action(ActionType.REDUCE, 3));
        actions.put("minus", new Action(ActionType.REDUCE, 3));
        actions.put("mult", new Action(ActionType.REDUCE, 3));
        actions.put("div", new Action(ActionType.REDUCE, 3));
        actions.put("eq", new Action(ActionType.REDUCE, 3));
        actions.put(">", new Action(ActionType.REDUCE, 3));
        actions.put("and", new Action(ActionType.REDUCE, 3));
        actions.put("or", new Action(ActionType.REDUCE, 3));
        actions.put(")", new Action(ActionType.REDUCE, 3));
        actionTable.put(82, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ASSIGN", 3);
        gotos.put("ALGO", 10);
        gotos.put("TERM", 14);
        gotoTable.put(82, gotos);
    }
    
    private void buildState83() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 86));
        actions.put("number", new Action(ActionType.SHIFT, 86));
        actions.put(")", new Action(ActionType.REDUCE, 36));
        actionTable.put(83, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("INPUT", 84);
        gotos.put("ATOM", 86);
        gotos.put("VAR", 86);
        gotos.put("ALGO", 10);
        gotos.put("TERM", 14);
        gotoTable.put(83, gotos);
    }
    
    private void buildState84() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 85));
        actionTable.put(84, actions);
    }
    
    private void buildState85() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.REDUCE, 53));
        actions.put("}", new Action(ActionType.REDUCE, 53));
        actions.put("$", new Action(ActionType.REDUCE, 53));
        actionTable.put(85, actions);
    }
    
    private void buildState86() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.REDUCE, 37));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 86));
        actions.put("number", new Action(ActionType.SHIFT, 86));
        actionTable.put(86, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("INPUT", 84);
        gotos.put("ATOM", 86);
        gotos.put("VAR", 86);
        gotoTable.put(86, gotos);
    }

    private void buildState90() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 91));
        actionTable.put(90, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VARIABLES", 93);
        gotoTable.put(90, gotos);
    }

    private void buildState91() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 92));
        actions.put("}", new Action(ActionType.REDUCE, 1));
        actionTable.put(91, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VARIABLES", 93);
        gotos.put("VAR", 92);
        gotoTable.put(91, gotos);
    }

    private void buildState92() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 92));
        actions.put("}", new Action(ActionType.REDUCE, 2));
        actionTable.put(92, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VARIABLES", 93);
        gotos.put("VAR", 92);
        gotoTable.put(92, gotos);
    }

    private void buildState93() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 94));
        actionTable.put(93, actions);
    }

    private void buildState94() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("proc", new Action(ActionType.SHIFT, 95));
        actionTable.put(94, actions);
    }

    private void buildState95() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 96));
        actionTable.put(95, actions);
    }

    private void buildState96() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 97));
        actions.put("}", new Action(ActionType.REDUCE, 5));
        actionTable.put(96, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("PROCDEFS", 107);
        gotos.put("PDEF", 97);
        gotos.put("NAME", 97);
        gotoTable.put(96, gotos);
    }

    private void buildState97() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("(", new Action(ActionType.SHIFT, 98));
        actionTable.put(97, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("PARAM", 100);
        gotos.put("BODY", 115);
        gotoTable.put(97, gotos);
    }

    private void buildState98() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 99));
        actions.put(")", new Action(ActionType.REDUCE, 12));
        actionTable.put(98, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("PARAM", 100);
        gotos.put("MAXTHREE", 99);
        gotos.put("VAR", 99);
        gotoTable.put(98, gotos);
    }

    private void buildState99() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 101));
        actions.put(")", new Action(ActionType.REDUCE, 13));
        actionTable.put(99, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("MAXTHREE", 102);
        gotos.put("PARAM", 100);
        gotos.put("VAR", 101);
        gotoTable.put(99, gotos);
    }

    private void buildState100() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 103));
        actionTable.put(100, actions);
    }

    private void buildState101() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 104));
        actions.put(")", new Action(ActionType.REDUCE, 14));
        actionTable.put(101, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VAR", 104);
        gotoTable.put(101, gotos);
    }

    private void buildState102() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 103));
        actionTable.put(102, actions);
    }

    private void buildState103() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 105));
        actionTable.put(103, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("BODY", 115);
        gotoTable.put(103, gotos);
    }

    private void buildState104() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.REDUCE, 15));
        actionTable.put(104, actions);
    }

    private void buildState105() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("local", new Action(ActionType.SHIFT, 106));
        actionTable.put(105, actions);
    }

    private void buildState106() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 108));
        actionTable.put(106, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("MAXTHREE", 111);
        gotoTable.put(106, gotos);
    }

    private void buildState107() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 109));
        actionTable.put(107, actions);
    }

    private void buildState108() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 110));
        actions.put("}", new Action(ActionType.REDUCE, 13)); // CHANGE: Use production 13 (MAXTHREE -> epsilon) not 12
        actionTable.put(108, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("MAXTHREE", 111);
        gotos.put("VAR", 110);
        gotoTable.put(108, gotos);
    }

    private void buildState109() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("func", new Action(ActionType.SHIFT, 112));
        actionTable.put(109, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("FUNCDEFS", 124);
        gotoTable.put(109, gotos);
    }

    private void buildState110() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 110));
        actions.put("}", new Action(ActionType.REDUCE, 13));
        actionTable.put(110, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("MAXTHREE", 111);
        gotos.put("VAR", 110);
        gotoTable.put(110, gotos);
    }

    private void buildState111() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 113));
        actionTable.put(111, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 115);
        gotoTable.put(111, gotos);
    }

    private void buildState112() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 114));
        actionTable.put(112, actions);
    }

    private void buildState113() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actions.put("}", new Action(ActionType.REDUCE, 20));
        actions.put(";", new Action(ActionType.SHIFT, 7));
        // When we see "return", we need to insert an empty ALGO and semicolon
        // This should trigger building the BODY with empty ALGO
        actions.put("return", new Action(ActionType.REDUCE, 20)); // Reduce empty to ALGO
        actionTable.put(113, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 115);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotos.put("BODY", 115);
        gotos.put("TERM", 14);
        gotos.put("PROCDEFS", 107);
        gotoTable.put(113, gotos);
    }

    private void buildState114() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 116));
        actions.put("}", new Action(ActionType.REDUCE, 9));
        actionTable.put(114, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("FUNCDEFS", 124);
        gotos.put("FDEF", 116);
        gotos.put("NAME", 116);
        gotoTable.put(114, gotos);
    }

    private void buildState115() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 117));  // For procedures
        actions.put(";", new Action(ActionType.SHIFT, 120));  // For functions with ALGO
        // When return appears after empty ALGO reduction, treat it like ; return
        actions.put("return", new Action(ActionType.SHIFT, 120)); // Go to state expecting return
        actionTable.put(115, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("BODY", 117);
        gotoTable.put(115, gotos);
    }

    private void buildState116() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("(", new Action(ActionType.SHIFT, 98));
        actionTable.put(116, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("PARAM", 118);
        gotos.put("BODY", 135);
        gotoTable.put(116, gotos);
    }

    private void buildState117() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 97));
        actions.put("}", new Action(ActionType.REDUCE, 6));
        actionTable.put(117, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("PROCDEFS", 107);
        gotos.put("PDEF", 97);
        gotos.put("NAME", 97);
        gotoTable.put(117, gotos);
    }

    private void buildState118() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(")", new Action(ActionType.SHIFT, 103));
        actionTable.put(118, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("BODY", 135);
        gotoTable.put(118, gotos);
    }

    private void buildState119() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.SHIFT, 120));
        actionTable.put(119, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 119);
        gotoTable.put(119, gotos);
    }

    private void buildState120() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("return", new Action(ActionType.SHIFT, 121));
        actionTable.put(120, actions);
    }

    private void buildState121() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 122));
        actions.put("number", new Action(ActionType.SHIFT, 122));
        actionTable.put(121, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ATOM", 122);
        gotos.put("VAR", 122);
        gotoTable.put(121, gotos);
    }

    private void buildState122() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 136));
        actionTable.put(122, actions);
    }

    private void buildState123() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 116));
        actions.put("}", new Action(ActionType.REDUCE, 8));
        actionTable.put(123, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("FUNCDEFS", 124);
        gotos.put("FDEF", 116);
        gotos.put("NAME", 116);
        gotoTable.put(123, gotos);
    }

    private void buildState124() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 125));
        actionTable.put(124, actions);
    }

    private void buildState125() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("main", new Action(ActionType.SHIFT, 126));
        actionTable.put(125, actions);
    }

    private void buildState126() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 127));
        actionTable.put(126, actions);
    }

    private void buildState127() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("var", new Action(ActionType.SHIFT, 128));
        actionTable.put(127, actions);
    }

    private void buildState128() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("{", new Action(ActionType.SHIFT, 129));
        actionTable.put(128, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VARIABLES", 131);
        gotoTable.put(128, gotos);
    }

    private void buildState129() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 130));
        actions.put("}", new Action(ActionType.REDUCE, 1));
        actionTable.put(129, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VARIABLES", 131);
        gotos.put("VAR", 130);
        gotoTable.put(129, gotos);
    }

    private void buildState130() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 130));
        actions.put("}", new Action(ActionType.REDUCE, 2));
        actionTable.put(130, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("VARIABLES", 131);
        gotos.put("VAR", 130);
        gotoTable.put(130, gotos);
    }

    private void buildState131() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 132));
        actionTable.put(131, actions);
    }

    private void buildState132() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("halt", new Action(ActionType.SHIFT, 1));
        actions.put("print", new Action(ActionType.SHIFT, 2));
        actions.put("user-defined-name", new Action(ActionType.SHIFT, 80));
        actions.put("while", new Action(ActionType.SHIFT, 20));
        actions.put("do", new Action(ActionType.SHIFT, 25));
        actions.put("if", new Action(ActionType.SHIFT, 31));
        actionTable.put(132, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 133);
        gotos.put("INSTR", 3);
        gotos.put("ASSIGN", 3);
        gotos.put("LOOP", 3);
        gotos.put("BRANCH", 3);
        gotos.put("TERM", 14);
        gotoTable.put(132, gotos);
    }

    private void buildState133() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 134));
        actionTable.put(133, actions);
    }

    private void buildState134() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("$", new Action(ActionType.ACCEPT, 0));
        actionTable.put(134, actions);
    }

    private void buildState135() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(";", new Action(ActionType.SHIFT, 120));
        actions.put("}", new Action(ActionType.REDUCE, 20));
        actions.put("return", new Action(ActionType.REDUCE, 11)); // ADD THIS
        actionTable.put(135, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("ALGO", 135);
        gotos.put("BODY", 136);
        gotoTable.put(135, gotos);
    }

    private void buildState136() {
        Map<String, Action> actions = new HashMap<>();
        actions.put("}", new Action(ActionType.SHIFT, 123));
        actionTable.put(136, actions);
        
        Map<String, Integer> gotos = new HashMap<>();
        gotos.put("FDEF", 123);
        gotoTable.put(136, gotos);
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
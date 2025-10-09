package Parser;

import java.util.*;

public class ParseTable {
    
    public enum ActionType {
        SHIFT, REDUCE, ACCEPT, ERROR
    }
    
    public static class Action {
        public ActionType type;
        public int value; // state for SHIFT, production for REDUCE
        
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
    
    // ACTION table: [state][terminal] -> Action
    private Map<Integer, Map<String, Action>> actionTable;
    
    // GOTO table: [state][non-terminal] -> state
    private Map<Integer, Map<String, Integer>> gotoTable;
    
    public ParseTable() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
        buildTables();
    }
    
    private void buildTables() {
        // Minimal table just to test the ALGO conflict
        // This is NOT a complete parser - just demonstrates the conflict resolution
        
        // State 0: Start state, expecting keywords
        Map<String, Action> state0 = new HashMap<>();
        state0.put("halt", new Action(ActionType.SHIFT, 1));
        state0.put("print", new Action(ActionType.SHIFT, 2));
        actionTable.put(0, state0);
        
        Map<String, Integer> goto0 = new HashMap<>();
        goto0.put("ALGO", 10);
        goto0.put("INSTR", 3);
        gotoTable.put(0, goto0);
        
        // State 1: After halt
        Map<String, Action> state1 = new HashMap<>();
        state1.put(";", new Action(ActionType.REDUCE, 22)); // INSTR -> halt
        state1.put("$", new Action(ActionType.REDUCE, 22));
        state1.put("}", new Action(ActionType.REDUCE, 22));
        actionTable.put(1, state1);
        
        // State 2: After print, expecting OUTPUT
        Map<String, Action> state2 = new HashMap<>();
        state2.put("user-defined-name", new Action(ActionType.SHIFT, 4));
        state2.put("string", new Action(ActionType.SHIFT, 5));
        actionTable.put(2, state2);
        
        Map<String, Integer> goto2 = new HashMap<>();
        goto2.put("OUTPUT", 6);
        gotoTable.put(2, goto2);
        
        // State 3: After INSTR (CONFLICT STATE!)
        Map<String, Action> state3 = new HashMap<>();
        // CRITICAL: Conflict resolution - always SHIFT on ;
        state3.put(";", new Action(ActionType.SHIFT, 7)); // SHIFT wins!
        state3.put("}", new Action(ActionType.REDUCE, 20)); // ALGO -> INSTR
        state3.put("$", new Action(ActionType.REDUCE, 20));
        actionTable.put(3, state3);
        
        // State 4: After variable in OUTPUT
        Map<String, Action> state4 = new HashMap<>();
        state4.put(";", new Action(ActionType.REDUCE, 34)); // OUTPUT -> ATOM
        state4.put("}", new Action(ActionType.REDUCE, 34));
        actionTable.put(4, state4);
        
        // State 5: After string in OUTPUT
        Map<String, Action> state5 = new HashMap<>();
        state5.put(";", new Action(ActionType.REDUCE, 35)); // OUTPUT -> string
        state5.put("}", new Action(ActionType.REDUCE, 35));
        actionTable.put(5, state5);
        
        // State 6: After OUTPUT in print statement
        Map<String, Action> state6 = new HashMap<>();
        state6.put(";", new Action(ActionType.REDUCE, 23)); // INSTR -> print OUTPUT
        state6.put("}", new Action(ActionType.REDUCE, 23));
        state6.put("$", new Action(ActionType.REDUCE, 23));
        actionTable.put(6, state6);
        
        // State 7: After INSTR ;
        Map<String, Action> state7 = new HashMap<>();
        state7.put("halt", new Action(ActionType.SHIFT, 1));
        state7.put("print", new Action(ActionType.SHIFT, 2));
        actionTable.put(7, state7);
        
        Map<String, Integer> goto7 = new HashMap<>();
        goto7.put("ALGO", 8);
        goto7.put("INSTR", 3);
        gotoTable.put(7, goto7);
        
        // State 8: After INSTR ; ALGO
        Map<String, Action> state8 = new HashMap<>();
        state8.put("}", new Action(ActionType.REDUCE, 21)); // ALGO -> INSTR ; ALGO
        state8.put("$", new Action(ActionType.REDUCE, 21));
        actionTable.put(8, state8);
        
        // State 10: ALGO complete
        Map<String, Action> state10 = new HashMap<>();
        state10.put("$", new Action(ActionType.ACCEPT, 0));
        actionTable.put(10, state10);
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
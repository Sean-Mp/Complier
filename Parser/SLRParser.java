package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import Lexer.State;
import Lexer.Token;

public class SLRParser {
    private List<Token> tokens;
    private int pos;
    private Stack<Integer> stateStack;
    private Stack<String> symbolStack;
    private Map<String, Map<String, Action>> actionTable;
    private Map<String, Map<String, Integer>> gotoTable;
    private List<Production> productions;

    public SLRParser()
    {
        this.stateStack = new Stack<>();
        this.symbolStack = new Stack<>();
        this.actionTable = new HashMap<>();
        this.gotoTable = new HashMap<>();

        //build table
        buildParsingRules();
    }
    

    public boolean parse(List<Token> tokens)
    {
        this.tokens = new ArrayList<>(tokens);
        this.tokens.add(new Token("$", State.SYMBOL));
        this.pos = 0;

        stateStack.clear();
        symbolStack.clear();
        stateStack.push(0);

        System.out.println("\n=== PARSING ===");

        while(true){
            int state = stateStack.peek();
            Token curr = this.tokens.get(pos);
            String terminal = getTerminalString(curr);

            System.out.printf("State: %d, Current: %s, Stack: %s%n", state, terminal, symbolStack);

            Map<String, Action> actions = actionTable.get(String.valueOf(state));
            
            if(actions == null || !actions.containsKey(terminal))
            {
                System.err.println("ERROR: No action found " + state + " with terminal " + terminal);
                System.err.println("Actions available: " + (actions != null ? actions.keySet() : "none"));
                return false;
            }

            Action action = actions.get(terminal);

            //Shift
            if(action.type.equals("shift"))
            {
                stateStack.push(action.state);
                symbolStack.push(terminal);
                pos++;
                System.out.println(" -> Shift to state " + action.state);
            }
            //Reduce
            else if(action.type.equals("reduce"))
            {
                Production prod = productions.get(action.state);
                System.out.println(" -> Reduce by: " + prod);

                //Pop off stack
                for(int i = 0; i < prod.rhsSize; i++)
                {
                    if(!stateStack.isEmpty())
                    {
                        stateStack.pop();
                    }

                    if(!symbolStack.isEmpty()){
                        symbolStack.pop();
                    }
                }

                //Push non-terminal
                symbolStack.push(prod.lhs);

                //Goto
                int gotoState = stateStack.peek();
                Map<String, Integer> gotos = gotoTable.get(String.valueOf(gotoState));

                if(gotos == null || !gotos.containsKey(prod.lhs))
                {
                    System.err.println("ERROR: No goto for state " + gotoState + " with non terminal " + prod.lhs);
                    return false;
                }

                stateStack.push(gotos.get(prod.lhs));
            }
            else if(action.type.equals("accept"))
            {
                System.out.println(" -> ACCEPT");
                System.out.println("===========\n");
                return true;
            }
        }
    }
    private String getTerminalString(Token token)
    {
        if(token.getValue().equals("$")){
            return "$";
        }

        State state = token.getState();
        String val = token.getValue();

        switch (state) {
            case KEYWORD:
                return val; 
            case OPERATOR:
                return val;
            case SYMBOL:
                return val;
            case VARIABLE:
                return "user-defined name";
            case NUMBER:
                return "number";
            case STRING:
                return "string";
            default:
                return val;
        }
    }
    private void buildParsingRules()
    {
        productions = new ArrayList<>();

        //New start
        productions.add(new Production("S'", 1, "SPL_PROG"));

        //SPL_PROG rules
        productions.add(new Production("SPL_PROG", 16, "glob { VARIABLES } proc { PROCDEFS } func { FUNCDEFS } main { MAINPROG }"));

        //VARIABLES rules
        productions.add(new Production("VARIABLES", 0, "ε"));
        productions.add(new Production("VARIABLES", 2, "VAR VARIABLES"));

        //PROCDEFS rules
        productions.add(new Production("PROCDEFS", 0, "ε"));
        productions.add(new Production("PROCDEFS", 2, "PDEF PROCDEFS"));

        //FUNCDEFS rules
        productions.add(new Production("FUNCDEFS", 0, "ε"));
        productions.add(new Production("FUNCDEFS", 2, "FDEF FUNCDEFS"));

        //PDEF rules
        productions.add(new Production("PDEF", 7, "NAME ( PARAMS ) { BODY }"));

        //FDEF rules
        productions.add(new Production("FDEF", 10, "NAME ( PARAMS ) { BODY ; return ATOM }"));

        //BODY rules
        productions.add(new Production("BODY", 5, "local { MAXTHREE } ALGO"));

        //MAXTHREE rules
        productions.add(new Production("MAXTHREE", 0, "ε"));
        productions.add(new Production("MAXTHREE", 1, "VAR"));
        productions.add(new Production("MAXTHREE", 2, "VAR VAR"));
        productions.add(new Production("MAXTHREE", 3, "VAR VAR VAR"));

        //MAINPROG rules
        productions.add(new Production("MAINPROG", 5, "var { VARIABLES } ALGO"));

        //ALGO rules
        productions.add(new Production("ALGO", 1, "INSTR"));
        productions.add(new Production("ALGO", 3, "INSTR ; ALGO"));

        //INSTR rules 
        productions.add(new Production("INSTR", 1, "halt"));
        productions.add(new Production("INSTR", 2, "print OUTPUT"));
        productions.add(new Production("INSTR", 4, "NAME ( INPUT )"));
        productions.add(new Production("INSTR", 1, "ASSIGN"));
        productions.add(new Production("INSTR", 1, "LOOP"));
        productions.add(new Production("INSTR", 1, "BRANCH"));

        //ASSIGN rules
        productions.add(new Production("ASSIGN", 6, "VAR = NAME ( INPUT )"));
        productions.add(new Production("ASSIGN", 3, "VAR = EXPR"));

        //OUTPUT rules 
        productions.add(new Production("OUTPUT",1, "ATOM"));
        productions.add(new Production("OUTPUT",1, "string"));

        //INPUT rules
        productions.add(new Production("INPUT", 0, "ε"));
        productions.add(new Production("INPUT", 1, "ATOM"));
        productions.add(new Production("INPUT", 2, "ATOM ATOM"));
        productions.add(new Production("INPUT", 3, "ATOM ATOM ATOM"));

        //ATOM rules
        productions.add(new Production("ATOM", 1, "VAR"));
        productions.add(new Production("ATOM", 1, "number"));

        //VAR and NAME rules
        productions.add(new Production("VAR", 1, "user-defined-name"));
        productions.add(new Production("NAME", 1, "user-defined-name"));

        //TERM rules
        productions.add(new Production("TERM", 1, "ATOM"));
        productions.add(new Production("TERM", 4, "( UNOP TERM )"));
        productions.add(new Production("TERM", 5, "( TERM BINOP TERM )"));

        //UNOP rules
        productions.add(new Production("UNOP", 1, "neg"));
        productions.add(new Production("UNOP", 1, "not"));

        //BINOP rules
        productions.add(new Production("BINOP", 1, "eq"));
        productions.add(new Production("BINOP", 1, ">"));
        productions.add(new Production("BINOP", 1, "or"));
        productions.add(new Production("BINOP", 1, "and"));
        productions.add(new Production("BINOP", 1, "plus"));
        productions.add(new Production("BINOP", 1, "minus"));
        productions.add(new Production("BINOP", 1, "mult"));
        productions.add(new Production("BINOP", 1, "div"));

        //LOOP rules
        productions.add(new Production("LOOP", 5, "while TERM { ALGO }"));
        productions.add(new Production("LOOP", 6, "do { ALGO } until TERM"));

        //BRANCH rules
        productions.add(new Production("BRANCH", 5, "if TERM { ALGO }"));
        productions.add(new Production("BRANCH ", 9, "if TERM { ALGO } else { ALGO }"));
    }

    /*
     * Adds an entry to the action table
     * @param state Current state
     * @param terminal terminal symbol that triggers that action
     * @param type type of the parser action (shift, reduce, accept)
     * @param next next state (for shift) or production number to reduce by
     */
    private void addAction(String state, String terminal, String type, int next)
    {
        actionTable.computeIfAbsent(state, k -> new HashMap<>()).put(terminal, new Action(type, next));
    }

    /*
     * Adds an entry to the goto table
     * GOTO table defines the next state to transition for a given non-terminal 
     * @param state current state
     * @param nonTerminal non-terminal symbol
     * @param next next state number to transition to
     */
    private void addGoto(String state, String nonTerminal, int next)
    {
        gotoTable.computeIfAbsent(state, k -> new HashMap<>()).put(nonTerminal, next);
    }

    // * represents the dot in the items
    private void buildTable()
    {
        //State 0: start
        addAction("0", "glob", "shift", 1);
        addGoto("0", "SPL_PROG", 100); //100 is accept state

        //State 1: glob * { VARIABLES } proc ...
        addAction("1", "{", "shift", 2);

        //State 2: glob { * VARIABLES } proc ...
        addAction("2", "user-defined-name", "shift", 3); //VAR
        addAction("2", "}", "reduce", 2); //VARIABLES -> ε
        addGoto("2", "VARIABLES", 4);
        addGoto("2", "VAR",5);

        //State 3: VAR -> user-defined-name *
        addAction("3", "user-defined-name", "reduce", 4);
        addAction("3", "}", "reduce", 4);

        //State 4: VARIABLES * } proc ...
        addAction("4", "}", "shift", 6);

        //State 5: glob { VARIABLES } * proc ...
        addAction("5", "proc","shift", 6);

        //State 6: glob { VARIABLES } proc * { PROCDEFS } func ...
        addAction("6", "{", "shift", 7);

        //State 7: glob { VARIABLES } proc { * PROCDEFS } func ...
        addAction("7", "}", "reduce", 6); //PROCDEFS -> ε
        addAction("7", "user-defined-name", "shift", 8);   //NAME -> user-defined-name
        addGoto("7", "PROCDEFS", 9);
        addGoto("7", "PDEF", 10);

        //State 8: PDEF -> NAME * ( PARAMS ) { BODY }
        addAction("8", "(", "shift", 11);

        //State 9: PDEF * PROCDEFS
        addAction("9", "user-defined-name", "shift", 8);
        addAction("9", "}", "reduce", 6);
        addGoto("9", "PDEF", 9);
        addGoto("9", "PROCDEFS", 10);

        //State 10: glob { VARIABLES } proc { PROCDEFS * } func ...
        addAction("10", "}", "shift", 12);

        //State 11: PDEF -> NAME ( * PARAM ) { BODY }
        addAction("11", "user-defined-name", "shift", 13); //VAR
        addAction("11", ")", "reduce", 13); //PARAMS -> ε
        addGoto("11", "PARAM", 14);
        addGoto("11", "MAXTHREE", 15);
        addGoto("11", "VAR", 16);

        //State 12: glob { VARIABLES } proc { PROCDEFS } * func ...
        addAction("12", "func", "shift", 17);

        //State 13: MAXTHREE -> VAR * VAR VAR
        addAction("13", "user-defined-name", "shift", 18);
        addAction("13", ")", "reduce", 15); //MAXTHREE -> VAR
        addGoto("13", "VAR", 16);

        //State 14: PDEF -> NAME ( PARAM  * ) { BODY }
        addAction("14", ")", "shift", 19);

        //State 15: PARAM -> MAXTHREE *
        addAction("15", ")", "reduce", 13); 

        //State 16: VAR -> user-defined-name *
        addAction("16", "user-defined-name", "reduce", 4);
        addAction("16", ")", "reduce", 4);

                
     }
}

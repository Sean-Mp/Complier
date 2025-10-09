package Parser;

public class Grammar {
    
    // Production rules (numbered for reference in parsing table)
    public static final String[] PRODUCTIONS = {
        "SPL_PROG -> glob { VARIABLES } proc { PROCDEFS } func { FUNCDEFS } main { MAINPROG }",
        "VARIABLES -> ε",
        "VARIABLES -> VAR VARIABLES",
        "VAR -> user-defined-name",
        "NAME -> user-defined-name",
        "PROCDEFS -> ε",
        "PROCDEFS -> PDEF PROCDEFS",
        "PDEF -> NAME ( PARAM ) { BODY }",
        "FDEF -> NAME ( PARAM ) { BODY ; return ATOM }",
        "FUNCDEFS -> FDEF FUNCDEFS",
        "FUNCDEFS -> ε",
        "BODY -> local { MAXTHREE } ALGO",
        "PARAM -> MAXTHREE",
        "MAXTHREE -> ε",
        "MAXTHREE -> VAR",
        "MAXTHREE -> VAR VAR",
        "MAXTHREE -> VAR VAR VAR",
        "MAINPROG -> var { VARIABLES } ALGO",
        "ATOM -> VAR",
        "ATOM -> number",
        "ALGO -> INSTR",              // Production causing conflict
        "ALGO -> INSTR ; ALGO",       // Production causing conflict
        "INSTR -> halt",
        "INSTR -> print OUTPUT",
        "INSTR -> NAME ( INPUT )",
        "INSTR -> ASSIGN",
        "INSTR -> LOOP",
        "INSTR -> BRANCH",
        "ASSIGN -> VAR = NAME ( INPUT )",
        "ASSIGN -> VAR = TERM",
        "LOOP -> while TERM { ALGO }",
        "LOOP -> do { ALGO } until TERM",
        "BRANCH -> if TERM { ALGO }",
        "BRANCH -> if TERM { ALGO } else { ALGO }",
        "OUTPUT -> ATOM",
        "OUTPUT -> string",
        "INPUT -> ε",
        "INPUT -> ATOM",
        "INPUT -> ATOM ATOM",
        "INPUT -> ATOM ATOM ATOM",
        "TERM -> ATOM",
        "TERM -> ( UNOP TERM )",
        "TERM -> ( TERM BINOP TERM )",
        "UNOP -> neg",
        "UNOP -> not",
        "BINOP -> eq",
        "BINOP -> >",
        "BINOP -> or",
        "BINOP -> and",
        "BINOP -> plus",
        "BINOP -> minus",
        "BINOP -> mult",
        "BINOP -> div"
    };
    
    // Get left-hand side of production
    public static String getLHS(int productionNum) {
        return PRODUCTIONS[productionNum].split(" -> ")[0];
    }
    
    // Get right-hand side of production
    public static String[] getRHS(int productionNum) {
        String rhs = PRODUCTIONS[productionNum].split(" -> ")[1];
        if (rhs.equals("ε")) {
            return new String[0]; // epsilon production
        }
        return rhs.split(" ");
    }
    
    // Get production length (for stack popping during reduce)
    public static int getProductionLength(int productionNum) {
        return getRHS(productionNum).length;
    }
}
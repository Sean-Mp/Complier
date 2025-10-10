package Parser;

public class Grammar {
    
    // Production rules (numbered for reference in parsing table)
    public static final String[] PRODUCTIONS = {
        // Main structure
        "SPL_PROG -> glob { VARIABLES } proc { PROCDEFS } func { FUNCDEFS } main { MAINPROG }",
        
        // Variables
        "VARIABLES -> epsilon",
        "VARIABLES -> VAR VARIABLES",
        "VAR -> user-defined-name",
        "NAME -> user-defined-name",
        
        // Procedures
        "PROCDEFS -> epsilon",
        "PROCDEFS -> PDEF PROCDEFS",
        "PDEF -> NAME ( PARAM ) { BODY }",
        
        // Functions
        "FDEF -> NAME ( PARAM ) { BODY ; return ATOM }",
        "FUNCDEFS -> FDEF FUNCDEFS",
        "FUNCDEFS -> epsilon",
        
        // Body and parameters
        "BODY -> local { MAXTHREE } ALGO",
        "PARAM -> MAXTHREE",
        "MAXTHREE -> epsilon",
        "MAXTHREE -> VAR",
        "MAXTHREE -> VAR VAR",
        "MAXTHREE -> VAR VAR VAR",
        
        // Main program
        "MAINPROG -> var { VARIABLES } ALGO",
        
        // Atoms
        "ATOM -> VAR",
        "ATOM -> number",
        
        // Algorithm (CRITICAL: These cause shift-reduce conflict - always SHIFT on ;)
        "ALGO -> INSTR",              // Production 20
        "ALGO -> INSTR ; ALGO",       // Production 21
        
        // Instructions
        "INSTR -> halt",                          // Production 22
        "INSTR -> print OUTPUT",                  // Production 23
        "INSTR -> NAME ( INPUT )",                // Production 24 - procedure call (abstract)
        "INSTR -> ASSIGN",                        // Production 25
        "INSTR -> LOOP",                          // Production 26
        "INSTR -> BRANCH",                        // Production 27
        
        // Assignments (abstract)
        "ASSIGN -> VAR = NAME ( INPUT )",         // Production 28 - function call assignment
        "ASSIGN -> VAR = TERM",                   // Production 29 - expression assignment
        
        // Loops
        "LOOP -> while TERM { ALGO }",            // Production 30
        "LOOP -> do { ALGO } until TERM",         // Production 31
        
        // Branches
        "BRANCH -> if TERM { ALGO }",             // Production 32
        "BRANCH -> if TERM { ALGO } else { ALGO }", // Production 33
        
        // Output
        "OUTPUT -> ATOM",                         // Production 34
        "OUTPUT -> string",                       // Production 35
        
        // Input (parameters for procedure/function calls)
        "INPUT -> epsilon",                       // Production 36
        "INPUT -> ATOM",                          // Production 37
        "INPUT -> ATOM ATOM",                     // Production 38
        "INPUT -> ATOM ATOM ATOM",                // Production 39
        
        // Terms
        "TERM -> ATOM",                           // Production 40
        "TERM -> ( UNOP TERM )",                  // Production 41
        "TERM -> ( TERM BINOP TERM )",            // Production 42
        
        // Unary operators
        "UNOP -> neg",                            // Production 43
        "UNOP -> not",                            // Production 44
        
        // Binary operators
        "BINOP -> eq",                            // Production 45
        "BINOP -> >",                             // Production 46
        "BINOP -> or",                            // Production 47
        "BINOP -> and",                           // Production 48
        "BINOP -> plus",                          // Production 49
        "BINOP -> minus",                         // Production 50
        "BINOP -> mult",                          // Production 51
        "BINOP -> div",                           // Production 52
        
        // BRIDGE PRODUCTIONS - These handle terminals directly to avoid GOTO mismatches
        "ASSIGN -> user-defined-name = user-defined-name ( INPUT )", // Production 53
        "ASSIGN -> user-defined-name = TERM",                        // Production 54
        "INSTR -> user-defined-name ( INPUT )"                       // Production 55
    };
    
    // Get left-hand side of production
    public static String getLHS(int productionNum) {
        return PRODUCTIONS[productionNum].split(" -> ")[0];
    }
    
    // Get right-hand side of production
    public static String[] getRHS(int productionNum) {
        String rhs = PRODUCTIONS[productionNum].split(" -> ")[1];
        if (rhs.equals("epsilon")) {
            return new String[0]; // epsilon production
        }
        return rhs.split(" ");
    }
    
    // Get production length (for stack popping during reduce)
    public static int getProductionLength(int productionNum) {
        return getRHS(productionNum).length;
    }
}
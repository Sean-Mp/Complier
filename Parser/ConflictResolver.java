package Parser;

import java.util.*;

/**
 * Handles shift-reduce conflict resolution for the SPL grammar
 */
public class ConflictResolver {
    
    /**
     * Resolves shift-reduce conflict in ALGO productions
     * 
     * The conflict occurs at:
     *   ALGO ::= INSTR •
     *   ALGO ::= INSTR • ; ALGO
     * 
     * With lookahead ';', the parser doesn't know whether to:
     *   - REDUCE (using ALGO ::= INSTR)
     *   - SHIFT the semicolon
     * 
     * Resolution: Always SHIFT when encountering ';' after INSTR in ALGO context
     */
    
    public enum Action {
        SHIFT,
        REDUCE,
        ERROR
    }
    
    public static class ConflictInfo {
        public String production;
        public String lookahead;
        public Action shiftAction;
        public Action reduceAction;
        
        public ConflictInfo(String production, String lookahead, 
                           Action shiftAction, Action reduceAction) {
            this.production = production;
            this.lookahead = lookahead;
            this.shiftAction = shiftAction;
            this.reduceAction = reduceAction;
        }
        
        @Override
        public String toString() {
            return String.format("Conflict in %s with lookahead '%s': SHIFT vs REDUCE", 
                               production, lookahead);
        }
    }
    
    /**
     * Identifies if the current state has a shift-reduce conflict
     */
    public static boolean hasConflict(String nonTerminal, String lookahead, 
                                     List<String> items) {
        if (!nonTerminal.equals("ALGO")) {
            return false;
        }
        
        // Check if we have both:
        // 1. A complete item that could reduce (ALGO ::= INSTR •)
        // 2. An item expecting semicolon (ALGO ::= INSTR • ; ALGO)
        
        boolean canReduce = items.contains("ALGO ::= INSTR •");
        boolean canShift = items.contains("ALGO ::= INSTR • ; ALGO");
        
        return canReduce && canShift && lookahead.equals(";");
    }
    
    /**
     * Resolves the conflict by choosing SHIFT
     * 
     * Rationale:
     * - SHIFT allows building longer instruction sequences
     * - Matches programmer's intent: continue parsing the algorithm
     * - Reduces only when no more instructions follow
     */
    public static Action resolveConflict(ConflictInfo conflict) {
        if (conflict.production.equals("ALGO ::= INSTR") && 
            conflict.lookahead.equals(";")) {
            System.out.println("Conflict detected: " + conflict);
            System.out.println("Resolution: SHIFT (to build complete instruction sequence)");
            return Action.SHIFT;
        }
        
        // For other conflicts, default behavior
        return Action.ERROR;
    }
    
    /**
     * Returns a detailed explanation of the conflict
     */
    public static String explainConflict() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SHIFT-REDUCE CONFLICT IN SPL GRAMMAR ===\n\n");
        sb.append("Location: ALGO productions\n");
        sb.append("  ALGO ::= INSTR\n");
        sb.append("  ALGO ::= INSTR ; ALGO\n\n");
        
        sb.append("Conflict State:\n");
        sb.append("  After parsing: INSTR\n");
        sb.append("  Lookahead: ';'\n\n");
        
        sb.append("Parser's Dilemma:\n");
        sb.append("  Option 1 (REDUCE): Complete ALGO now (INSTR → ALGO)\n");
        sb.append("  Option 2 (SHIFT):  Continue to build (INSTR ; ALGO)\n\n");
        
        sb.append("Example Causing Conflict:\n");
        sb.append("  halt ; print x ; halt\n");
        sb.append("         ^\n");
        sb.append("         conflict here after parsing 'halt'\n\n");
        
        sb.append("Resolution:\n");
        sb.append("  Always SHIFT when lookahead is ';'\n");
        sb.append("  Reason: Build the longest possible instruction sequence\n");
        sb.append("  This matches the natural semantics of sequential instructions\n\n");
        
        return sb.toString();
    }
    
    /**
     * Example usage and testing
     */
    public static void demonstrateConflict() {
        System.out.println(explainConflict());
        
        // Simulate conflict detection
        List<String> items = Arrays.asList(
            "ALGO ::= INSTR •",
            "ALGO ::= INSTR • ; ALGO"
        );
        
        if (hasConflict("ALGO", ";", items)) {
            ConflictInfo conflict = new ConflictInfo(
                "ALGO ::= INSTR", 
                ";", 
                Action.SHIFT, 
                Action.REDUCE
            );
            
            Action resolution = resolveConflict(conflict);
            System.out.println("\nFinal Decision: " + resolution);
        }
    }
}
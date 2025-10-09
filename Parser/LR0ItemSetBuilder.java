package Parser;

import java.util.*;

public class LR0ItemSetBuilder {
    
    // An LR(0) item: production number + dot position
    static class Item {
        int production;
        int dotPos;
        
        Item(int production, int dotPos) {
            this.production = production;
            this.dotPos = dotPos;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Item)) return false;
            Item other = (Item) o;
            return production == other.production && dotPos == other.dotPos;
        }
        
        @Override
        public int hashCode() {
            return production * 100 + dotPos;
        }
        
        @Override
        public String toString() {
            String[] rhs = Grammar.getRHS(production);
            StringBuilder sb = new StringBuilder();
            sb.append(Grammar.getLHS(production)).append(" -> ");
            for (int i = 0; i <= rhs.length; i++) {
                if (i == dotPos) sb.append("• ");
                if (i < rhs.length) sb.append(rhs[i]).append(" ");
            }
            return sb.toString();
        }
    }
    
    // Compute closure of item set
    public static Set<Item> closure(Set<Item> items) {
        Set<Item> result = new HashSet<>(items);
        boolean added = true;
        
        while (added) {
            added = false;
            Set<Item> toAdd = new HashSet<>();
            
            for (Item item : result) {
                String[] rhs = Grammar.getRHS(item.production);
                if (item.dotPos < rhs.length) {
                    String nextSymbol = rhs[item.dotPos];
                    // If next symbol is non-terminal, add all productions for it
                    if (isNonTerminal(nextSymbol)) {
                        for (int i = 0; i < Grammar.PRODUCTIONS.length; i++) {
                            if (Grammar.getLHS(i).equals(nextSymbol)) {
                                Item newItem = new Item(i, 0);
                                if (!result.contains(newItem)) {
                                    toAdd.add(newItem);
                                    added = true;
                                }
                            }
                        }
                    }
                }
            }
            result.addAll(toAdd);
        }
        
        return result;
    }
    
    private static boolean isNonTerminal(String symbol) {
        return Character.isUpperCase(symbol.charAt(0));
    }
    
    public static void printItemSet(Set<Item> items, int stateNum) {
        System.out.println("\nState " + stateNum + ":");
        for (Item item : items) {
            System.out.println("  " + item);
        }
    }
    
    public static void main(String[] args) {
        // Start with initial item: S' -> • SPL_PROG
        Set<Item> initial = new HashSet<>();
        initial.add(new Item(0, 0)); // Assuming production 0 is start
        
        Set<Item> closure = closure(initial);
        printItemSet(closure, 0);
        
        System.out.println("\nYou need to:");
        System.out.println("1. Compute all item sets using goto function");
        System.out.println("2. Build ACTION and GOTO tables");
        System.out.println("3. Apply conflict resolution for ALGO productions");
    }
}
import symboltable.*;

public class TypeChecker {
    private SymbolTable symbolTable;
    private boolean hasErrors = false;
    private int line = 0;
    private int column = 0;

    public TypeChecker(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    private void reportError(String message) {
        System.err.println("Type Error: " + message);
        hasErrors = true;
    }

    // Type checking methods that return the type of expressions
    public String checkAtomType(Object atom) {
        if (atom instanceof Integer) {
            return "numeric";
        } else if (atom instanceof String) {
            String name = (String) atom;
            Symbol symbol = symbolTable.lookup(name);
            if (symbol != null) {
                return "numeric"; // All variables are numeric in SPL
            } else {
                reportError("Variable '" + name + "' not declared");
                return "error";
            }
        }
        return "error";
    }

    public String checkUnopType(String unop, String termType) {
        if (unop.equals("neg")) {
            if (!termType.equals("numeric")) {
                reportError("'neg' operator requires numeric operand");
                return "error";
            }
            return "numeric";
        } else if (unop.equals("not")) {
            if (!termType.equals("boolean")) {
                reportError("'not' operator requires boolean operand");
                return "error";
            }
            return "boolean";
        }
        return "error";
    }

    public String checkBinopType(String binop) {
        switch (binop) {
            case "eq":
            case ">":
                return "comparison";
            case "or":
            case "and":
                return "boolean";
            case "plus":
            case "minus":
            case "mult":
            case "div":
                return "numeric";
            default:
                return "error";
        }
    }

    public String checkTermBinop(String binopType, String leftType, String rightType, String binop) {
        if (binopType.equals("numeric")) {
            // Arithmetic operators: both operands must be numeric
            if (!leftType.equals("numeric") || !rightType.equals("numeric")) {
                reportError("Arithmetic operator '" + binop + "' requires numeric operands");
                return "error";
            }
            return "numeric";
        } else if (binopType.equals("boolean")) {
            // Boolean operators: both operands must be boolean
            if (!leftType.equals("boolean") || !rightType.equals("boolean")) {
                reportError("Boolean operator '" + binop + "' requires boolean operands");
                return "error";
            }
            return "boolean";
        } else if (binopType.equals("comparison")) {
            // Comparison operators: both operands must be numeric, result is boolean
            if (!leftType.equals("numeric") || !rightType.equals("numeric")) {
                reportError("Comparison operator '" + binop + "' requires numeric operands");
                return "error";
            }
            return "boolean";
        }
        return "error";
    }

    public boolean checkAssignment(String varType, String termType) {
        if (!varType.equals("numeric") || !termType.equals("numeric")) {
            reportError("Assignment requires both sides to be numeric");
            return false;
        }
        return true;
    }

    public boolean checkLoop(String termType) {
        if (!termType.equals("boolean")) {
            reportError("Loop condition must be boolean");
            return false;
        }
        return true;
    }

    public boolean checkBranch(String termType) {
        if (!termType.equals("boolean")) {
            reportError("Branch condition must be boolean");
            return false;
        }
        return true;
    }

    public boolean checkOutput(String outputType) {
        // OUTPUT can be string (always correct) or ATOM (must be numeric)
        if (outputType.equals("string")) {
            return true;
        }
        if (!outputType.equals("numeric")) {
            reportError("Print output must be numeric or string");
            return false;
        }
        return true;
    }

    public boolean checkFunctionName(String name) {
        Symbol symbol = symbolTable.lookup(name);
        if (symbol != null && symbol.getType() != null) {
            reportError("Function/Procedure name '" + name + "' must be type-less (has no type in Symbol Table)");
            return false;
        }
        return true;
    }

    public boolean checkInput(String inputType) {
        // INPUT is correctly typed if all ATOMs are numeric (or empty)
        if (inputType == null || inputType.equals("empty")) {
            return true;
        }
        if (!inputType.equals("numeric")) {
            reportError("Input parameters must be numeric");
            return false;
        }
        return true;
    }
}
import java.util.*;

// Visitor interface
interface TypeVisitor {
    void visit(ProgramNode node);
    void visit(ProcedureNode node);
    void visit(FunctionNode node);
    void visit(MainProgramNode node);
    void visit(AtomNode node);
    void visit(UnopNode node);
    void visit(BinopNode node);
    void visit(AssignmentNode node);
    void visit(LoopNode node);
    void visit(BranchNode node);
    void visit(PrintNode node);
    void visit(ProcedureCallNode node);
    void visit(FunctionCallNode node);
    void visit(HaltNode node);
    void visit(ReturnNode node);
    void visit(BlockNode node);
}

// Base class
abstract class ASTNode {
    private int line;
    private int column;
    
    public ASTNode(int line, int column) {
        this.line = line;
        this.column = column;
    }
    
    public abstract void accept(TypeVisitor visitor);
    
    public int getLine() { return line; }
    public int getColumn() { return column; }
}

// Program Structure
class ProgramNode extends ASTNode {
    private List<String> globalVars;
    private List<ProcedureNode> procedures;
    private List<FunctionNode> functions;
    private MainProgramNode mainProgram;
    
    public ProgramNode(List<String> globalVars, List<ProcedureNode> procedures, 
                      List<FunctionNode> functions, MainProgramNode mainProgram, 
                      int line, int column) {
        super(line, column);
        this.globalVars = globalVars;
        this.procedures = procedures;
        this.functions = functions;
        this.mainProgram = mainProgram;
    }
    
    public List<String> getGlobalVars() { return globalVars; }
    public List<ProcedureNode> getProcedures() { return procedures; }
    public List<FunctionNode> getFunctions() { return functions; }
    public MainProgramNode getMainProgram() { return mainProgram; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class ProcedureNode extends ASTNode {
    private String name;
    private List<String> parameters;
    private List<String> localVars;
    private List<ASTNode> algorithm;
    
    public ProcedureNode(String name, List<String> parameters, List<String> localVars, 
                        List<ASTNode> algorithm, int line, int column) {
        super(line, column);
        this.name = name;
        this.parameters = parameters;
        this.localVars = localVars;
        this.algorithm = algorithm;
    }
    
    public String getName() { return name; }
    public List<String> getParameters() { return parameters; }
    public List<String> getLocalVars() { return localVars; }
    public List<ASTNode> getAlgorithm() { return algorithm; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class FunctionNode extends ASTNode {
    private String name;
    private List<String> parameters;
    private List<String> localVars;
    private List<ASTNode> algorithm;
    private ASTNode returnValue;
    
    public FunctionNode(String name, List<String> parameters, List<String> localVars, 
                       List<ASTNode> algorithm, ASTNode returnValue, int line, int column) {
        super(line, column);
        this.name = name;
        this.parameters = parameters;
        this.localVars = localVars;
        this.algorithm = algorithm;
        this.returnValue = returnValue;
    }
    
    public String getName() { return name; }
    public List<String> getParameters() { return parameters; }
    public List<String> getLocalVars() { return localVars; }
    public List<ASTNode> getAlgorithm() { return algorithm; }
    public ASTNode getReturnValue() { return returnValue; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class MainProgramNode extends ASTNode {
    private List<String> variables;
    private List<ASTNode> algorithm;
    
    public MainProgramNode(List<String> variables, List<ASTNode> algorithm, int line, int column) {
        super(line, column);
        this.variables = variables;
        this.algorithm = algorithm;
    }
    
    public List<String> getVariables() { return variables; }
    public List<ASTNode> getAlgorithm() { return algorithm; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

// Expressions
class AtomNode extends ASTNode {
    private Object value;
    private String type;
    
    public AtomNode(Object value, int line, int column) {
        super(line, column);
        this.value = value;
    }
    
    public Object getValue() { return value; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class UnopNode extends ASTNode {
    private String operator;
    private ASTNode operand;
    private String type;
    
    public UnopNode(String operator, ASTNode operand, int line, int column) {
        super(line, column);
        this.operator = operator;
        this.operand = operand;
    }
    
    public String getOperator() { return operator; }
    public ASTNode getOperand() { return operand; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class BinopNode extends ASTNode {
    private String operator;
    private ASTNode left;
    private ASTNode right;
    private String type;
    
    public BinopNode(String operator, ASTNode left, ASTNode right, int line, int column) {
        super(line, column);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
    
    public String getOperator() { return operator; }
    public ASTNode getLeft() { return left; }
    public ASTNode getRight() { return right; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

// Statements
class AssignmentNode extends ASTNode {
    private String variable;
    private ASTNode expression;
    private boolean isFunctionCall;
    
    public AssignmentNode(String variable, ASTNode expression, boolean isFunctionCall, int line, int column) {
        super(line, column);
        this.variable = variable;
        this.expression = expression;
        this.isFunctionCall = isFunctionCall;
    }
    
    public String getVariable() { return variable; }
    public ASTNode getExpression() { return expression; }
    public boolean isFunctionCall() { return isFunctionCall; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class LoopNode extends ASTNode {
    private String loopType;
    private ASTNode condition;
    private List<ASTNode> body;
    
    public LoopNode(String loopType, ASTNode condition, List<ASTNode> body, int line, int column) {
        super(line, column);
        this.loopType = loopType;
        this.condition = condition;
        this.body = body;
    }
    
    public String getLoopType() { return loopType; }
    public ASTNode getCondition() { return condition; }
    public List<ASTNode> getBody() { return body; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class BranchNode extends ASTNode {
    private ASTNode condition;
    private List<ASTNode> ifBody;
    private List<ASTNode> elseBody;
    
    public BranchNode(ASTNode condition, List<ASTNode> ifBody, List<ASTNode> elseBody, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }
    
    public ASTNode getCondition() { return condition; }
    public List<ASTNode> getIfBody() { return ifBody; }
    public List<ASTNode> getElseBody() { return elseBody; }
    public boolean hasElse() { return elseBody != null; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class PrintNode extends ASTNode {
    private Object output;
    private boolean isString;
    
    public PrintNode(Object output, boolean isString, int line, int column) {
        super(line, column);
        this.output = output;
        this.isString = isString;
    }
    
    public Object getOutput() { return output; }
    public boolean isString() { return isString; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class ProcedureCallNode extends ASTNode {
    private String procedureName;
    private List<ASTNode> arguments;
    
    public ProcedureCallNode(String procedureName, List<ASTNode> arguments, int line, int column) {
        super(line, column);
        this.procedureName = procedureName;
        this.arguments = arguments;
    }
    
    public String getProcedureName() { return procedureName; }
    public List<ASTNode> getArguments() { return arguments; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class FunctionCallNode extends ASTNode {
    private String functionName;
    private List<ASTNode> arguments;
    
    public FunctionCallNode(String functionName, List<ASTNode> arguments, int line, int column) {
        super(line, column);
        this.functionName = functionName;
        this.arguments = arguments;
    }
    
    public String getFunctionName() { return functionName; }
    public List<ASTNode> getArguments() { return arguments; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class HaltNode extends ASTNode {
    public HaltNode(int line, int column) {
        super(line, column);
    }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class ReturnNode extends ASTNode {
    private ASTNode returnValue;
    
    public ReturnNode(ASTNode returnValue, int line, int column) {
        super(line, column);
        this.returnValue = returnValue;
    }
    
    public ASTNode getReturnValue() { return returnValue; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}

class BlockNode extends ASTNode {
    private List<ASTNode> statements;
    
    public BlockNode(List<ASTNode> statements, int line, int column) {
        super(line, column);
        this.statements = statements;
    }
    
    public List<ASTNode> getStatements() { return statements; }
    
    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }
}
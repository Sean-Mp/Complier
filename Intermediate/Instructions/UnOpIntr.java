package Intermediate.Instructions;

public class UnOpIntr extends Instruction{
    String dest;
    String operator;
    String operand;

    UnOpIntr(String dest, String operator, String operand)
    {
        this.dest = dest;
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String toString()
    {
        return dest + " = " + operator + " " + operand; 
    }
}

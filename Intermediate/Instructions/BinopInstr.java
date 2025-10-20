package Intermediate.Instructions;

public class BinopInstr extends Instruction {
    String dest;
    String op1;
    String operator;
    String op2;

    BinopInstr(String dest, String op1, String operator, String op2)
    {
        this.dest = dest;
        this.op1 = op1;
        this.operator = operator;
        this.op2 = op2;
    }

    @Override
    public String toString()
    {
        return dest + " = " + op1 + " " + operator + " " + op2;
    }
}

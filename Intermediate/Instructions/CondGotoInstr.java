package Intermediate.Instructions;

public class CondGotoInstr extends Instruction{
    String condition;
    String thenLabel;
    String elseLabel;

    CondGotoInstr(String condition, String op, String thenLabel, String elseLabel)
    {
        this.condition = condition;
        this.thenLabel = thenLabel;
        this.elseLabel = elseLabel;
    }

    @Override
    public String toString()
    {
        return "IF " + condition + " THEN " + thenLabel + " ELSE " + elseLabel;
    }
}

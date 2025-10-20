package Intermediate.Instructions;

public class LabelInstr extends Instruction {
    String label;

    LabelInstr(String label)
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return "LABEL " + label;
    }
}

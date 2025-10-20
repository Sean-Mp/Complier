package Intermediate.Instructions;

public class GotoIntr extends Instruction{
    String label;

    GotoIntr(String label)
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return "GOTO " + label;
    }
}

package Intermediate.Instructions;

public class AssignInstr extends Instruction {
    String dest;
    String value;

    AssignInstr(String dest, String value)
    {
        this.dest = dest;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return dest + " = " + value;
    }
}

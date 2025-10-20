package Intermediate.Instructions;

import java.util.List;

public class AssignCallInstr extends Instruction{
    String dest;
    String fname;
    List<String> args;
    
    AssignCallInstr(String dest, String fname, List<String> args)
    {
        this.dest = dest;
        this.fname = fname;
        this.args = args;
    }

    @Override
    public String toString()
    {
        return dest + " = " + "CALL " + fname + "(" + String.join(", ", args) + ")";
    }
}

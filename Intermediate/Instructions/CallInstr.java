package Intermediate.Instructions;

import java.util.List;

public class CallInstr extends Instruction{
    String fname;
    List<String> args;

    CallInstr(String fname, List<String> args)
    {
        this.fname = fname;
        this.args = args;
    }

    @Override
    public String toString()
    {
        return "CALL " + fname + "(" + String.join(",", args) + ")";
    }
}

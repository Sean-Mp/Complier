package Intermediate.Instructions;

import java.util.List;

public class CodeGenResult {
    List<Instruction> code;
    String place;

    CodeGenResult(List<Instruction> code)
    {
        this.code = code;
        this.place = null;
    }
    CodeGenResult(List<Instruction> code, String place)
    {
        this.code = code;
        this.place = place;
    }
    
}

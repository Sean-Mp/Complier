package Parser;

public class Production {
    String lhs;
    int rhsSize;
    String description;

    Production(String lhs, int rhsSize, String des)
    {
        this.lhs = lhs;
        this.rhsSize = rhsSize;
        this.description = des;
    }
    public int getRhsSize()
    {
        return rhsSize;
    }

    @Override
    public String toString()
    {
        return lhs + " -> " + description;
    }
}

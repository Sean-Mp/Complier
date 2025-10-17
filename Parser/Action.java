package Parser;

//Action that the parser should take
public class Action {
    String type;
    int state; //shift: next state, reduce: production number

    Action(String type, int state){
        this.type = type;
        this.state = state;
    }
}

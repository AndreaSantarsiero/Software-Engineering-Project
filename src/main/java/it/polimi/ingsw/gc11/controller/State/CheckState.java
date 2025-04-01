package it.polimi.ingsw.gc11.controller.State;

public class CheckState implements GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new CheckState()); // Change to Check
    }

    @Override
    public String getStateName(){
        return "CHECK";
    }
}

package it.polimi.ingsw.gc11.controller.State;

public class IdleState implements GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new BuildingState()); // Change to Building
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}

package it.polimi.ingsw.gc11.controller.State;
//da cancellare
import it.polimi.ingsw.gc11.controller.GameContext;

public class IdleState extends GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new BuildingState()); // Change to Building
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }

    @Override
    public void startGame(GameContext context) {
        this.nextState(context);
    }
}

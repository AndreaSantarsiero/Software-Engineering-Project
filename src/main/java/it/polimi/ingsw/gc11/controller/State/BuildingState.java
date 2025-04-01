package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class BuildingState implements GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new CheckState()); // Change to Check
    }

    @Override
    public String getStateName(){
        return "BUILDING";
    }
}

package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class CheckState extends GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new AdventureState());
    }

    @Override
    public String getStateName(){
        return "CHECK";
    }
}

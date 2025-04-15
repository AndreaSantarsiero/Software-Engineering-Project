package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class AdventureState extends GamePhase {
    @Override
    public void nextState(GameContext context) {
        context.setState(new EndgameState());
    }

    @Override
    public String getStateName(){
        return "ADVENTURE";
    }
}

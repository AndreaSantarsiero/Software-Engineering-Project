package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class CheckPhase extends GamePhase {
    @Override
    public void nextPhase(GameContext context) {
        context.setPhase(new AdventurePhase());
    }

    @Override
    public String getPhaseName(){
        return "CHECK";
    }
}

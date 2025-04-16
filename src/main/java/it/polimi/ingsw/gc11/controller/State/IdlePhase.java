package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class IdlePhase extends GamePhase {
    @Override
    public void nextPhase(GameContext context) {
        context.setPhase(new BuildingPhase()); // Change to Building
    }

    @Override
    public String getPhaseName() {
        return "IDLE";
    }

    @Override
    public void startGame(GameContext context) {
        this.nextPhase(context);
    }
}

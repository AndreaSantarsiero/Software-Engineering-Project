package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class CheckPhase extends GamePhase {
    private GameContext gameContext;

    public CheckPhase(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void nextPhase() {
        gameContext.setPhase(new AdventurePhase(this.gameContext));
    }
}

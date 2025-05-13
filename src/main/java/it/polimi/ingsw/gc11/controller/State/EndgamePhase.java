package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class EndgamePhase extends GamePhase {
    GameContext gameContext;

    public EndgamePhase(GameContext gameContext) {
        this.gameContext = gameContext;
    }
}

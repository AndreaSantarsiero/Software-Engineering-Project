package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public interface GamePhase {
    void nextState(GameContext context);
    String getStateName();
}

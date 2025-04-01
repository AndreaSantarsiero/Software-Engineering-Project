package it.polimi.ingsw.gc11.controller.State;

public interface GamePhase {
    void nextState(GameContext context);
    String getStateName();
}

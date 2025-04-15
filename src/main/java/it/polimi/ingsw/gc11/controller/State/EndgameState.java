package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;

public class EndgameState extends GamePhase {
    @Override
    public void nextState(GameContext context) {

    }

    @Override
    public String getStateName(){
        return "ENDGAME";
    }
}

package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.PlayerContext;
import it.polimi.ingsw.gc11.model.GameModel;


import java.util.ArrayList;

public class GameContext {
    private GameModel gameModel;
    private GamePhase state;
    private ArrayList<PlayerContext> playerContexts;

    public GameContext() {
        this.gameModel = new GameModel();
        this.playerContexts = new ArrayList<>();
        // Initial state
        this.state = new IdleState();
    }
    public GameModel getGameModel() {
        return gameModel;
    }

    public void setState(GamePhase state) {
        this.state = state;
    }

    public void nextState() {
        state.nextState(this);
    }

    public GamePhase getState() {
        return state;
    }

}

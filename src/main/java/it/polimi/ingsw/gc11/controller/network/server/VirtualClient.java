package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.network.Utils;



public class VirtualClient {

    private GameContext gameContext;
    private Utils.ConnectionType type;



    public VirtualClient(Utils.ConnectionType type) {
        this.type = type;
    }


    public GameContext getGameContext() {
        return gameContext;
    }


    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }
}

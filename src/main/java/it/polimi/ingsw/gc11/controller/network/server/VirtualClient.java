package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.network.Utils;



public class VirtualClient {

    private GameContext gameContext;
    private Utils.ConnectionType type;



    public VirtualClient(GameContext gameContext, Utils.ConnectionType type) {
        this.gameContext = gameContext;
        this.type = type;
    }


    public GameContext getGameContext() {
        return gameContext;
    }
}

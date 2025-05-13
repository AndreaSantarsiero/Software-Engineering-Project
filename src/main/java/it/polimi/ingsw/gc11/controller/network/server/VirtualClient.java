package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;



public abstract class VirtualClient {

    private GameContext gameContext;



    public GameContext getGameContext() {
        return gameContext;
    }


    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }



    public abstract void sendAction(ServerAction action) throws NetworkException;
}

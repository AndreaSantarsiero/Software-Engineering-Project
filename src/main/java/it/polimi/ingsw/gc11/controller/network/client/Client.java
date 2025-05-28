package it.polimi.ingsw.gc11.controller.network.client;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.controller.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.*;



public abstract class Client {

    protected UUID clientSessionToken;
    protected VirtualServer virtualServer;



    public Client(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }



    public void setClientSessionToken(UUID clientSessionToken) {
        this.clientSessionToken = clientSessionToken;
    }

    public UUID getClientSessionToken() {
        return clientSessionToken;
    }



    //from client to server
    public abstract void registerSession(String username) throws NetworkException;

    public abstract void sendAction(ClientControllerAction action) throws NetworkException;

    public abstract void sendAction(ClientGameAction action) throws NetworkException;



    //from server to client
    public void sendAction(ServerAction action){
        virtualServer.receiveAction(action);
    }
}

package it.polimi.ingsw.gc11.controller.network.client;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;

import java.util.*;



public abstract class Client {

    protected UUID clientSessionToken;
    protected VirtualServer virtualServer;



    public Client(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }



    abstract public void registerSession(String username) throws NetworkException, UsernameAlreadyTakenException;

    abstract public void createMatch(String username, FlightBoard.Type flightType, int numPlayers) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException;

    abstract public void connectToGame(String username, String matchId) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException;

    abstract public Map<String, List<String>> getAvailableMatches(String username) throws NetworkException;

    abstract public List<Player> getPlayers(String username, UUID token, String matchID) throws NetworkException;



    abstract public void sendAction(ClientAction action) throws NetworkException;

    public void sendAction(ServerAction action){
        virtualServer.receiveAction(action);
    }
}

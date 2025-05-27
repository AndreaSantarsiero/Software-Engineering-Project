package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.controller.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;



public interface ServerInterface extends Remote {

    //creating, joining and handling matches
    UUID registerPlayerSession(String username, ClientInterface playerStub) throws RemoteException, UsernameAlreadyTakenException;
    void createMatch(String username, UUID token, FlightBoard.Type flightLevel, int numPlayers) throws RemoteException, FullLobbyException, UsernameAlreadyTakenException;
    void connectPlayerToGame(String username, UUID token, String matchId) throws RemoteException, FullLobbyException, NullPointerException, UsernameAlreadyTakenException;
    Map<String, List<String>> getAvailableMatches(String username, UUID token) throws RemoteException;
    Map<String, String> getPlayers(String username, UUID token) throws RemoteException;


    void sendAction(ClientGameAction action, UUID token) throws RemoteException;
}

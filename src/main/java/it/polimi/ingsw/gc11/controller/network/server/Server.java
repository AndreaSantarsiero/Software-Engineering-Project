package it.polimi.ingsw.gc11.controller.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerRMI;
import it.polimi.ingsw.gc11.controller.network.server.socket.ServerSocket;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;

import java.util.*;



/**
 * Abstract class representing a network server that mediates communication between clients and the core game logic
 * This class handles common game-related operations, while delegating connection-specific methods
 * to its subclasses (e.g., {@link ServerRMI}, {@link ServerSocket})
 */
public abstract class Server {

    protected final ServerController serverController;



    public Server(ServerController serverController) {
        this.serverController = serverController;
    }



    private GameContext getGameContext(String username, UUID token) {
        return serverController.getPlayerVirtualClient(username, token).getGameContext();
    }



    /**
     * Creates a new match and connects the player to it
     *
     * @param username    the player's username
     * @param token       the session token associated with the player
     * @param flightLevel the difficulty level of the flight board
     * @param numPlayers  the number of players in the match
     */
    public void createMatch(String username, UUID token, FlightBoard.Type flightLevel, int numPlayers) throws FullLobbyException, UsernameAlreadyTakenException {
        serverController.createMatch(flightLevel, numPlayers, username, token);
    }

    /**
     * Connects the player to an existing match
     *
     * @param username the player's username
     * @param token    the session token associated with the player
     * @param matchId  the identifier of the match to join
     */
    public void connectPlayerToGame(String username, UUID token, String matchId) throws FullLobbyException, NullPointerException, UsernameAlreadyTakenException {
        serverController.connectPlayerToGame(username, token, matchId);
    }

    /**
     * Retrieves a list of available match identifiers that the player can join
     *
     * @param username the player's username
     * @param token    the session token associated with the player
     * @return a map where each key is a match ID and the corresponding value is a list of player usernames
     * @throws RuntimeException if the session is invalid
     */
    public Map<String, List<String>> getAvailableMatches(String username, UUID token) {
        return serverController.getAvailableMatches(username, token);
    }

    public Map<String, String> getPlayers(String username, UUID token){
        return serverController.getPlayers(username, token);
    }


    public void sendAction(ClientAction action, UUID token) {
        serverController.receiveAction(action, token);
    }



    public abstract void shutdown() throws Exception;
}

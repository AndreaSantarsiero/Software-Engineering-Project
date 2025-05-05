package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.server.*;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerRMI;
import it.polimi.ingsw.gc11.controller.network.server.socket.ServerSocket;
import it.polimi.ingsw.gc11.model.FlightBoard;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;



/**
 * The {@code ServerController} class is the central authority for managing active player sessions
 * and available game matches. It handles player registration, session authentication, and match creation
 * <p>
 * This class supports concurrent access and is thread-safe
 */
public class ServerController {

    private final Map<String, ClientSession> playerSessions;
    private final Map<String, GameContext> availableMatches;
    private final ServerRMI serverRMI;
    private final ServerSocket serverSocket;



    /**
     * Constructs a new {@code ServerController} with empty session and match registries
     */
    public ServerController(int port) {
        this.playerSessions = new ConcurrentHashMap<>();
        this.availableMatches = new ConcurrentHashMap<>();
        this.serverRMI = new ServerRMI(this, port);
        this.serverSocket = new ServerSocket(this, port);
    }



    /**
     * Creates a new match with the specified flight difficulty level
     *
     * <p>This method initializes a new {@link GameContext}, sets the game level
     * based on the provided flight type, and stores the game instance in the
     * {@code availableMatches} map using a generated unique match ID
     *
     * @param flightLevel the difficulty level for the new match
     * @return the unique identifier of the newly created match
     */
    public String createMatch(FlightBoard.Type flightLevel) {
        GameContext match = new GameContext(flightLevel);
        availableMatches.put(match.getMatchID(), match);
        return match.getMatchID();
    }


    /**
     * Retrieves the {@link VirtualClient} associated with a given username and token
     *
     * <p>This method validates that the session exists and that the token matches the one
     * stored in the corresponding {@link ClientSession}. If the session is not found or the
     * token is invalid, a {@link RuntimeException} is thrown
     *
     * @param username the username associated with the client
     * @param token    the authentication token issued at login
     * @return the {@code VirtualClient} associated with the username
     * @throws RuntimeException if no valid session is found for the username and token
     */
    public VirtualClient getPlayerVirtualClient(String username, UUID token) {
        ClientSession session = playerSessions.get(username);

        if (session != null && session.checkToken(token)) {
            return session.getVirtualClient();
        }

        throw new RuntimeException("No session found for username " + username + " and token " + token);
    }


    /**
     * Registers a new player session for the specified username and match
     *
     * <p>If a session for the username already exists, the method throws an exception.
     * A new {@link ClientSession} is created and linked to the specified match context,
     * and the unique session token is returned (not exposed outside this class)
     *
     * @param username the username of the connecting player
     * @param matchID  the identifier of the match the player wants to join
     * @return the unique token associated with the new session
     * @throws RuntimeException if the match ID is invalid or a session already exists for the username
     */
    public UUID registerPlayerSession(String username, String matchID, Utils.ConnectionType type) {
        if (playerSessions.containsKey(username)) {
            throw new RuntimeException("A session already exists for username: " + username);
        }

        GameContext match = availableMatches.get(matchID);
        if (match == null) {
            throw new RuntimeException("No matches found for matchID " + matchID);
        }

        ClientSession clientSession = new ClientSession(username, new VirtualClient(match, type));
        playerSessions.put(username, clientSession);
        return clientSession.getToken();
    }



    //cosa facciamo se il player si disconnette?
    //cosa facciamo a fine game?



    public void shutdown() {
        try {
            if (serverRMI != null) {
                serverRMI.shutdown();
                System.out.println("Server RMI shut down successfully");
            }
        } catch (Exception e) {
            System.err.println("error during server RMI shut down: " + e.getMessage());
        }

        try {
            if (serverSocket != null) {
                serverSocket.shutdown();
                System.out.println("Server socket shut down successfully");
            }
        } catch (Exception e) {
            System.err.println("error during server socket shut down: " + e.getMessage());
        }
    }
}

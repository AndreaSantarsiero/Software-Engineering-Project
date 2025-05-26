package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.controller.network.server.*;
import it.polimi.ingsw.gc11.controller.network.server.rmi.*;
import it.polimi.ingsw.gc11.controller.network.server.socket.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;



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
    public ServerController(int RMIPort, int SocketPort) throws NetworkException, UsernameAlreadyTakenException {
        this.playerSessions = new ConcurrentHashMap<>();
        this.availableMatches = new ConcurrentHashMap<>();
        this.serverRMI = new ServerRMI(this, RMIPort);
        this.serverSocket = new ServerSocket(this, SocketPort);
    }



    /**
     * Retrieves the {@link ClientSession} associated with a given username and token
     *
     * <p>This method validates that the session exists and that the token matches the one
     * stored in the corresponding {@link ClientSession}. If the session is not found or the
     * token is invalid, a {@link RuntimeException} is thrown
     *
     * @param username the username associated with the client
     * @param token    the authentication token issued at login
     * @return the {@code ClientSession} associated with the username
     * @throws RuntimeException if no valid session is found for the username and token
     */
    private ClientSession getPlayerSession(String username, UUID token) {
        if (!playerSessions.containsKey(username)) {
            throw new RuntimeException("No session found for username " + username);
        }
        if (!playerSessions.get(username).checkToken(token)) {
            throw new RuntimeException("Invalid connection token for username " + username);
        }
        return playerSessions.get(username);
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
        return getPlayerSession(username, token).getVirtualClient();
    }


    /**
     * Registers a new player session for the specified username and connection type
     *
     * <p>This method creates a new {@link ClientSession} containing a {@link VirtualClient}
     * associated with the specified connection type. If a session already exists for the given
     * username, the method throws an exception
     *
     * @param username the username of the connecting player
     * @param playerStub the networking interface used by the client
     * @return the unique session token associated with the new session
     * @throws UsernameAlreadyTakenException if a session already exists for the username
     * @throws IllegalArgumentException if the username is null or empty
     */
    public UUID registerRMISession(String username, ClientInterface playerStub) throws UsernameAlreadyTakenException, IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is null or empty");
        }
        if (playerSessions.containsKey(username)) {
            throw new UsernameAlreadyTakenException("A session already exists for username: " + username);
        }

        ClientSession clientSession = new ClientSession(username, new VirtualRMIClient(playerStub));
        playerSessions.put(username, clientSession);
        return clientSession.getToken();
    }


    /**
     * Registers a new player session for the specified username and connection type
     *
     * <p>This method creates a new {@link ClientSession} containing a {@link VirtualClient}
     * associated with the specified connection type. If a session already exists for the given
     * username, the method throws an exception
     *
     * @param username the username of the connecting player
     * @return the unique session token associated with the new session
     * @throws UsernameAlreadyTakenException if a session already exists for the username
     * @throws IllegalArgumentException if the username is null or empty
     */
    public UUID registerSocketSession(String username) throws UsernameAlreadyTakenException, IllegalArgumentException {
        if (playerSessions.containsKey(username)) {
            throw new UsernameAlreadyTakenException("A session already exists for username: " + username);
        }
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is null or empty");
        }

        ClientSession clientSession = new ClientSession(username, new VirtualSocketClient());
        playerSessions.put(username, clientSession);
        return clientSession.getToken();
    }


    /**
     * Creates a new match with the specified flight difficulty level
     *
     * <p>This method initializes a new {@link GameContext}, sets the game level
     * based on the provided flight type, and stores the game instance in the
     * {@code availableMatches} map using a generated unique match ID
     *
     * @param flightLevel the difficulty level for the new match
     */
    public void createMatch(FlightBoard.Type flightLevel, int numPlayers, String username, UUID token) throws FullLobbyException, UsernameAlreadyTakenException {
        getPlayerSession(username, token);
        GameContext match = new GameContext(flightLevel, numPlayers, this);
        availableMatches.put(match.getMatchID(), match);
        connectPlayerToGame(username, token, match.getMatchID());
    }


    /**
     * Enrolls an existing player session to a specific game match
     *
     * <p>This method associates the player's {@link VirtualClient} with the specified game context,
     * identified by the given match ID. The session must already be registered via {@link #registerRMISession}
     * or via {@link #registerSocketSession}
     *
     * @param username the username of the player to enroll
     * @param matchID the identifier of the match the player wants to join
     * @throws RuntimeException if the match ID is invalid or the session is not found
     * @throws FullLobbyException if the player cannot join this match
     */
    public void connectPlayerToGame(String username, UUID token, String matchID) throws RuntimeException, FullLobbyException, UsernameAlreadyTakenException {
        ClientSession clientSession = getPlayerSession(username, token);
        GameContext match = availableMatches.get(matchID);
        if (match == null) {
            throw new RuntimeException("No matches found for matchID " + matchID);
        }

        match.connectPlayerToGame(username);
        clientSession.getVirtualClient().setGameContext(match);
    }


    /**
     * Retrieves a map of available matches that the player can join
     * </p>
     * Each entry in the map represents a match, where the key is the match ID (gameId) and the value is a list of usernames of the players currently connected to that match.
     *
     * @param username the player's username
     * @param token    the session token associated with the player
     * @return a map where each key is a match ID and the corresponding value is a list of player usernames
     * @throws RuntimeException if the session is invalid
     */

    public Map<String, List<String>> getAvailableMatches(String username, UUID token) {
        getPlayerSession(username, token);
        Map<String, List<String>> result = new HashMap<>();

        for (Map.Entry<String, GameContext> entry : availableMatches.entrySet()) {
            String gameId = entry.getKey();
            GameContext match = entry.getValue();
            List<String> usernames = match.getGameModel().getPlayers().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());
            result.put(gameId, usernames);
        }

        return result;
    }


    public List<Player> getPlayers(String username, UUID token){
        //Get matchId of the gameContext paired with the username provided
        String matchID = getPlayerSession(username, token).getVirtualClient().getGameContext().getMatchID();
        GameContext match = availableMatches.get(matchID);

        if (match == null) {
            throw new IllegalArgumentException("No matches found for matchID " + matchID);
        }

        return match.getGameModel().getPlayers();
    }



    //cosa facciamo se il player si disconnette? invalidare il token, freeze, riconnect ecc
    //cosa facciamo a fine game? bisogna levare username e token dalla mappa credo

    //possiamo mettere un timeout nella sessione di ogni utente, ogni volta che arriva il ping
    //resetto il timeout, e quando scade chiamo metodo freeze che invalida il token della sessione
    //e avvisa il GameContext che il player si è disconnesso (serve metodo)
    //poi servirà metodo unFreeze connection che rigenera un nuovo token e riavvisa il GameContext
    //(simile a registerSession, ma l'username deve essere già presente)
    //volendo, per avvisare il client della disconnessione, rispondiamo ad ogni ping con un pong ecc...



    public void receiveAction(ClientAction action, UUID token) {
        GameContext gameContext = getPlayerVirtualClient(action.getUsername(), token).getGameContext();
        gameContext.addClientAction(action);
    }

    public void sendAction(String username, ServerAction action) throws NetworkException {
        playerSessions.get(username).getVirtualClient().sendAction(action);
    }



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

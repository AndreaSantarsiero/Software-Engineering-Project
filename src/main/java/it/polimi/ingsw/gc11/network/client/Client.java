package it.polimi.ingsw.gc11.network.client;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.*;


/**
 * Abstract base class representing a network client in the game.
 *
 * <p>The {@code Client} class defines a general interface and shared logic for different types
 * of network clients, such as socket-based or RMI-based implementations. It provides methods
 * for sending and receiving actions between the client and server, and manages the client session token.</p>
 *
 * <p>All concrete clients (e.g., {@code ClientSocket}, {@code ClientRMI}) must implement the
 * abstract methods for registering a session and sending actions to the server.</p>
 */
public abstract class Client {

    protected UUID clientSessionToken;
    protected VirtualServer virtualServer;


    /**
     * Constructs a new client with the given virtual server handler.
     *
     * @param virtualServer the local handler used to process server-originated actions
     */
    public Client(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }


    /**
     * Sets the session token for this client.
     *
     * @param clientSessionToken the session token received from the server
     */
    public void setClientSessionToken(UUID clientSessionToken) {
        this.clientSessionToken = clientSessionToken;
    }

    /**
     * Returns the session token associated with this client.
     *
     * @return the session token
     */
    public UUID getClientSessionToken() {
        return clientSessionToken;
    }



    //from client to server
    /**
     * Registers a new session on the server using the given username.
     *
     * @param username the username to register
     * @throws NetworkException if the registration request fails due to network issues
     */
    public abstract void registerSession(String username) throws NetworkException;

    /**
     * Sends a controller-level action to the server.
     *
     * @param action the {@link ClientControllerAction} to send
     * @throws NetworkException if a network error occurs during transmission
     */
    public abstract void sendAction(ClientControllerAction action) throws NetworkException;

    /**
     * Sends a game-contextual action to the server.
     *
     * @param action the {@link ClientGameAction} to send
     * @throws NetworkException if a network error occurs during transmission
     */
    public abstract void sendAction(ClientGameAction action) throws NetworkException;



    //from server to client
    /**
     * Handles an action received from the server by forwarding it to the {@link VirtualServer}.
     *
     * @param action the {@link ServerAction} received from the server
     */
    public void sendAction(ServerAction action){
        virtualServer.receiveAction(action);
    }
}

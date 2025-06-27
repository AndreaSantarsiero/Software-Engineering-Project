package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.Serializable;
import java.util.UUID;


/**
 * Action that allows a client request to be handled by the ServerController.
 * Encapsulates the originating username and authentication token,
 * and defines the execution contract which may throw a NetworkException.
 */
public abstract class ClientControllerAction implements Serializable {

    protected final String username;
    protected  UUID token;

    /**
     * Constructs a new ClientControllerAction for the specified user.
     *
     * @param username the name of the client issuing this action
     */
    public ClientControllerAction(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the client who issued this action.
     *
     * @return the client's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the session token associated with this action.
     *
     * @return the UUID token for authentication
     */
    public UUID getToken() {
        return token;
    }

    /**
     * Sets the session token for this action.
     *
     * @param token the UUID token to authenticate the client
     */
    public void setToken(UUID token) {
        this.token = token;
    }

    /**
     * Executes this controller action within the provided ServerController.
     * Implementations perform server-side logic in response to client requests.
     *
     * @param serverController the controller managing server operations
     * @throws NetworkException if a network-level error occurs during execution
     */
    public abstract void execute(ServerController serverController) throws NetworkException;
}
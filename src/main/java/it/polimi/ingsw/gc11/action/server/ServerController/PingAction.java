package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;


/**
 * Action that allows a client to ping the server to keep the connection alive.
 * Delegates to {@code serverController.ping} with the client's username and token.
 *
 * @see ServerController#ping(String, java.util.UUID)
 */
public class PingAction extends ClientControllerAction {

    /**
     * Constructs a new PingAction for the specified client.
     *
     * @param username the name of the client sending the ping
     */
    public PingAction(String username) {
        super(username);
    }

    /**
     * Executes the ping in the ServerController.
     * May throw a NetworkException on communication failure.
     *
     * @param serverController the ServerController handling the ping
     * @throws NetworkException if a network error occurs during the ping
     */
    @Override
    public void execute(ServerController serverController) throws NetworkException {
        serverController.ping(username, token);
    }
}

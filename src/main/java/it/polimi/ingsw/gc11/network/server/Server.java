package it.polimi.ingsw.gc11.network.server;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.network.server.rmi.ServerRMI;
import it.polimi.ingsw.gc11.network.server.socket.ServerSocket;
import java.util.*;



/**
 * Abstract class representing a network server that mediates communication between clients and the core game logic
 * This class handles common game-related operations, while delegating connection-specific methods
 * to its subclasses (e.g., {@link ServerRMI}, {@link ServerSocket})
 */
public abstract class Server {

    protected final ServerController serverController;


    /**
     * Constructs a new {@code Server} with the given {@link ServerController}.
     *
     * @param serverController the controller responsible for managing game logic and client sessions
     */
    public Server(ServerController serverController) {
        this.serverController = serverController;
    }


    /**
     * Sends a {@link ClientControllerAction} to the server controller.
     * <p>
     * This type of action represents operations such as user registration or color selection.
     * </p>
     *
     * @param action the controller-level action to be handled by the game logic
     */
    public void sendAction(ClientControllerAction action) {
        serverController.addClientControllerAction(action);
    }

    /**
     * Sends a {@link ClientGameAction} to the server controller, identified by a session token.
     * <p>
     * This type of action is typically issued during the gameplay phase and is bound to a specific player session.
     * </p>
     *
     * @param action the game-level action to be handled by the game logic
     * @param token  the UUID token identifying the client session
     */
    public void sendAction(ClientGameAction action, UUID token) {
        serverController.receiveAction(action, token);
    }


    /**
     * Shuts down the server and cleans up any network resources.
     * <p>
     * This method must be implemented by subclasses to handle protocol-specific teardown
     * (e.g., closing socket servers or unbinding RMI registries).
     * </p>
     *
     * @throws Exception if an error occurs during shutdown
     */
    public abstract void shutdown() throws Exception;
}

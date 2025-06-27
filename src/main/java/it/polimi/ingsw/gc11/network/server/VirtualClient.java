package it.polimi.ingsw.gc11.network.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;


/**
 * Abstract representation of a connected client on the server side.
 * <p>
 * This class abstracts away the communication protocol (e.g., RMI or Socket),
 * allowing the server to send actions to clients in a uniform way.
 * Each {@code VirtualClient} instance is also associated with a {@link GameContext},
 * which encapsulates game-specific data related to that client.
 * </p>
 *
 * <p>Concrete implementations of this class include:
 * <ul>
 *     <li>{@link it.polimi.ingsw.gc11.network.server.rmi.VirtualRMIClient}</li>
 *     <li>{@link it.polimi.ingsw.gc11.network.server.socket.VirtualSocketClient}</li>
 * </ul>
 * </p>
 */
public abstract class VirtualClient {

    private GameContext gameContext;


    /**
     * Returns the {@link GameContext} associated with this client.
     *
     * @return the game context of this client
     */
    public GameContext getGameContext() {
        return gameContext;
    }

    /**
     * Associates a {@link GameContext} with this client.
     *
     * @param gameContext the game context to associate with the client
     */
    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }


    /**
     * Sends a {@link ServerAction} from the server to the client.
     * <p>
     * Concrete subclasses implement this method using the appropriate network protocol (RMI or Socket).
     * </p>
     *
     * @param action the action to send
     * @throws NetworkException if the message cannot be sent due to a network error
     */
    public abstract void sendAction(ServerAction action) throws NetworkException;
}

package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import java.io.Serializable;


/**
 * Action that serves as the base for all game actions initiated by a client.
 * Encapsulates the player's username and defines the contract for execution
 * within a GameContext.
 */
public abstract class ClientGameAction implements Serializable {

    protected final String username;

    /**
     * Constructs a new ClientGameAction for the specified player.
     *
     * @param username the name of the player issuing the action
     */
    public ClientGameAction(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the player who initiated this action.
     *
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Executes the action's logic within the provided game context.
     *
     * @param context the GameContext in which to execute this action
     */
    public abstract void execute(GameContext context);
}

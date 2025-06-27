package it.polimi.ingsw.gc11.network.server.socket;

import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import java.io.Serializable;
import java.util.UUID;


/**
 * Represents a message sent by a client to the server during the game phase,
 * used in the socket-based communication model.
 * <p>
 * This class encapsulates a {@link UUID} session token identifying the player,
 * along with the {@link ClientGameAction} representing the action the client wants to perform.
 * </p>
 *
 * @see ClientGameAction
 * @see java.io.Serializable
 */
public class ClientGameMessage implements Serializable {

    private final UUID token;
    private final ClientGameAction clientGameAction;

    /**
     * Constructs a new {@code ClientGameMessage} with the specified session token and action.
     *
     * @param token the unique session identifier associated with the client
     * @param clientGameAction the game action the client wishes to perform
     */
    public ClientGameMessage(UUID token, ClientGameAction clientGameAction) {
        this.token = token;
        this.clientGameAction = clientGameAction;
    }

    /**
     * Returns the session token associated with this message.
     *
     * @return the client's {@link UUID} token
     */
    public UUID getToken() {
        return token;
    }

    /**
     * Returns the game action included in this message.
     *
     * @return the {@link ClientGameAction} sent by the client
     */
    public ClientGameAction getClientGameAction() {
        return clientGameAction;
    }
}

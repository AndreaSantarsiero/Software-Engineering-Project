package it.polimi.ingsw.gc11.controller.network.server.socket;

import it.polimi.ingsw.gc11.controller.action.server.GameContext.ClientGameAction;
import java.io.Serializable;
import java.util.UUID;



public class ClientGameMessage implements Serializable {

    private final UUID token;
    private final ClientGameAction clientGameAction;


    public ClientGameMessage(UUID token, ClientGameAction clientGameAction) {
        this.token = token;
        this.clientGameAction = clientGameAction;
    }


    public UUID getToken() {
        return token;
    }

    public ClientGameAction getClientGameAction() {
        return clientGameAction;
    }
}

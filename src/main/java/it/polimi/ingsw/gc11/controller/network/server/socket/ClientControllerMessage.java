package it.polimi.ingsw.gc11.controller.network.server.socket;

import it.polimi.ingsw.gc11.controller.action.server.ServerController.ClientControllerAction;

import java.util.UUID;

public class ClientControllerMessage {
    private final UUID token;
    private final ClientControllerAction clientControllerAction;

    public ClientControllerMessage(UUID token, ClientControllerAction clientControllerAction) {
        this.token = token;
        this.clientControllerAction = clientControllerAction;
    }

    public UUID getToken() {
        return token;
    }

    public ClientControllerAction getClientControllerAction() {
        return clientControllerAction;
    }
}

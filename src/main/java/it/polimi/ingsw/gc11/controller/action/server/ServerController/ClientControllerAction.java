package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.Serializable;
import java.util.UUID;



public abstract class ClientControllerAction implements Serializable {

    protected final String username;
    protected final UUID token;


    public ClientControllerAction(String username, UUID token) {
        this.username = username;
        this.token = token;
    }


    public String getUsername() {
        return username;
    }

    public UUID getToken() {
        return token;
    }


    public abstract void execute(ServerController serverController) throws NetworkException;
}
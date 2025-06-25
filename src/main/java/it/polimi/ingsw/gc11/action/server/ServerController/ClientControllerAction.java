package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.Serializable;
import java.util.UUID;



public abstract class ClientControllerAction implements Serializable {

    protected final String username;
    protected  UUID token;


    public ClientControllerAction(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }


    public abstract void execute(ServerController serverController) throws NetworkException;
}
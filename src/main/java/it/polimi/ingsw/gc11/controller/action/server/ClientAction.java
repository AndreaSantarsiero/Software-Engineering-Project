package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import java.io.Serializable;



public abstract class ClientAction implements Serializable {

    String username;


    public ClientAction(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract void execute(GameContext context);
}

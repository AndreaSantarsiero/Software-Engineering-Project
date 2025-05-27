package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import java.io.Serializable;



public abstract class ClientGameAction implements Serializable {

    protected final String username;


    public ClientGameAction(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }


    public abstract void execute(GameContext context);
}

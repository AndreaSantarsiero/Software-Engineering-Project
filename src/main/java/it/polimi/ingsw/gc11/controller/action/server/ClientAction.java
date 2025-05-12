package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.action.Action;



public abstract class ClientAction implements Action {

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
}

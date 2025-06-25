package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;



public class PingAction extends ClientControllerAction {

    public PingAction(String username) {
        super(username);
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {

    }
}

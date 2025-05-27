package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.List;
import java.util.UUID;



public class GetAvailableMatches extends ClientControllerAction {

    public GetAvailableMatches(String username, UUID token) {
        super(username, token);
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        Map<String, List<String>> availableMatches = serverController.getAvailableMatches(username, token);
        //invio risposta con il parametro
    }
}

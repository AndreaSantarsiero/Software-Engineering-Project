package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.Map;
import java.util.UUID;



public class GetPlayersAction extends ClientControllerAction {

    public GetPlayersAction(String username, UUID token) {
        super(username, token);
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        Map<String, String> players = serverController.getPlayers(username, token);
        //invio risposta con il parametro
    }
}

package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendAvailableMatchesAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.List;
import java.util.Map;



public class GetAvailableMatches extends ClientControllerAction {

    public GetAvailableMatches(String username) {
        super(username);
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            Map<String, List<String>> availableMatches = serverController.getAvailableMatches(username, token);
            SendAvailableMatchesAction response = new SendAvailableMatchesAction(availableMatches);
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

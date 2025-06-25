package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.SendAvailableMatchesAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.List;
import java.util.Map;



public class ConnectToGameAction extends ClientControllerAction {

    private final String matchId;


    public ConnectToGameAction(String username, String matchId) {
        super(username);
        this.matchId = matchId;
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            serverController.connectPlayerToGame(username, token, matchId);
            Map<String, List<String>> availableMatches = serverController.getAvailableMatches(username, token);

            for(String username : serverController.getUsernameList()){
                SendAvailableMatchesAction action = new SendAvailableMatchesAction(availableMatches);
                serverController.sendAction(username, action);
            }

            NotifySuccessAction response = new NotifySuccessAction();
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

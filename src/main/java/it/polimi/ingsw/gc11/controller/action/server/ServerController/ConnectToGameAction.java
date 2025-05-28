package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;



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
            NotifySuccessAction response = new NotifySuccessAction();
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

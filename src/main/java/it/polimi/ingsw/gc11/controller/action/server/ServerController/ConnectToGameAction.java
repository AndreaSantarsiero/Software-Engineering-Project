package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import java.util.UUID;



public class ConnectToGameAction extends ClientControllerAction {

    private final String matchId;


    public ConnectToGameAction(String username, UUID token, String matchId) {
        super(username, token);
        this.matchId = matchId;
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            serverController.connectPlayerToGame(username, token, matchId);
            NotifySuccessAction response = new NotifySuccessAction();
            serverController.sendAction(username, response);
        }
        catch (FullLobbyException | UsernameAlreadyTakenException e) {
            //invio risposta negativa
        }
    }
}

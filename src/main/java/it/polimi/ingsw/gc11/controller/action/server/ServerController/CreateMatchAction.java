package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;



public class CreateMatchAction extends ClientControllerAction {

    private final FlightBoard.Type flightLevel;
    private final int numPlayers;


    public CreateMatchAction(String username, FlightBoard.Type flightLevel, int numPlayers) {
        super(username);
        this.flightLevel = flightLevel;
        this.numPlayers = numPlayers;
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            serverController.createMatch(flightLevel, numPlayers, username, token);
            NotifySuccessAction response = new NotifySuccessAction();
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

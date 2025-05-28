package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.action.client.SendAvailableMatchesAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import java.util.List;
import java.util.Map;



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
            Map<String, GameContext> availableMatches = serverController.createMatch(flightLevel, numPlayers, username, token);
            Map<String, List<String>> matchesUpdate = serverController.getAvailableMatches(username, token);

            for(Map.Entry<String, GameContext> entry : availableMatches.entrySet()){
                GameContext match = entry.getValue();
                for(Player player : match.getGameModel().getPlayers()){
                    SendAvailableMatchesAction action = new SendAvailableMatchesAction(matchesUpdate);
                    serverController.sendAction(player.getUsername(), action);
                }
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

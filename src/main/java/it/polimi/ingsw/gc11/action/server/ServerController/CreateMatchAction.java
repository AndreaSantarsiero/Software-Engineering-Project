package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.SendAvailableMatchesAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Action that allows a client to create a new game match with the specified flight level and player count.
 * On success, broadcasts the updated list of available matches to all clients
 * and sends a NotifySuccessAction to the creator.
 * On failure, sends a NotifyExceptionAction with the error message.
 */
public class CreateMatchAction extends ClientControllerAction {

    private final FlightBoard.Type flightLevel;
    private final int numPlayers;

    /**
     * Constructs a new CreateMatchAction.
     *
     * @param username    the name of the client creating the match
     * @param flightLevel the flight board type for the match
     * @param numPlayers  the desired number of players
     */
    public CreateMatchAction(String username, FlightBoard.Type flightLevel, int numPlayers) {
        super(username);
        this.flightLevel = flightLevel;
        this.numPlayers = numPlayers;
    }

    /**
     * Executes the match creation in the ServerController.
     * <ul>
     *   <li>Calls {@code serverController.createMatch} with the provided parameters.</li>
     *   <li>Retrieves the updated available matches map.</li>
     *   <li>Broadcasts a SendAvailableMatchesAction to all connected clients.</li>
     *   <li>Sends a NotifySuccessAction to the creator.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message to the requester.</li>
     * </ul>
     *
     * @param serverController the ServerController handling the request
     * @throws NetworkException if a network error occurs during execution
     */
    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            serverController.createMatch(flightLevel, numPlayers, username, token);
            Map<String, List<String>> availableMatches = serverController.getAvailableMatches(username, token);

            for(String username : serverController.getUsernameList()){
                try{
                    SendAvailableMatchesAction action = new SendAvailableMatchesAction(availableMatches);
                    serverController.sendAction(username, action);
                } catch (Exception ignored) {} //in case some clients are disconnected
            }

            NotifySuccessAction response = new NotifySuccessAction();
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            serverController.sendAction(username, exception);
        }
    }
}

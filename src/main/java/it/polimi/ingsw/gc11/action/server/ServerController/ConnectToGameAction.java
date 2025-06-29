package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.SendAvailableMatchesAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.List;
import java.util.Map;


/**
 * Action that allows a client to connect to a specified game match.
 * On success, broadcasts the updated list of available matches to all clients
 * and sends a NotifySuccessAction to the connecting client.
 * On failure, sends a NotifyExceptionAction with the error message.
 */
public class ConnectToGameAction extends ClientControllerAction {

    private final String matchId;

    /**
     * Constructs a new ConnectToGameAction.
     *
     * @param username the name of the client connecting to the match
     * @param matchId  the identifier of the match to join
     */
    public ConnectToGameAction(String username, String matchId) {
        super(username);
        this.matchId = matchId;
    }

    /**
     * Executes the connection logic in the ServerController.
     * <ul>
     *   <li>Calls {@code serverController.connectPlayerToGame} with the client's credentials and match ID.</li>
     *   <li>Retrieves the updated map of available matches.</li>
     *   <li>Broadcasts a SendAvailableMatchesAction to all connected clients.</li>
     *   <li>Sends a NotifySuccessAction to the connecting client.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message to the requester.</li>
     * </ul>
     *
     * @param serverController the ServerController handling the request
     * @throws NetworkException if a network error occurs during execution
     */
    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            serverController.connectPlayerToGame(username, token, matchId);
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
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

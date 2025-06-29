package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendAvailableMatchesAction;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.List;
import java.util.Map;


/**
 * Action that allows a client to request the current list of available matches.
 * On success, sends a SendAvailableMatchesAction to the requester;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class GetAvailableMatchesAction extends ClientControllerAction {

    /**
     * Constructs a new GetAvailableMatchesAction for the specified user.
     *
     * @param username the name of the client requesting available matches
     */
    public GetAvailableMatchesAction(String username) {
        super(username);
    }

    /**
     * Executes the retrieval of available matches in the ServerController.
     * <ul>
     *   <li>Calls {@code serverController.getAvailableMatches(username, token)}.</li>
     *   <li>Sends a SendAvailableMatchesAction containing the map of matches to the requester.</li>
     *   <li>On exception, sends a NotifyExceptionAction with the error message to the requester.</li>
     * </ul>
     *
     * @param serverController the ServerController handling the request
     * @throws NetworkException if a network error occurs during execution
     */
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

package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendSessionDataAction;
import it.polimi.ingsw.gc11.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.network.server.rmi.VirtualRMIClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.UUID;


/**
 * Action that registers an RMI session for a connecting client.
 * On success, sends a SendSessionDataAction containing the assigned token to the client.
 * On failure, sends a NotifyExceptionAction via the unregistered RMI client stub.
 */
public class RegisterRMISessionAction extends ClientControllerAction {

    private final ClientInterface playerStub;

    /**
     * Constructs a new RegisterRMISessionAction for the specified client stub.
     *
     * @param username   the name of the client registering an RMI session
     * @param playerStub the RMI stub representing the client callback interface
     */
    public RegisterRMISessionAction(String username, ClientInterface playerStub) {
        super(username);
        this.playerStub = playerStub;
    }

    /**
     * Executes the RMI session registration in the ServerController.
     * <ul>
     *   <li>Calls {@code serverController.registerRMISession} to obtain a session token.</li>
     *   <li>Sends a {@link SendSessionDataAction} with the username and token to the client.</li>
     *   <li>On exception, sends a {@link NotifyExceptionAction} back via the unregistered RMI client.</li>
     * </ul>
     *
     * @param serverController the ServerController handling the registration
     * @throws NetworkException if a network error occurs during registration
     */
    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            UUID token = serverController.registerRMISession(username, playerStub);
            SendSessionDataAction response = new SendSessionDataAction(username, token);
            serverController.sendAction(username, response);
        } catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendUnregisteredAction(new VirtualRMIClient(playerStub), exception);
        }
    }
}

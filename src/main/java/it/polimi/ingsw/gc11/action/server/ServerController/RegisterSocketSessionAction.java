package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendSessionDataAction;
import it.polimi.ingsw.gc11.network.server.socket.VirtualSocketClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.UUID;


/**
 * Action that allows a client to register a socket-based session.
 * On success, sends a SendSessionDataAction with the assigned token;
 * on failure, sends a NotifyExceptionAction via the unregistered socket client.
 */
public class RegisterSocketSessionAction extends ClientControllerAction {

    private VirtualSocketClient virtualSocketClient;

    /**
     * Constructs a new RegisterSocketSessionAction for the specified username.
     *
     * @param username the name of the client registering a socket session
     */
    public RegisterSocketSessionAction(String username) {
        super(username);
    }

    /**
     * Sets the virtual socket client through which to send unregistered actions.
     *
     * @param virtualSocketClient the VirtualSocketClient representing the client connection
     */
    public void setVirtualSocketClient(VirtualSocketClient virtualSocketClient) {
        this.virtualSocketClient = virtualSocketClient;
    }

    /**
     * Executes the socket session registration in the ServerController.
     * <ul>
     *   <li>Calls {@code serverController.registerSocketSession} to obtain a session token.</li>
     *   <li>Sends a {@link SendSessionDataAction} with the username and token to the client.</li>
     *   <li>On exception, sends a {@link NotifyExceptionAction} via the unregistered socket client.</li>
     * </ul>
     *
     * @param serverController the ServerController handling the registration
     * @throws NetworkException if a network error occurs during registration
     */
    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            UUID token = serverController.registerSocketSession(username, virtualSocketClient);
            SendSessionDataAction response = new SendSessionDataAction(username, token);
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendUnregisteredAction(virtualSocketClient, exception);
        }
    }
}

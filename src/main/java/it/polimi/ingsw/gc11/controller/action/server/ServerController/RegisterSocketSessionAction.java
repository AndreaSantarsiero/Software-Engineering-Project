package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendUUIDTokenAction;
import it.polimi.ingsw.gc11.controller.network.server.socket.VirtualSocketClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.UUID;



public class RegisterSocketSessionAction extends ClientControllerAction {

    private VirtualSocketClient virtualSocketClient;


    public RegisterSocketSessionAction(String username) {
        super(username);
    }


    public void setVirtualSocketClient(VirtualSocketClient virtualSocketClient) {
        this.virtualSocketClient = virtualSocketClient;
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            UUID token = serverController.registerSocketSession(username, virtualSocketClient);
            SendUUIDTokenAction response = new SendUUIDTokenAction(token);
            serverController.sendAction(username, response);
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

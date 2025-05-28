package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendSessionDataAction;
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
            System.out.println("[SOCKET] Creating socket session on server...");
            UUID token = serverController.registerSocketSession(username, virtualSocketClient);
            SendSessionDataAction response = new SendSessionDataAction(username, token);
            serverController.sendAction(username, response);
            System.out.println("[SOCKET] Sending socket session response...");
        }
        catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
            System.out.println("[SOCKET] Sending socket session exception...");
        }
    }
}

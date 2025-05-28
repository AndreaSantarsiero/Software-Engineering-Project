package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.server.socket.VirtualSocketClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
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
        }
        catch (UsernameAlreadyTakenException e) {

        }
    }
}

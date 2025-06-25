package it.polimi.ingsw.gc11.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.SendSessionDataAction;
import it.polimi.ingsw.gc11.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.network.server.rmi.VirtualRMIClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.util.UUID;



public class RegisterRMISessionAction extends ClientControllerAction {

    private final ClientInterface playerStub;


    public RegisterRMISessionAction(String username, ClientInterface playerStub) {
        super(username);
        this.playerStub = playerStub;
    }


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

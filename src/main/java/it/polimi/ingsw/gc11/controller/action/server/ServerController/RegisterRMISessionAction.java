package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendUUIDTokenAction;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientInterface;
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
            SendUUIDTokenAction response = new SendUUIDTokenAction(token);
            serverController.sendAction(username, response);
        } catch (Exception e) {
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            serverController.sendAction(username, exception);
        }
    }
}

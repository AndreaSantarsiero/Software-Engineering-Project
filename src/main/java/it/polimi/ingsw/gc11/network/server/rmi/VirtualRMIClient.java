package it.polimi.ingsw.gc11.network.server.rmi;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.network.server.VirtualClient;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.rmi.RemoteException;



public class VirtualRMIClient extends VirtualClient {

    private final ClientInterface clientStub;



    public VirtualRMIClient(ClientInterface clientStub) {
        this.clientStub = clientStub;
    }



    @Override
    public void sendAction(ServerAction action) throws NetworkException {
        try {
            clientStub.sendAction(action);
        } catch (RemoteException e) {
            throw new NetworkException(e.getMessage());
        }
    }
}

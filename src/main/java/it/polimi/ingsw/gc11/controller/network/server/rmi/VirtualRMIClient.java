package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.controller.network.server.VirtualClient;



public class VirtualRMIClient extends VirtualClient {

    private final ClientInterface clientStub;



    public VirtualRMIClient(ClientInterface clientStub) {
        this.clientStub = clientStub;
    }



    @Override
    public void notifyException(String message) {
        clientStub.notifyException(message);
    }
}

package it.polimi.ingsw.gc11.network.client.rmi;

import it.polimi.ingsw.gc11.action.client.ServerAction;
import it.polimi.ingsw.gc11.network.client.Client;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class ClientStubExporter extends UnicastRemoteObject implements ClientInterface {

    private final Client client;



    protected ClientStubExporter(Client client) throws RemoteException {
        super();
        this.client = client;
    }



    public void sendAction(ServerAction action) {
        client.sendAction(action);
    }
}

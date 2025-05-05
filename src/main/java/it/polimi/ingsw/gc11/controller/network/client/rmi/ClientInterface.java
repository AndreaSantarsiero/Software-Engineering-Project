package it.polimi.ingsw.gc11.controller.network.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;


public interface ClientInterface extends Remote {
    public UUID checkConnection() throws RemoteException;
}

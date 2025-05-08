package it.polimi.ingsw.gc11.controller.network.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface ClientInterface extends Remote {
    void notifyException(String message) throws RemoteException;
}

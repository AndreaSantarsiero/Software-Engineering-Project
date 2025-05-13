package it.polimi.ingsw.gc11.controller.network.client.rmi;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import java.rmi.Remote;
import java.rmi.RemoteException;



//E' lo stub che esporta il client per il server
public interface ClientInterface extends Remote {
    void sendAction(ServerAction action) throws RemoteException;
}

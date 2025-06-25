package it.polimi.ingsw.gc11.network.server.rmi;

import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;



public interface ServerInterface extends Remote {

    void sendAction(ClientControllerAction action) throws RemoteException;
    void sendAction(ClientGameAction action, UUID token) throws RemoteException;
}

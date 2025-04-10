package it.polimi.ingsw.gc11.controller.Network;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface Loggable extends Remote {
    boolean login(String nick) throws RemoteException;
    void logout(String nick) throws RemoteException;
    String hello(String nick) throws RemoteException;
}

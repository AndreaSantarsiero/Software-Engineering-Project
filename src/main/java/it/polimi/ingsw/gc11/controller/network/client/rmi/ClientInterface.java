package it.polimi.ingsw.gc11.controller.network.client.rmi;

import java.rmi.Remote;



public interface ClientInterface extends Remote {
    public void notifyException(String message);
}

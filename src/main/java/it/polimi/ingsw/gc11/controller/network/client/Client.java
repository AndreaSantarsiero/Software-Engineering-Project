package it.polimi.ingsw.gc11.controller.network.client;

import java.util.UUID;



public abstract class Client {

    protected UUID clientSessionToken;



    public UUID checkConnection(){
        return clientSessionToken;
    }
}

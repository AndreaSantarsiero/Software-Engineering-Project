package it.polimi.ingsw.gc11.network.server;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.action.server.GameContext.ClientGameAction;
import it.polimi.ingsw.gc11.action.server.ServerController.ClientControllerAction;
import it.polimi.ingsw.gc11.network.server.rmi.ServerRMI;
import it.polimi.ingsw.gc11.network.server.socket.ServerSocket;
import java.util.*;



/**
 * Abstract class representing a network server that mediates communication between clients and the core game logic
 * This class handles common game-related operations, while delegating connection-specific methods
 * to its subclasses (e.g., {@link ServerRMI}, {@link ServerSocket})
 */
public abstract class Server {

    protected final ServerController serverController;



    public Server(ServerController serverController) {
        this.serverController = serverController;
    }



    public void sendAction(ClientControllerAction action) {
        serverController.addClientControllerAction(action);
    }


    public void sendAction(ClientGameAction action, UUID token) {
        serverController.receiveAction(action, token);
    }



    public abstract void shutdown() throws Exception;
}

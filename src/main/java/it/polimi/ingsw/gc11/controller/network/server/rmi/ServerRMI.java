package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.controller.network.server.Server;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;



/**
 * RMI-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerRMI extends Server implements ServerInterface {

    private final Registry registry;



    public ServerRMI(ServerController serverController, int port) throws NetworkException, UsernameAlreadyTakenException {
        super(serverController);
        try {
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(port);
            registry.bind("ServerInterface", stub);
        }
        catch (RemoteException | AlreadyBoundException e) {
            throw new NetworkException("RMI server could not bind to port: " + port);
        }
    }



    /**
     * Registers a new RMI-based player session
     *
     * @param username the player's unique identifier
     * @return the UUID token assigned to the session
     */
    public UUID registerPlayerSession(String username, ClientInterface playerStub) throws UsernameAlreadyTakenException {
        return serverController.registerRMISession(username, playerStub);
    }



    @Override
    public void shutdown() throws Exception {
        if (registry != null) {
            registry.unbind("ServerInterface");
            UnicastRemoteObject.unexportObject(this, true);
        }
    }
}

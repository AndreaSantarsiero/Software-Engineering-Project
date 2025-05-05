package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.server.Server;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
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



    public ServerRMI(ServerController serverController, int port) throws NetworkException {
        super(serverController);
        try {
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, port);
            registry = LocateRegistry.createRegistry(port);
            registry.bind("ServerInterface", stub);
        }
        catch (Exception e) {
            throw new NetworkException("RMI server could not bind to port: " + port);
        }
    }



    /**
     * Registers a new RMI-based player session with the specified match ID
     *
     * @param username the player's unique identifier
     * @param matchId the identifier of the match to join
     * @return the UUID token assigned to the session
     */
    @Override
    protected UUID registerPlayerSession(String username, String matchId){
        return serverController.registerPlayerSession(username, matchId, Utils.ConnectionType.RMI);
    }



    @Override
    public void shutdown() throws Exception {
        if (registry != null) {
            registry.unbind("ServerInterface");
            UnicastRemoteObject.unexportObject(this, true);
        }
    }
}

package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.client.rmi.ClientInterface;
import it.polimi.ingsw.gc11.controller.network.server.Server;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;



/**
 * RMI-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerRMI extends Server implements ServerInterface {

    private ClientInterface clientStub;



    public ServerRMI(ServerController serverController, int port) throws RemoteException, AlreadyBoundException {
        super(serverController);
        try {
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, port);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("ServerInterface", stub);
        }
        catch (Exception e) {
            throw new ServerException(e.getMessage());
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
}

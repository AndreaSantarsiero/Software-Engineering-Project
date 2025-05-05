package it.polimi.ingsw.gc11.controller.network.server.socket;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.server.Server;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import java.io.IOException;
import java.util.UUID;



/**
 * Socket-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerSocket extends Server {

    private final java.net.ServerSocket serverSocket;



    public ServerSocket(ServerController serverController, int port) {
        super(serverController);
        try{
            this.serverSocket = new java.net.ServerSocket(port);
        } catch (IOException e) {
            throw new NetworkException("Socket server could not bind to port: " + port);
        }
    }



    /**
     * Registers a new Socket-based player session with the specified match ID
     *
     * @param username the player's unique identifier
     * @param matchId the identifier of the match to join
     * @return the UUID token assigned to the session
     */
    @Override
    protected UUID registerPlayerSession(String username, String matchId){
        return serverController.registerPlayerSession(username, matchId, Utils.ConnectionType.SOCKET);
    }
}

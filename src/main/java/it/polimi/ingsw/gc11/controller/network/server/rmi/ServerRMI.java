package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.server.Server;
import java.util.UUID;



/**
 * RMI-based implementation of the server interface that delegates core logic
 * to the {@link ServerController}
 */
public class ServerRMI extends Server implements ServerInterface {

    public ServerRMI(ServerController serverController) {
        super(serverController);
    }



    /**
     * Registers a new RMI-based player session with the specified match ID
     *
     * @param username the player's unique identifier
     * @param matchId the identifier of the match to join
     * @return the UUID token assigned to the session
     */
    @Override
    public UUID connectPlayerToGame(String username, String matchId){
        return serverController.registerPlayerSession(username, matchId, Utils.ConnectionType.RMI);
    }
}

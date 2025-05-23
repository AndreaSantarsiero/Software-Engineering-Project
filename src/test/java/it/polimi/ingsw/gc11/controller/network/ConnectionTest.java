package it.polimi.ingsw.gc11.controller.network;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class ConnectionTest {

    ServerController serverController;
    String serverIp = "127.0.0.1";
    static int RMIPort = 1099;
    static int socketPort = 1234;



    @BeforeEach
    void setUp() throws InterruptedException, NetworkException, UsernameAlreadyTakenException {
        serverController = new ServerController(RMIPort, socketPort);
        Thread.sleep(200);  //waiting for the server to start up
        System.out.println("\nServer started on RMI port: " + RMIPort + ", Socket port: " + socketPort);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        RMIPort++;
        socketPort++;
        if (serverController != null) {
            serverController.shutdown();
            Thread.sleep(200);
        }
    }



    @Test
    void testCreateMatch() throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {
        VirtualServer playerOne = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerOne.registerSession("username1");
        playerOne.createMatch(FlightBoard.Type.LEVEL2, 4);
        assertEquals(1, playerOne.getAvailableMatches().size(), "Number of available matches doesn't match");

        VirtualServer playerTwo = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerTwo.registerSession("username2");
        playerTwo.createMatch(FlightBoard.Type.TRIAL, 2);
        assertEquals(2, playerOne.getAvailableMatches().size(), "Number of available matches doesn't match");
    }



    @Test
    void testUsername() throws NetworkException, UsernameAlreadyTakenException {
        VirtualServer playerOne = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerOne.registerSession("username");
        VirtualServer playerTwo = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        assertThrows(UsernameAlreadyTakenException.class, () -> playerTwo.registerSession("username"), "Username already taken");
        assertThrows(IllegalArgumentException.class, () -> playerTwo.registerSession(""), "Username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> playerTwo.registerSession(null), "Username cannot be null");
    }



    @Test
    void testFullLobby() throws NetworkException, UsernameAlreadyTakenException, FullLobbyException {
        VirtualServer playerOne = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerOne.registerSession("playerOne");
        VirtualServer playerTwo = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerTwo.registerSession("playerTwo");
        VirtualServer playerThree = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerThree.registerSession("playerThree");
        VirtualServer playerFour = new VirtualServer(Utils.ConnectionType.RMI, serverIp, RMIPort, null);
        playerFour.registerSession("playerFour");

        playerOne.createMatch(FlightBoard.Type.LEVEL2, 3);
        String matchId = playerTwo.getAvailableMatches().keySet().iterator().next();
        playerTwo.connectToGame(matchId);
        playerThree.connectToGame(matchId);
        assertEquals(1, playerOne.getAvailableMatches().size(), "Number of available matches doesn't match");
        assertThrows(FullLobbyException.class, () -> playerFour.connectToGame(matchId), "Cannot connect to a full lobby");
    }
}

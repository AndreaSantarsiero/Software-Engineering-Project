package it.polimi.ingsw.gc11.controller.network;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class ConnectionTest {

    static ServerController serverController;
    static String serverIp = "127.0.0.1";
    static int RMIPort = 1099;
    static int SocketPort = 1234;



    @BeforeAll
    static void setUp() throws InterruptedException {
        serverController = new ServerController(RMIPort, SocketPort);
        Thread.sleep(1000);  //waiting for the server to start up
        System.err.println("Server started on RMI port: " + RMIPort + ", Socket port: " + SocketPort);
    }



    @Test
    void testCreateMatch() {
        VirtualServer playerUno = new VirtualServer(Utils.ConnectionType.RMI, "playerUno", serverIp, RMIPort);
        playerUno.createMatch(FlightBoard.Type.LEVEL2, 4);
        VirtualServer playerDue = new VirtualServer(Utils.ConnectionType.RMI, "playerDue", serverIp, RMIPort);
        playerDue.createMatch(FlightBoard.Type.TRIAL, 4);
    }



    @Test
    void testUniqueUsername() {
        VirtualServer playerUno = new VirtualServer(Utils.ConnectionType.RMI, "username", serverIp, RMIPort);
        assertThrows(UsernameAlreadyTakenException.class, () -> new VirtualServer(Utils.ConnectionType.RMI, "username", serverIp, RMIPort), "Username already taken");
    }
}

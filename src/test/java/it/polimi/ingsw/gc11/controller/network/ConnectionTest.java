package it.polimi.ingsw.gc11.controller.network;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.FlightBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class ConnectionTest {

    ServerController serverController;
    String serverIp = "127.0.0.1";
    int RMIPort = 1099;
    int SocketPort = 1234;



    @BeforeEach
    void setUp() throws InterruptedException {
        serverController = new ServerController(RMIPort, SocketPort);
        Thread.sleep(1000);  //waiting for the server to start up
        System.err.println("Server started on RMI port: " + RMIPort + ", Socket port: " + SocketPort);
    }



    @Test
    void testCreateMatch() {
        VirtualServer playerUno = new VirtualServer(Utils.ConnectionType.RMI, "playerUno", serverIp, RMIPort);
        playerUno.createMatch(FlightBoard.Type.LEVEL2);
        VirtualServer playerDue = new VirtualServer(Utils.ConnectionType.RMI, "playerDue", serverIp, RMIPort);
        playerDue.createMatch(FlightBoard.Type.TRIAL);
    }
}

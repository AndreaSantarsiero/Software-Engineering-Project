package it.polimi.ingsw.gc11.controller.network;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.dumbClient.DumbPlayerContext;
import it.polimi.ingsw.gc11.network.Utils;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class ConnectionTest {

    ServerController serverController;
    String serverIp = "127.0.0.1";
    static int RMIPort = 1099;
    static int socketPort = 1234;



    @BeforeEach
    void setUp() throws InterruptedException, NetworkException, UsernameAlreadyTakenException {
        serverController = new ServerController(RMIPort, socketPort, 100000);
        Thread.sleep(20);  //waiting for the server to start up
        System.out.println("\nServer started on RMI port: " + RMIPort + ", Socket port: " + socketPort);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        RMIPort++;
        socketPort++;
        if (serverController != null) {
            serverController.shutdown();
            Thread.sleep(20);  //waiting for the server to shut down
        }
    }



    @Test
    void testCreateMatch() throws NetworkException, InterruptedException {
        DumbPlayerContext playerOneContext = new DumbPlayerContext();
        VirtualServer playerOne   = new VirtualServer(playerOneContext, 10000, false);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("username1");
        Thread.sleep(20);  //waiting for the server to register the session
        playerOne.createMatch(FlightBoard.Type.LEVEL2, 4);

        DumbPlayerContext playerTwoContext = new DumbPlayerContext();
        VirtualServer playerTwo   = new VirtualServer(playerTwoContext, 10000, false);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerTwo.registerSession("username2");
        Thread.sleep(20);  //waiting for the server to register the session
        playerTwo.createMatch(FlightBoard.Type.TRIAL, 2);
    }



    @Test
    void testFullLobby() throws NetworkException, InterruptedException {
        DumbPlayerContext playerOneContext = new DumbPlayerContext();
        VirtualServer playerOne   = new VirtualServer(playerOneContext, 10000, false);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("playerOne");

        DumbPlayerContext playerTwoContext = new DumbPlayerContext();
        VirtualServer playerTwo   = new VirtualServer(playerTwoContext, 10000, false);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerTwo.registerSession("playerTwo");

        DumbPlayerContext playerThreeContext = new DumbPlayerContext();
        VirtualServer playerThree = new VirtualServer(playerThreeContext, 10000, false);
        JoiningPhaseData dataThree = (JoiningPhaseData) playerThreeContext.getCurrentPhase();
        dataThree.setVirtualServer(playerThree);
        playerThree.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerThree.registerSession("playerThree");

        DumbPlayerContext playerFourContext = new DumbPlayerContext();
        VirtualServer playerFour = new VirtualServer(playerFourContext, 10000, false);
        JoiningPhaseData dataFour = (JoiningPhaseData) playerFourContext.getCurrentPhase();
        dataFour.setVirtualServer(playerFour);
        playerFour.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerFour.registerSession("playerFour");

        Thread.sleep(20);  //waiting for the server to register every session
        playerOne.createMatch(FlightBoard.Type.LEVEL2, 3);
    }
}

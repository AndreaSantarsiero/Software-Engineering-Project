package it.polimi.ingsw.gc11.controller.network;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testCreateMatch() throws NetworkException {
        PlayerContext playerOneContext = new PlayerContext();
        VirtualServer playerOne   = new VirtualServer(playerOneContext);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("username1");
        playerOne.createMatch(FlightBoard.Type.LEVEL2, 4);

        PlayerContext playerTwoContext = new PlayerContext();
        VirtualServer playerTwo   = new VirtualServer(playerTwoContext);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerTwo.registerSession("username2");
        playerTwo.createMatch(FlightBoard.Type.TRIAL, 2);
    }



    @Test
    void testUsername() throws NetworkException {
        PlayerContext playerOneContext = new PlayerContext();
        VirtualServer playerOne   = new VirtualServer(playerOneContext);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("username");

        PlayerContext playerTwoContext = new PlayerContext();
        VirtualServer playerTwo   = new VirtualServer(playerTwoContext);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        assertThrows(UsernameAlreadyTakenException.class, () -> playerTwo.registerSession("username"), "Username already taken");
        assertThrows(IllegalArgumentException.class, () -> playerTwo.registerSession(""), "Username cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> playerTwo.registerSession(null), "Username cannot be null");
    }



    @Test
    void testFullLobby() throws NetworkException {
        PlayerContext playerOneContext = new PlayerContext();
        VirtualServer playerOne   = new VirtualServer(playerOneContext);
        JoiningPhaseData dataOne = (JoiningPhaseData) playerOneContext.getCurrentPhase();
        dataOne.setVirtualServer(playerOne);
        playerOne.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerOne.registerSession("playerOne");

        PlayerContext playerTwoContext = new PlayerContext();
        VirtualServer playerTwo   = new VirtualServer(playerTwoContext);
        JoiningPhaseData dataTwo = (JoiningPhaseData) playerTwoContext.getCurrentPhase();
        dataTwo.setVirtualServer(playerTwo);
        playerTwo.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerTwo.registerSession("playerTwo");

        PlayerContext playerThreeContext = new PlayerContext();
        VirtualServer playerThree = new VirtualServer(playerThreeContext);
        JoiningPhaseData dataThree = (JoiningPhaseData) playerThreeContext.getCurrentPhase();
        dataThree.setVirtualServer(playerThree);
        playerThree.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerThree.registerSession("playerThree");

        PlayerContext playerFourContext = new PlayerContext();
        VirtualServer playerFour = new VirtualServer(playerFourContext);
        JoiningPhaseData dataFour = (JoiningPhaseData) playerFourContext.getCurrentPhase();
        dataFour.setVirtualServer(playerFour);
        playerFour.initializeConnection(Utils.ConnectionType.RMI, serverIp, RMIPort);
        playerFour.registerSession("playerFour");

        playerOne.createMatch(FlightBoard.Type.LEVEL2, 3);
    }
}

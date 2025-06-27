package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.network.Utils;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class ViewModel {

    private final PlayerContext playerContext;
    private VirtualServer virtualServer;
    private String serverIp;
    private Integer serverPort;



    public ViewModel(String serverIp, Integer serverPort) {
        this.playerContext = new PlayerContext();
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }



    public PlayerContext getPlayerContext() {
        return this.playerContext;
    }

    public VirtualServer getVirtualServer() {
        return this.virtualServer;
    }



    public void setRMIVirtualServer() throws NetworkException {

        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            config.load(fis);

            // Reading client configuration
            if(serverIp == null) {
                serverIp = config.getProperty("serverIp");
            }
            if(serverPort == null) {
                serverPort = Integer.parseInt(config.getProperty("serverRMIPort"));
            }
            int pingInterval = Integer.parseInt(config.getProperty("pingInterval"));

            this.virtualServer = new VirtualServer(playerContext, pingInterval, true);
            this.virtualServer.initializeConnection(Utils.ConnectionType.RMI, serverIp, serverPort);
            JoiningPhaseData joiningPhaseData = (JoiningPhaseData) this.playerContext.getCurrentPhase();
            joiningPhaseData.setVirtualServer(virtualServer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSOCKETVirtualServer() throws NetworkException {

        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            config.load(fis);

            // Reading client configuration
            if(serverIp == null) {
                serverIp = config.getProperty("serverIp");
            }
            if(serverPort == null) {
                serverPort = Integer.parseInt(config.getProperty("serverSocketPort"));
            }
            int pingInterval = Integer.parseInt(config.getProperty("pingInterval"));

            this.virtualServer = new VirtualServer(playerContext, pingInterval, true);
            virtualServer.initializeConnection(Utils.ConnectionType.SOCKET, serverIp, serverPort);
            JoiningPhaseData joiningPhaseData = (JoiningPhaseData) this.playerContext.getCurrentPhase();
            joiningPhaseData.setVirtualServer(virtualServer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

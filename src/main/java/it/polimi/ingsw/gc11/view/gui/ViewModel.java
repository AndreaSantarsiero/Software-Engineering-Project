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

    PlayerContext playerContext;
    VirtualServer virtualServer;



    public ViewModel() {
        this.playerContext = new PlayerContext();
    }



    public PlayerContext getPlayerContext() {
        return this.playerContext;
    }

    public VirtualServer getVirtualServer() {
        return this.virtualServer;
    }



    public void setRMIVirtualServer() throws NetworkException {

        this.virtualServer = new VirtualServer(playerContext);

        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            config.load(fis);

            // Reading client configuration
            String serverIp = config.getProperty("serverIp");
            int serverRMIPort = Integer.parseInt(config.getProperty("serverRMIPort"));
            int pingInterval = Integer.parseInt(config.getProperty("pingInterval"));

            this.virtualServer.initializeConnection(Utils.ConnectionType.RMI, serverIp, serverRMIPort);
            JoiningPhaseData joiningPhaseData = (JoiningPhaseData) this.playerContext.getCurrentPhase();
            joiningPhaseData.setVirtualServer(virtualServer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSOCKETVirtualServer() throws NetworkException {

        this.virtualServer = new VirtualServer(playerContext);

        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            config.load(fis);

            // Reading client configuration
            String serverIp = config.getProperty("serverIp");
            int serverSocketPort = Integer.parseInt(config.getProperty("serverSocketPort"));
            int pingInterval = Integer.parseInt(config.getProperty("pingInterval"));

            virtualServer.initializeConnection(Utils.ConnectionType.SOCKET, serverIp, serverSocketPort);
            JoiningPhaseData joiningPhaseData = (JoiningPhaseData) this.playerContext.getCurrentPhase();
            joiningPhaseData.setVirtualServer(virtualServer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}

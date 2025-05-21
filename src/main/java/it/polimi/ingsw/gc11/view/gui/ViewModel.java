package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.PlayerContext;
import java.util.ArrayList;


public class ViewModel {

    Player myself;
    ArrayList<Player> otherPlayers;

    VirtualServer virtualServer;

    public ViewModel() {

    }

    public Player getMyself() {
        return myself;
    }

    public ArrayList<Player> getOtherPlayers() {
        return this.otherPlayers;
    }

    public VirtualServer getVirtualServer() {
        return this.virtualServer;
    }

    public void setMyself(String username) {
        this.myself = new Player(username);
    }

    public void setRMIVirtualServer() throws NetworkException {
        this.virtualServer = new VirtualServer(Utils.ConnectionType.RMI, "127.0.0.1", 1099, new PlayerContext());
    }

    public void setSOCKETVirtualServer() throws NetworkException {
        //Socket ancora da configurare
        //this.virtualServer = new VirtualServer(Utils.ConnectionType.SOCKET, "127.0.0.1", 1234, new PlayerContext());
    }
}

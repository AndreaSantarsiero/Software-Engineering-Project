package it.polimi.ingsw.gc11.controller.network.client.socket;

import it.polimi.ingsw.gc11.controller.action.server.ClientAction;
import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;



public class ClientSocket extends Client {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;



    public ClientSocket(VirtualServer virtualServer, String ip, int port) throws IOException {
        super(virtualServer);
        this.socket = new Socket(ip, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }



    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }



    @Override
    public void registerSession(String username) throws NetworkException, UsernameAlreadyTakenException {

    }

    @Override
    public void createMatch(String username, FlightBoard.Type flightType, int numPlayers) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {

    }

    @Override
    public void connectToGame(String username, String matchId) throws NetworkException, FullLobbyException, UsernameAlreadyTakenException {

    }

    @Override
    public Map<String, List<String>> getAvailableMatches(String username) throws NetworkException {
        return null;
    }

    @Override
    public Map<String, String> getPlayers(String username){
        return null;
    }



    @Override
    public void sendAction(ClientAction action) throws NetworkException {

    }
}

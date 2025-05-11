package it.polimi.ingsw.gc11.controller.network.client.socket;

import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
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
    public List<String> getAvailableMatches(String username) throws NetworkException {
        return null;
    }



    @Override
    public List<ShipCard> getFreeShipCard(String username, int pos) throws NetworkException {
        return null;
    }

    @Override
    public void placeShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException {

    }

    @Override
    public void removeShipCard(String username, int x, int y) throws NetworkException {

    }

    @Override
    public void reserveShipCard(String username, ShipCard shipCard) throws NetworkException {

    }

    @Override
    public void useReservedShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException {

    }

    @Override
    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) throws NetworkException {
        return null;
    }

    @Override
    public void endBuilding(String username, int pos) throws NetworkException {

    }



    @Override
    public AdventureCard getAdventureCard(String username) throws NetworkException {
        return null;
    }

    @Override
    public void acceptAdventureCard(String username) throws NetworkException {

    }

    @Override
    public void declineAdventureCard(String username) throws NetworkException {

    }

    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage) throws NetworkException {

    }

    @Override
    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException {

    }

    @Override
    public void rewardDecision(String username, boolean decision) throws NetworkException {

    }

    @Override
    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException {

    }

    @Override
    public void getCoordinate(String username) throws NetworkException {

    }

    @Override
    public void handleShot(String username, Map<Battery, Integer> batteries) throws NetworkException {

    }

    @Override
    public void eliminateBatteries(String username, Map<Battery, Integer> batteries) throws NetworkException {

    }
}

package it.polimi.ingsw.gc11.controller.network.client.rmi;

import it.polimi.ingsw.gc11.controller.network.client.Client;
import it.polimi.ingsw.gc11.controller.network.server.rmi.ServerInterface;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;



public class ClientRMI extends Client implements ClientInterface {


    private final ServerInterface stub;



    /**
     * Constructs a client and connects it to the RMI registry
     *
     * @param ip   the IP address of the RMI server
     * @param port the port of the RMI registry
     */
    public ClientRMI(String ip, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        this.stub = (ServerInterface) registry.lookup("ServerInterface");
    }



    public void createMatch(String username, FlightBoard.Type flightType) throws RemoteException {
        this.clientSessionToken = stub.createMatch(username, flightType);
    }

    public void connectToGame(String username, String matchId) throws RemoteException {
        this.clientSessionToken = stub.connectPlayerToGame(username, matchId);
    }

    void startGame(String username, UUID token) throws RemoteException {
        stub.startGame(username, clientSessionToken);
    }
    void endGame(String username, UUID token) throws RemoteException {
        stub.endGame(username, clientSessionToken);
    }



    ShipCard getFreeShipCard(String username, UUID token, int pos) throws RemoteException{
        return stub.getFreeShipCard(username, token, pos);
    }

    void placeShipCard(String username, UUID token, ShipCard shipCard, int x, int y) throws RemoteException{
        stub.placeShipCard(username, token, shipCard, x, y);
    }

    void removeShipCard(String username, UUID token, int x, int y) throws RemoteException{
        stub.removeShipCard(username, token, x, y);
    }

    void reserveShipCard(String username, UUID token, ShipCard shipCard) throws RemoteException{
        stub.reserveShipCard(username, token, shipCard);
    }

    void useReservedShipCard(String username, UUID token, ShipCard shipCard, int x, int y) throws RemoteException{
        stub.useReservedShipCard(username, token, shipCard, x, y);
    }

    ArrayList<AdventureCard> observeMiniDeck(String username, UUID token, int numDeck) throws RemoteException{
        return stub.observeMiniDeck(username, token, numDeck);
    }

    void endBuilding(String username, UUID token, int pos) throws RemoteException{
        stub.endBuilding(username, token, pos);
    }



    AdventureCard getAdventureCard(String username, UUID token) throws RemoteException{
        return stub.getAdventureCard(username, clientSessionToken);
    }

    void acceptAdventureCard(String username, UUID token) throws RemoteException{
        stub.acceptAdventureCard(username, clientSessionToken);
    }

    void declineAdventureCard(String username, UUID token) throws RemoteException{
        stub.declineAdventureCard(username, clientSessionToken);
    }

    void killMembers(String username, UUID token, Map<HousingUnit, Integer> housingUsage) throws RemoteException{
        stub.killMembers(username, token, housingUsage);
    }

    void chosenMaterial(String username, UUID token, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws RemoteException{
        stub.chosenMaterial(username, token, storageMaterials);
    }

    void rewardDecision(String username, UUID token, boolean decision) throws RemoteException{
        stub.rewardDecision(username, token, decision);
    }

    void chooseFirePower(String username, UUID token, Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws RemoteException{
        stub.chooseFirePower(username, token, batteries, doubleCannons);
    }

    void getCoordinate(String username, UUID token) throws RemoteException{
        stub.getCoordinate(username, token);
    }

    void handleShot(String username, UUID token, Map<Battery, Integer> batteries) throws RemoteException{
        stub.handleShot(username, token, batteries);
    }

    void eliminateBatteries(String username, UUID token, Map<Battery, Integer> batteries) throws RemoteException{
        stub.eliminateBatteries(username, token, batteries);
    }
}

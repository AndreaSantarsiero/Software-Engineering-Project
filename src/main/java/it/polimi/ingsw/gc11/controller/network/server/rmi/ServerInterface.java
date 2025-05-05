package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;



public interface ServerInterface extends Remote {

    //creating, joining and handling matches
    UUID connectPlayerToGame(String username, String matchId) throws RemoteException;
    UUID createMatch(String username, FlightBoard.Type flightLevel) throws RemoteException;
    void startGame(String username, UUID token) throws RemoteException;
    void endGame(String username, UUID token) throws RemoteException;

    //Building phase methods
    ShipCard getFreeShipCard(String username, UUID token, int pos) throws RemoteException;
    void placeShipCard(String username, UUID token, ShipCard shipCard, int x, int y) throws RemoteException;
    void removeShipCard(String username, UUID token, int x, int y) throws RemoteException;
    void reserveShipCard(String username, UUID token, ShipCard shipCard) throws RemoteException;
    void useReservedShipCard(String username, UUID token, ShipCard shipCard, int x, int y) throws RemoteException;
    ArrayList<AdventureCard> observeMiniDeck(String username, UUID token, int numDeck) throws RemoteException;
    void endBuilding(String username, UUID token, int pos) throws RemoteException;

    //Adventure phase methods
    AdventureCard getAdventureCard(String username, UUID token) throws RemoteException;
    void acceptAdventureCard(String username, UUID token) throws RemoteException;
    void declineAdventureCard(String username, UUID token) throws RemoteException;
    void killMembers(String username, UUID token, Map<HousingUnit, Integer> housingUsage) throws RemoteException;
    void chosenMaterial(String username, UUID token, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws RemoteException;
    void rewardDecision(String username, UUID token, boolean decision) throws RemoteException;
    void chooseFirePower(String username, UUID token, Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws RemoteException;
    void getCoordinate(String username, UUID token) throws RemoteException;
    void handleShot(String username, UUID token, Map<Battery, Integer> batteries) throws RemoteException;
    void eliminateBatteries(String username, UUID token, Map<Battery, Integer> batteries) throws RemoteException;
}

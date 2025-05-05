package it.polimi.ingsw.gc11.controller.network.server.rmi;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;



public interface ServerInterface extends Remote {
    public UUID connectPlayerToGame(String username, String matchId) throws RemoteException;
    public UUID createMatch(FlightBoard.Type flightLevel, String username) throws RemoteException;

//    void startGame();
//    void endGame();
//    //Building phase methods
//    ShipCard getFreeShipCard(int pos);
    public void placeShipCard(String username, UUID token, ShipCard shipCard, int x, int y) throws RemoteException;
    public void removeShipCard(String username, UUID token, int x, int y) throws RemoteException;
    public void reserveShipCard(String username, UUID token, ShipCard shipCard) throws RemoteException;
    public void useReservedShipCard(String username, UUID token, ShipCard shipCard, int x, int y) throws RemoteException;
//    ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck);
//    void endBuilding(String username, int pos);
//
//    //Adventure phase methods
//    AdventureCard getAdventureCard(String username);
//    void acceptAdventureCard(String username);
//    void declineAdventureCard(String username);
//    void killMembers(String username, Map<HousingUnit, Integer> housingUsage);
//    void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials);
//    void rewardDecision(String username, boolean decision);
//    void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons);
//    void getCoordinate(String username);
//    void handleShot(String username, Map<Battery, Integer> batteries);
//    void eliminateBatteries(String username, Map<Battery, Integer> batteries);
}

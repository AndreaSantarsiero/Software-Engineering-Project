package it.polimi.ingsw.gc11.controller;

//CLASSE CHE RACCOGLIE TUTTI I METODI CHIAMABILI DALLA RETE
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public interface GameInterface {
    //IdlePhase methods
    void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException;

    //Building phase methods
    ShipCard getFreeShipCard(int pos);
    void placeShipCard(String username, ShipCard shipCard, int x, int y);
    void removeShipCard(String username, int x, int y);
    void reserveShipCard(String username, ShipCard shipCard);
    void useReservedShipCard(String username, ShipCard shipCard, int x, int y);
    ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck);
    void endBuilding(String username, int pos);

    //CheckPhase methods


    //Adventure phase methods
    AdventureCard getAdventureCard(String username);
    void acceptAdventureCard(String username);
    void declineAdventureCard(String username);
    void killMembers(String username, Map<HousingUnit, Integer> housingUsage);
    void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials);
    void rewardDecision(String username, boolean decision);
    void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons);
    void getCoordinate(String username);
    void handleShot(String username, Map<Battery, Integer> batteries);
    void eliminateBatteries(String username, Map<Battery, Integer> batteries);
}
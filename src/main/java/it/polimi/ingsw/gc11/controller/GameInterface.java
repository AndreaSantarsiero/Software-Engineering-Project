package it.polimi.ingsw.gc11.controller;

//CLASSE CHE RACCOGLIE TUTTI I METODI CHIAMABILI DALLA RETE
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameInterface {
    void connectPlayerToGame(String playerUsername);
    void startGame();
    void endGame();
    //Building phase methods
    ShipCard getFreeShipCard(int pos);
    void placeShipCard(String username, ShipCard shipCard, int x, int y);
    void removeShipCard(String username, int x, int y);
    void reserveShipCard(String username, ShipCard shipCard);
    void useReservedShipCard(String username, ShipCard shipCard, int x, int y);
    ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck);
    void endBuilding(String username, int pos);

    //Adventure phase methods
    AdventureCard getAdventureCard(String username);
    void acceptAdventureCard(String username);
    void declineAdventureCard(String username);
    void killMembers(String username, Map<HousingUnit, Integer> housingUsage);
    void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials);
    void useBatteries(String username, Map<Battery, Integer> batteryUsage);
    int getTotalAvailableBatteries(String username);
    int getEnginesPower (String username, int numBatteries);
    double getCannonsPower(String username, int numBatteries);
    void removeMaterials(String username, int x, int y, ArrayList<Material> materials);
    void selectPlanet(String username, int pos);
    void selectEnginePower(String username, int numBatteries);
    void selectCannonsPower(String username, int numBatteries);
    //... to add other methods
}

package it.polimi.ingsw.gc11.controller.network.client;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.*;



public abstract class Client {

    protected UUID clientSessionToken;



    public UUID checkConnection(){
        return clientSessionToken;
    }



    abstract public void createMatch(String username, FlightBoard.Type flightType) throws NetworkException;

    abstract public void connectToGame(String username, String matchId) throws NetworkException;

    abstract public void startGame(String username) throws NetworkException;

    abstract public void endGame(String username) throws NetworkException;



    abstract public ShipCard getFreeShipCard(String username, int pos) throws NetworkException;

    abstract public void placeShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException;

    abstract public void removeShipCard(String username, int x, int y) throws NetworkException;

    abstract public void reserveShipCard(String username, ShipCard shipCard) throws NetworkException;

    abstract public void useReservedShipCard(String username, ShipCard shipCard, int x, int y) throws NetworkException;

    abstract public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) throws NetworkException;

    abstract public void endBuilding(String username, int pos) throws NetworkException;



    abstract public AdventureCard getAdventureCard(String username) throws NetworkException;

    abstract public void acceptAdventureCard(String username) throws NetworkException;

    abstract public void declineAdventureCard(String username) throws NetworkException;

    abstract public void killMembers(String username, Map<HousingUnit, Integer> housingUsage) throws NetworkException;

    abstract public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) throws NetworkException;

    abstract public void rewardDecision(String username, boolean decision) throws NetworkException;

    abstract public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons) throws NetworkException;

    abstract public void getCoordinate(String username) throws NetworkException;

    abstract public void handleShot(String username, Map<Battery, Integer> batteries) throws NetworkException;

    abstract public void eliminateBatteries(String username, Map<Battery, Integer> batteries) throws NetworkException;
}

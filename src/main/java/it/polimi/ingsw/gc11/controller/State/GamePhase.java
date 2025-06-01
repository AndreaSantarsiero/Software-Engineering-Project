package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



//These are all Default Implementation
public abstract class GamePhase {

    //IdlePhase
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        throw new FullLobbyException("Cannot connect player to game in the current game phase: " + getPhaseName());
    }

    public void chooseColor(String username, String chosenColor) {
        throw new IllegalStateException("Can't choose player's color in the current game phase: " + getPhaseName());
    }


    //BuildingPhaseLv2
    public ShipCard getFreeShipCard(String username, int pos){
        throw new IllegalStateException("Can't get free ship card in the current game phase: " + getPhaseName());
    }

    public void releaseShipCard(String username, ShipCard shipCard){
        throw new IllegalStateException("Can't release held ship card in the current game phase: " + getPhaseName());
    }

    public ShipBoard placeShipCard(String username, ShipCard shipCard, int x, int y){
        throw new IllegalStateException("Can't place a ship card in the current game phase: " + getPhaseName());
    }

    public ShipBoard removeShipCard(String username, int x, int y) {
        throw new IllegalStateException("Can't remove ship card in the current game phase: " + getPhaseName());
    }

    public ShipBoard reserveShipCard(String username, ShipCard shipCard){
        throw new IllegalStateException("Can't reserve ship card in the current game phase: " + getPhaseName());
    }

    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, int x, int y) {
        throw new IllegalStateException("Can't use reserved ship card in the current game phase: " + getPhaseName());
    }

    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        throw new IllegalStateException("Can't observe mini deck in the current game phase: " + getPhaseName());
    }

    public void endBuilding(String username) {
        throw new IllegalStateException("Can't end building in the current game phase: " + getPhaseName());
    }

    public void endBuilding(String username, int pos) {
        throw new IllegalStateException("Can't end building in the current game phase: " + getPhaseName());
    }



    //CheckPhase
    public ShipBoard repairShip(String username, ArrayList<Integer> cardsToEliminateX,
                              ArrayList<Integer> cardsToEliminateY) {
        throw new IllegalStateException("Can't repair your ship in the current game phase: " + getPhaseName());
    }

    public void changePosition(String username, int pos){
        throw new IllegalStateException("Can't change your position in the current game phase: " + getPhaseName());
    }




    //AdventurePhase
    public AdventureCard getAdventureCard(String username){
        throw new IllegalStateException("Can't get a new Adventure Card in the current game phase: " + getPhaseName());
    }

    public void acceptAdventureCard(String username){
        throw new IllegalStateException("Can't accept an adventure card in the current game phase: " + getPhaseName());
    }

    public void declineAdventureCard(String username){
        throw new IllegalStateException("Can't decline an adventure card in the current game phase: " + getPhaseName());
    }

    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        throw new IllegalStateException("Can't kill members of an adventure card in the current game phase: " + getPhaseName());
    }

    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        throw new IllegalStateException("Can't choose materials of an adventure card in the current game phase: " + getPhaseName());
    }

    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        throw new IllegalStateException("Can't choose fire powers of an adventure card in the current game phase: " + getPhaseName());
    }

    public Player rewardDecision(String username, boolean decision){
        throw new IllegalStateException("Can't make the reward decision of the adventure card in the current game phase: " + getPhaseName());
    }

    public Hit getCoordinate(String username){
        throw new IllegalStateException("Can't get a coordinate of the adventure card in the current game phase: " + getPhaseName());
    }

    public Player handleShot(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't handle shot of the adventure card in the current game phase: " + getPhaseName());
    }

    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't elimine batteries of the adventure card in the current game phase: " + getPhaseName());
    }

    public Player landOnPlanet(String username, int numPlanet){
        throw new IllegalStateException("Can't land on a planet in the current game phase: " + getPhaseName());
    }

    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        throw new IllegalStateException("Can't choose engine power in the current game phase: " + getPhaseName());
    }

    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        throw new IllegalStateException("Can't meteor hit in the current game phase: " + getPhaseName());
    }



    public abstract String getPhaseName();
}

package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
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
        throw new FullLobbyException("Cannot connect player to game in the current game phase");
    }



    //BuildingPhase
    public ShipCard getFreeShipCard(GameModel gameModel, String username, int pos){
        throw new IllegalStateException("Can't get free ship card in the current game phase.");
    }

    public void releaseShipCard(GameModel gameModel, String username, ShipCard shipCard){
        throw new IllegalStateException("Can't release held ship card in the current game phase.");
    }

    public ShipBoard placeShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y){
        throw new IllegalStateException("Can't place a ship card in the current game phase.");
    }

    public ShipBoard removeShipCard(GameModel gameModel, String username, int x, int y) {
        throw new IllegalStateException("Can't remove ship card in the current game phase.");
    }

    public ShipBoard reserveShipCard(GameModel gameModel, String username, ShipCard shipCard){
        throw new IllegalStateException("Can't reserve ship card in the current game phase.");
    }

    public ShipBoard useReservedShipCard(GameModel gameModel, String username, ShipCard shipCard, int x, int y) {
        throw new IllegalStateException("Can't use reserved ship card in the current game phase.");
    }

    public ArrayList<AdventureCard> observeMiniDeck(GameModel gameModel, String username, int numDeck) {
        throw new IllegalStateException("Can't observe mini deck in the current game phase.");
    }

    public void endBuilding(String username, GameModel gameModel, int pos) {
        throw new IllegalStateException("Can't end building in the current game phase.");
    }

    public void goToCheckPhase(GameContext context) throws IllegalStateException{
        throw new IllegalStateException("Can't go to check state in the current game phase.");
    }



    //AdventurePhase
    public AdventureCard getAdventureCard(String username){
        throw new IllegalStateException("Can't get a new Adventure Card in the current game phase.");
    }

    public void acceptAdventureCard(String username){
        throw new IllegalStateException("Can't accept an adventure card in the current game phase.");
    }

    public void declineAdventureCard(String username){
        throw new IllegalStateException("Can't decline an adventure card in the current game phase.");
    }

    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        throw new IllegalStateException("Can't kill members of an adventure card in the current game phase.");
    }

    public void chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        throw new IllegalStateException("Can't choose materials of an adventure card in the current game phase.");
    }

    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        throw new IllegalStateException("Can't choose fire powers of an adventure card in the current game phase.");
    }

    public void rewardDecision(String username, boolean decision){
        throw new IllegalStateException("Can't make the reward decision of the adventure card in the current game phase.");
    }

    public void getCoordinate(String username){
        throw new IllegalStateException("Can't get a coordinate of the adventure card in the current game phase.");
    }

    public void handleShot(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't handle shot of the adventure card in the current game phase.");
    }

    public void useBatteries(String username, Map<Battery, Integer> batteries){
        throw new IllegalStateException("Can't elimine batteries of the adventure card in the current game phase.");
    }

    public void landOnPlanet(String username, int numPlanet){
        throw new IllegalStateException("Can't land on a planet in the current game phase.");
    }

    public void chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        throw new IllegalStateException("Can't choose engine power in the current game phase.");
    }

    public void meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        throw new IllegalStateException("Can't meteor hit in the current game phase.");
    }
}

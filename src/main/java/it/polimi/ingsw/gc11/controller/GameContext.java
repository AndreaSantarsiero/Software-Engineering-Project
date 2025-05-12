package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



//Controller of a specific gameModel and multiple gameView
public class GameContext implements GameInterface {
    private final GameModel gameModel;
    private final String matchID;
    private GamePhase phase;

    public GameContext(FlightBoard.Type flightType, int numPlayers) {
        this.gameModel = new GameModel(numPlayers);
        this.gameModel.setLevel(flightType);
        this.matchID = gameModel.getID();
        this.phase = new IdlePhase(this);
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public String getMatchID() {
        return matchID;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }



    //IdlePhase
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        phase.connectPlayerToGame(playerUsername);
    }



    //BuildingPhase
    public ShipCard getFreeShipCard(int pos){
        return phase.getFreeShipCard(this.gameModel, pos);
    }

    public ShipBoard placeShipCard(String username, ShipCard shipCard, int x, int y){
        return phase.placeShipCard(gameModel, username, shipCard, x, y);
    }

    public ShipBoard removeShipCard(String username, int x, int y) {
        return phase.removeShipCard(gameModel, username, x, y);
    }

    public ShipBoard reserveShipCard(String username, ShipCard shipCard) {
        return phase.reserveShipCard(gameModel, username, shipCard);
    }

    public ShipBoard useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        return phase.useReservedShipCard(gameModel, username, shipCard, x, y);
    }

    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return phase.observeMiniDeck(gameModel, username, numDeck);
    }

    public void endBuilding(String username, int pos){
        phase.endBuilding(username, gameModel, pos);
    }

    public void goToCheckPhase() throws IllegalStateException {
        phase.goToCheckPhase(this);
    }

    public void checkAllShipBoards(){
    }



    //AdventurePhase
    public AdventureCard getAdventureCard(String username) {
        return phase.getAdventureCard(username);
    }

    public void acceptAdventureCard(String username) {
        phase.acceptAdventureCard(username);
    }

    public void declineAdventureCard(String username) {
        phase.declineAdventureCard(username);
    }

    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        phase.killMembers(username, housingUsage);
    }

    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        phase.chosenMaterial(username, storageMaterials);
    }

    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        phase.chooseFirePower(username, batteries, doubleCannons);
    }

    public void rewardDecision(String username, boolean decision){
        phase.rewardDecision(username, decision);
    }

    public void getCoordinate(String username){
        phase.getCoordinate(username);
    }

    public void handleShot(String username, Map<Battery, Integer> batteries){
        phase.handleShot(username, batteries);
    }

    public void eliminateBatteries(String username, Map<Battery, Integer> batteries){
        phase.eliminateBatteries(username, batteries);
    }

    public void landOn(String username, int numPlanet){
        phase.landOn(username, numPlanet);
    }

    public void chosenEnginePower(String username, Map<Battery, Integer> Batteries){
        phase.chosenEnginePower(username, Batteries);
    }

    public void meteorHit(String username, Map<Battery, Integer> batteries, Cannon cannon){
        phase.meteorHit(username, batteries, cannon);
    }
}

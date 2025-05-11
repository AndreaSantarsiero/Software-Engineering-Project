package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
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

    public void placeShipCard(String username, ShipCard shipCard, int x, int y){
        gameModel.getPlayerShipBoard(username).addShipCard(shipCard, x, y);
    }

    public void removeShipCard(String username, int x, int y) {
        phase.removeShipCard(gameModel, username, x, y);
    }

    public void reserveShipCard(String username, ShipCard shipCard) {
        phase.reserveShipCard(gameModel, username, shipCard);
    }

    public void useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        phase.useReservedShipCard(gameModel, username, shipCard, x, y);
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
        if (username == null) {
            throw new NullPointerException();
        }
        try {
            phase.killMembers(username, housingUsage);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.chosenMaterial(username, storageMaterials);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }


    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.chooseFirePower(username, batteries, doubleCannons);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void rewardDecision(String username, boolean decision){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.rewardDecision(username, decision);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void getCoordinate(String username){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.getCoordinate(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void handleShot(String username, Map<Battery, Integer> batteries){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.handleShot(username, batteries);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void eliminateBatteries(String username, Map<Battery, Integer> batteries){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.eliminateBatteries(username, batteries);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void landOn(String username, int numPlanet){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.landOn(username, numPlanet);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void chosenEnginePower(String username, Map<Battery, Integer> Batteries){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.chosenEnginePower(username, Batteries);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void meteorHit(String username, Map<Battery, Integer> batteries, Cannon cannon){
        if(username == null) {
            throw new NullPointerException();
        }

        try{
            phase.meteorHit(username, batteries, cannon);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}

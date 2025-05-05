package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.HandleHit;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class GameContext {
    //Controller of a specific gameModel and multiple gameView

    private final GameModel gameModel;
    private final String matchID;
    private GamePhase phase;
    private final ArrayList<PlayerContext> playerContexts;



    public GameContext(FlightBoard.Type flightType) {
        this.gameModel = new GameModel();
        this.gameModel.setLevel(flightType);
        this.playerContexts = new ArrayList<>();
        this.matchID = gameModel.getID();
        // Initial state
        this.phase = new IdlePhase();
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


    public void addPlayerContext(String playerUsername) throws FullLobbyException {
        if (playerContexts.size() < 4) {
            this.playerContexts.add(new PlayerContext(playerUsername));
        }
        else {
            throw new FullLobbyException("The lobby you're trying to join is full at the moment.");
        }
    }


    public void connectPlayerToGame(String playerUsername) {
        try {
            this.addPlayerContext(playerUsername);
        }
        catch (FullLobbyException e) {
            System.out.println(e.getMessage());
        }
    }


    public void startGame() {
        try {
            phase.startGame(this);
        }
        catch (GameAlreadyStartedException e) {
            System.out.println(e.getMessage());
        }
    }


    public void endGame() {
        try{
            this.phase.endGame(this);
        }
        catch (Exception e) {
            System.out.println("Can't end game in this state");
        }
    }


    public ShipCard getFreeShipCard(int pos) throws IndexOutOfBoundsException {
        if (pos < 0) {
            throw new IndexOutOfBoundsException();
        }
        try{
            return this.phase.getFreeShipCard(this.gameModel, pos);
        }
        catch (IllegalStateException e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void placeShipCard(String username, ShipCard shipCard, int x, int y)
            throws NullPointerException, IllegalArgumentException {
        if (shipCard == null) {
            throw new NullPointerException();
        }
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        try{
            this.gameModel.getPlayerShipBoard(username).addShipCard(shipCard, x, y);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }


    public void removeShipCard(String username, int x, int y) throws IllegalArgumentException {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        try {
            this.phase.removeShipCard(this.gameModel, username, x, y);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void reserveShipCard(String username, ShipCard shipCard) throws NullPointerException {
        if (shipCard == null) {
            throw new NullPointerException();
        }
        try {
            this.gameModel.getPlayerShipBoard(username).reserveShipCard(shipCard);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }


    public void useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        if (shipCard == null) {
            throw new NullPointerException();
        }
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        try {
            this.gameModel.getPlayerShipBoard(username).useReservedShipCard(shipCard, x, y);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<AdventureCard> observeMiniDeck(String username, int numDeck) {
        return null;
    }


    public void endBuilding(String username, int pos) {

    }

    public void goToCheckPhase(){
        try {
            phase.goToCheckPhase(this);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkAllShipBoards(){
    }



    public AdventureCard getAdventureCard(String username) {
        if (username == null) {
            throw new NullPointerException();
        }
        try {
            return phase.getAdventureCard(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void acceptAdventureCard(String username) {
        if (username == null) {
            throw new NullPointerException();
        }
        try {
            phase.acceptAdventureCard(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void declineAdventureCard(String username) {
        if (username == null) {
            throw new NullPointerException();
        }
        try {
            phase.declineAdventureCard(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
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
}

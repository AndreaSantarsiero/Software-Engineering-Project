package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import java.util.ArrayList;

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

    public String getMatchID() {
        return matchID;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public void nextPhase() {
        phase.nextPhase(this);
    }

    public GamePhase getPhase() {
        return phase;
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
            System.out.println("Can't get free ship card in the current game phase.");
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
            System.out.println("Can't place a ship card in the current game phase.");
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
            System.out.println("Can't remove ship card in the current game phase.");
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
            System.out.println("Can't reserve ship card in the current game phase.");
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
            System.out.println("Can't use reserved ship card in the current game phase.");
        }
    }

    public void goToCheckPhase(){
        try {
            phase.goToCheckPhase(this);
        }
        catch (IllegalStateException e) {
            System.out.println("Can't go to check state in the current game phase.");
        }
    }

    public void checkAllShipBoards(){
    }

}

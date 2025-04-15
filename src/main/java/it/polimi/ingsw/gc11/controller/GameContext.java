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
    private GamePhase state;
    private final ArrayList<PlayerContext> playerContexts;

    public GameContext(FlightBoard.Type flightType) {
        this.gameModel = new GameModel();
        this.gameModel.setLevel(flightType);
        this.playerContexts = new ArrayList<>();
        this.matchID = gameModel.getID();
        // Initial state
        this.state = new IdleState();
    }

    public String getMatchID() {
        return matchID;
    }

    public void setState(GamePhase state) {
        this.state = state;
    }

    public void nextState() {
        state.nextState(this);
    }

    public GamePhase getState() {
        return state;
    }

    public void addPlayerContext(String playerUsername) throws FullLobbyException {
        if (playerContexts.size() < 4) {
            this.playerContexts.add(new PlayerContext(playerUsername));
        }
        else {
            throw new FullLobbyException("The lobby you're trying to join is full at the moment.");
        }
    }

    public void startGame() throws GameAlreadyStartedException {
        if (this.state instanceof IdleState) {
            this.nextState();
        }
        else {
            throw new GameAlreadyStartedException("Game ID " + matchID + " is already running");
        }
    }

    public void endGame() {
        if (this.state instanceof AdventureState) {
            this.nextState();
        }
        else {
            //states checking
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

    public ShipCard getFreeShipCard(int pos) throws IndexOutOfBoundsException {
        if (pos < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (state instanceof BuildingState){
            return gameModel.getFreeShipCard(pos);
        }
        else {
            throw new IllegalStateException();
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
        if (state instanceof BuildingState){
            this.gameModel.getPlayerShipBoard(username).addShipCard(shipCard, x, y);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public void removeShipCard(String username, int x, int y) throws IllegalArgumentException {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }

        if (state instanceof BuildingState){
            try {
                this.gameModel.getPlayerShipBoard(username).removeShipCard(x, y);
            }
            catch (IllegalArgumentException e) {
                //can't remove shipcard
            }
        }
        else {
            throw new IllegalStateException();
        }
    }

    public void reserveShipCard(String username, ShipCard shipCard) throws NullPointerException {
        if (shipCard == null) {
            throw new NullPointerException();
        }
        if (state instanceof BuildingState){
            try {
                this.gameModel.getPlayerShipBoard(username).reserveShipCard(shipCard);
            }
            catch (IllegalStateException e) {
                //can't reserve shipcard
            }
        }
        else {
            throw new IllegalStateException();
        }
    }

    public void useReservedShipCard(String username, ShipCard shipCard, int x, int y){
        if (shipCard == null) {
            throw new NullPointerException();
        }
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        if (state instanceof BuildingState){
            try {
                this.gameModel.getPlayerShipBoard(username).useReservedShipCard(shipCard, x, y);
            }
            catch (IllegalStateException e) {
                //can't use reserved shipcard
            }
        }
        else {
            throw new IllegalStateException();
        }
    }

    public void goToCheckPhase(){
        if (this.state instanceof BuildingState) {
            this.nextState();
        }
        else {
            throw new IllegalStateException();
        }
    }

    public void checkAllShipBoards(){
    }

}

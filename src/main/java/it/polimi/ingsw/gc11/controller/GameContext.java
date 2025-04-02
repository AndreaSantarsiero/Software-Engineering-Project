package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;


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

    public void getShipCard(String gameID){
        if (this.state instanceof BuildingState) {

        }
    }

    public void endGame(String gameID) {
        if (this.state instanceof AdventureState) {
            this.nextState();
        }
        else {
            //states check
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


}

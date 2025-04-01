package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.*;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import java.util.HashMap;

public class ServerController {
    private HashMap<String,GameContext> games; //multiple games version

    public ServerController() {
        this.games = new HashMap<>();
    }


    //Create new match and specifies the level, return its ID
    public String createNewMatch(FlightBoard.Type flightType) {
        GameContext newGameContext = new GameContext();
        newGameContext.getGameModel().setLevel(flightType);
        String id = newGameContext.getGameModel().getID();
        this.games.put(id,newGameContext);
        return id;
    }

    public void startGame(String gameID) throws GameAlreadyStartedException {
        GameContext selectedGame = this.games.get(gameID);
        if (selectedGame == null) {
            throw new IllegalArgumentException("Game ID " + gameID + " not found");
        }
        if (selectedGame.getState() instanceof IdleState) {
            selectedGame.nextState();
        }
        else {
            throw new GameAlreadyStartedException("Game ID " + gameID + " is already running");
        }

    }

    public void endGame(String gameID) {
        GameContext selectedGame = this.games.get(gameID);
        if (selectedGame == null) {
            throw new IllegalArgumentException("Game ID " + gameID + " not found");
        }
        if (selectedGame.getState() instanceof AdventureState) {
            selectedGame.nextState();
        }
        else {

        }
    }

    public void connectPlayer(){

    }
}

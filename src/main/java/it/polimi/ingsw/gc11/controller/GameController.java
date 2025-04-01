package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.controller.State.GameContext;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.gc11.model.FlightBoard;


import java.util.ArrayList;


public class GameController {
    ArrayList<GameContext> games; //multiple games version

    public GameController() {
        this.games = new ArrayList<>();
    }

    //Create new match and return its ID
    public String createNewMatch(FlightBoard.Type flightType) {
        GameContext newGameContext = new GameContext();
        newGameContext.getGameModel().setLevel(flightType);
        this.games.add(newGameContext);
        return newGameContext.getGameModel().getID();
    }

    public void startGame(String gameID) throws GameAlreadyStartedException {
        GameContext selectedGame = null;
        for (GameContext game : games) {
            if (game.getGameModel().getID().equals(gameID)) {
                selectedGame = game;
                break;
            }
        }
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
        GameContext selectedGame = null;
        for (GameContext game : games) {
            if (game.getGameModel().getID().equals(gameID)) {
                selectedGame = game;
                break;
            }
        }
        if (selectedGame == null) {
            throw new IllegalArgumentException("Game ID " + gameID + " not found");
        }
        /*
        if (selectedGame.getState() instanceof AdventureState)) {
            this.gameState.get(selectedGame).nextState();
        }
        else {
             to check invalid state
        }
        */
    }



}

package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.view.GameView;



public class PlayerContext {

    String username;
    private final GameView gameView;


    public PlayerContext(String username) {
        this.username = username;
        this.gameView = new GameView();
    }
}

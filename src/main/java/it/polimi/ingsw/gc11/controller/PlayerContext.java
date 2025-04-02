package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.view.GameView;

public class PlayerContext {
    String userName;
    //private Client clientConnection;
    private final GameView gameView;

    public PlayerContext(String username) {
        this.userName = username;
        this.gameView = new GameView();
    }
}

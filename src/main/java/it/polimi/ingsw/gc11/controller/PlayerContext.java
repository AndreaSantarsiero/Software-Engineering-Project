package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.GameView;

public class PlayerContext {
    private final Player player;
    private final GameView gameView;

    public PlayerContext(String username) {
        player = new Player(username);
        gameView = new GameView();
    }
}

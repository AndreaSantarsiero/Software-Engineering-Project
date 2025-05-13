package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Player;

public class UpdateEnemyProfileAction extends ServerAction{
    private final Player player;

    public UpdateEnemyProfileAction(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void execute() {

    }
}

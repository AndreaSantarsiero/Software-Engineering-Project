package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Player;

public class UpdatePlayerProfileAction extends ServerAction{
    private final Player player;

    public UpdatePlayerProfileAction(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void execute() {

    }
}

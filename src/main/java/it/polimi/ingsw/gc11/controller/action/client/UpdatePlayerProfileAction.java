package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Player;

public class UpdatePlayerProfileAction extends ServerAction{
    private final Player player;
    private final int positionOnFlightBoard;

    public UpdatePlayerProfileAction(Player player, int positionOnFlightBoard) {
        this.player = player;
        this.positionOnFlightBoard = positionOnFlightBoard;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void execute() {

    }
}

package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class AcceptAbandonedShipAction extends ClientAction {
    public AcceptAbandonedShipAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.acceptAdventureCard(getUsername());
    }
}


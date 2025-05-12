package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class DeclineAbandonedShipAction extends ClientAction {
    public DeclineAbandonedShipAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.declineAdventureCard(getUsername());
    }
}


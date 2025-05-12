package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class AcceptAdventureCardAction extends ClientAction {
    public AcceptAdventureCardAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.acceptAdventureCard(getUsername());
    }
}




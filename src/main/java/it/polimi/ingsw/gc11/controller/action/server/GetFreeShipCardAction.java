package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class GetFreeShipCardAction extends ClientAction {
    private int pos;

    public GetFreeShipCardAction(String username, int pos) {
        super(username);
        this.pos = pos;
    }

    @Override
    public void execute(GameContext context) {
        context.getFreeShipCard(username, pos);
    }
}

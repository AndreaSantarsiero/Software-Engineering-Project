package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class RemoveShipCardAction extends ClientAction {
    private int x;
    private int y;

    public RemoveShipCardAction(String username, int x, int y) {
        super(username);
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(GameContext cxt){
        cxt.removeShipCard(username, x, y);
    }
}

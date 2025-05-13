package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class UseReservedShipCardAction extends ClientAction{
    private ShipCard shipCard;
    private int x;
    private int y;

    public UseReservedShipCardAction(String username, ShipCard shipCard, int x, int y) {
        super(username);
        this.shipCard = shipCard;
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(GameContext context) {
        context.useReservedShipCard(username, shipCard, x, y);
    }
}

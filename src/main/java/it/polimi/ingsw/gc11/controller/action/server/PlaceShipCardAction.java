package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class PlaceShipCardAction extends ClientAction {
    private int x;
    private int y;
    private ShipCard shipCard;

    public PlaceShipCardAction(String username, int x, int y, ShipCard shipCard) {
        super(username);
        this.x = x;
        this.y = y;
        this.shipCard = shipCard;
    }

    public PlaceShipCardAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        context.placeShipCard(username, shipCard, x, y);
    }
}

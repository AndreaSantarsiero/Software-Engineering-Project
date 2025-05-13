package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class ReserveShipCardAction extends ClientAction {
    private ShipCard shipCard;

    public ReserveShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    @Override
    public void execute(GameContext context) {
        context.reserveShipCard(username, shipCard);
    }
}

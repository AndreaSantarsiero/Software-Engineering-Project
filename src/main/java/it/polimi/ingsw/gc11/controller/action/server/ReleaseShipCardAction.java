package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class ReleaseShipCardAction extends ClientAction{
    private ShipCard shipCard;

    public ReleaseShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    @Override
    public void execute(GameContext context){
        context.releaseShipCard(username, shipCard);
    }
}

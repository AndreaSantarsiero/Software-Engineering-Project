package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class SendFreeShipCardAction extends ServerAction{
    private final ShipCard shipCard;
    public SendFreeShipCardAction(ShipCard shipCard) {
        this.shipCard = shipCard;
    }

    public ShipCard getShipCard() {
        return shipCard;
    }

    @Override
    public void execute() {

    }
}

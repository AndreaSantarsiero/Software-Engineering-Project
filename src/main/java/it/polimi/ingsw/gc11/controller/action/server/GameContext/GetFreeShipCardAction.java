package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendFreeShipCardAction;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.model.shipcard.StructuralModule;



public class GetFreeShipCardAction extends ClientGameAction {

    private final ShipCard shipCard;


    public GetFreeShipCardAction(String username, ShipCard shipCard) {
        super(username);
        if(shipCard.equals(new StructuralModule("covered", ShipCard.Connector.SINGLE, ShipCard.Connector.NONE, ShipCard.Connector.NONE, ShipCard.Connector.NONE))){
            shipCard = null;
        }
        this.shipCard = shipCard;
    }


    @Override
    public void execute(GameContext context) {
        try {
            ShipCard heldShipCard = context.getFreeShipCard(username, shipCard);
            SendFreeShipCardAction response = new SendFreeShipCardAction(heldShipCard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

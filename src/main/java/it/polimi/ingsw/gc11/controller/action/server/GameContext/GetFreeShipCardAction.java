package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendFreeShipCardAction;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;



public class GetFreeShipCardAction extends ClientGameAction {

    private int pos;


    public GetFreeShipCardAction(String username, int pos) {
        super(username);
        this.pos = pos;
    }


    @Override
    public void execute(GameContext context) {
        try {
            ShipCard shipCard = context.getFreeShipCard(username, pos);
            SendFreeShipCardAction response = new SendFreeShipCardAction(shipCard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

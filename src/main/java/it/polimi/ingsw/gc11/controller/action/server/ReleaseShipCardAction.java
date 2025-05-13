package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class ReleaseShipCardAction extends ClientAction{
    private ShipCard shipCard;

    public ReleaseShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    @Override
    public void execute(GameContext context){
        try{
            context.releaseShipCard(username, shipCard);
            NotifySuccessAction notifySuccessAction = new NotifySuccessAction();
            context.sendAction(username, notifySuccessAction);
        }catch(Exception e){
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exceptionAction);
        }
    }
}

package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class ReleaseShipCardAction extends ClientGameAction {
    private ShipCard shipCard;

    public ReleaseShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    @Override
    public void execute(GameContext context){
        try{
            context.releaseShipCard(username, shipCard);
            NotifySuccessAction response = new NotifySuccessAction();
            context.sendAction(username, response);
        }catch(Exception e){
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exceptionAction);
        }
    }
}

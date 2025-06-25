package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateRemoveShipCardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;



public class RemoveShipCardAction extends ClientGameAction {

    private final int x;
    private final int y;


    public RemoveShipCardAction(String username, int x, int y) {
        super(username);
        this.x = x;
        this.y = y;
    }


    @Override
    public void execute(GameContext context){
        try {
            ShipBoard shipBoard = context.removeShipCard(username, x, y);
            ShipCard heldShipCard = context.getGameModel().getHeldShipCard(username);
            UpdateRemoveShipCardAction response = new UpdateRemoveShipCardAction(shipBoard, heldShipCard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

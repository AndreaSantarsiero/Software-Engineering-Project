package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class ReserveShipCardAction extends ClientGameAction {
    private ShipCard shipCard;

    public ReserveShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    @Override
    public void execute(GameContext context) {
        try {
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            ShipBoard shipBoard = context.reserveShipCard(username, shipCard);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

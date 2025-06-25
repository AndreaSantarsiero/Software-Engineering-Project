package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class UseReservedShipCardAction extends ClientGameAction {
    private ShipCard shipCard;
    private int x;
    private int y;

    public UseReservedShipCardAction(String username, ShipCard shipCard, int x, int y) {
        super(username);
        this.shipCard = shipCard;
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(GameContext context) {
        try {
            ShipCard.Orientation orientation = shipCard.getOrientation();
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            ShipBoard shipBoard = context.useReservedShipCard(username, shipCard, orientation, x, y);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

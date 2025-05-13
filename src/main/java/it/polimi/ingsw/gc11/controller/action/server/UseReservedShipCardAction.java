package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class UseReservedShipCardAction extends ClientAction{
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
            ShipBoard shipBoard = context.useReservedShipCard(username, shipCard, x, y);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

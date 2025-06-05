package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class PlaceShipCardAction extends ClientGameAction {
    private int x;
    private int y;
    private ShipCard shipCard;

    public PlaceShipCardAction(String username, int x, int y, ShipCard shipCard) {
        super(username);
        this.x = x;
        this.y = y;
        this.shipCard = shipCard;
    }

    public PlaceShipCardAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        try {
            ShipCard.Orientation orientation = shipCard.getOrientation();
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            ShipBoard shipBoard = context.placeShipCard(username, shipCard, orientation, x, y);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

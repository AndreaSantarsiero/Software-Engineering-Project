package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;

public class RemoveShipCardAction extends ClientGameAction {
    private int x;
    private int y;

    public RemoveShipCardAction(String username, int x, int y) {
        super(username);
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(GameContext cxt){
        try {
            ShipBoard shipBoard = cxt.removeShipCard(username, x, y);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            cxt.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            cxt.sendAction(username, exception);
        }
    }
}

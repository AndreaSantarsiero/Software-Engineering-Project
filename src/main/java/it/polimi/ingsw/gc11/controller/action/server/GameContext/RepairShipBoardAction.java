package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;

import java.util.ArrayList;

public class RepairShipBoardAction extends ClientGameAction {
    private boolean repaired;
    private ArrayList<Integer> cardToEliminateX;
    private ArrayList<Integer> cardToEliminateY;

    private RepairShipBoardAction(String username, boolean repaired, ArrayList<Integer> cardToEliminateX, ArrayList<Integer> cardToEliminateY) {
        super(username);
        this.cardToEliminateX = cardToEliminateX;
        this.cardToEliminateY = cardToEliminateY;
    }

    @Override
    public void execute(GameContext context) {
        try {
            ShipBoard shipBoard = context.repairShip(username, cardToEliminateX, cardToEliminateY);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

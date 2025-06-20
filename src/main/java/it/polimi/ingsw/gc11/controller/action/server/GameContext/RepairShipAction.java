package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.List;



public class RepairShipAction extends ClientGameAction {

    private final List<Integer> cardsToEliminateX;
    private final List<Integer> cardsToEliminateY;


    public RepairShipAction(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {
        super(username);
        this.cardsToEliminateX = cardsToEliminateX;
        this.cardsToEliminateY = cardsToEliminateY;
    }


    @Override
    public void execute(GameContext context) {
        try {
            ShipBoard shipBoard = context.repairShip(username, cardsToEliminateX, cardsToEliminateY);
            if(context.getPhase().getPhaseName().equals("CheckPhase")){
                UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
                context.sendAction(username, response);
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

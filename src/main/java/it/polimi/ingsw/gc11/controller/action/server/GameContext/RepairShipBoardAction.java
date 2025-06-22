package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import java.util.List;



public class RepairShipBoardAction extends ClientGameAction {

    private final List<Integer> cardsToEliminateX;
    private final List<Integer> cardsToEliminateY;


    public RepairShipBoardAction(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {
        super(username);
        this.cardsToEliminateX = cardsToEliminateX;
        this.cardsToEliminateY = cardsToEliminateY;
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.repairShip(username, cardsToEliminateX, cardsToEliminateY);
            //success action sent by the controller
        }
        catch (IllegalArgumentException ignored) {}
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

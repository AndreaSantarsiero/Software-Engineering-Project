package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.Map;



public class GetEnemiesShipBoardAction extends ClientGameAction {

    public GetEnemiesShipBoardAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            Map<String, ShipBoard> enemiesShipBoard = context.getPlayersShipBoard();
            enemiesShipBoard.remove(this.username);

            for(Map.Entry<String, ShipBoard> entry : enemiesShipBoard.entrySet()) {
                UpdateEnemyShipBoardAction response = new UpdateEnemyShipBoardAction(entry.getValue(), entry.getKey());
                context.sendAction(username, response);
            }

            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

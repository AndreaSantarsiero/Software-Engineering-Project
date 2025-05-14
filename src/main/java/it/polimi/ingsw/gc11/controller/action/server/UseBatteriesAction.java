package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;



public class UseBatteriesAction extends ClientAction {
    private final Map<Battery, Integer> batteries;

    public UseBatteriesAction(String username, Map<Battery, Integer> batteries) {
        super(username);
        this.batteries = batteries;
    }

    @Override
    public void execute(GameContext context) {
        try{
            Player player = context.useBatteries(getUsername(), batteries);

            if(player.getUsername().equals(username)){
                UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
                context.sendAction(username, response);
            }
            else{
                UpdateEnemyProfileAction response1 = new UpdateEnemyProfileAction(player);
                context.sendAction(username, response1);

                UpdateEnemyShipBoardAction response2 = new UpdateEnemyShipBoardAction(player.getShipBoard(), player.getUsername());
                context.sendAction(username, response2);
            }


        } catch (Exception e) {
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(getUsername());
            context.sendAction(username, exceptionAction);
        }

    }
}


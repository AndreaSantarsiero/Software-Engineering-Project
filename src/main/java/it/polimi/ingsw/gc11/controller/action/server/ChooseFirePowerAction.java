package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;

public class ChooseFirePowerAction extends ClientAction {
    private final Map<Battery, Integer> batteries;
    private final List<Cannon> cannons;

    public ChooseFirePowerAction(String username, Map<Battery, Integer> batteries, List<Cannon> cannons) {
        super(username);
        this.batteries = batteries;
        this.cannons = cannons;
    }

    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.chooseFirePower(username, batteries, cannons);

            for(Player p : context.getGameModel().getPlayers()) {
                if(player.getUsername().equals(p.getUsername())) {
                    UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
                    context.sendAction(username, response);
                }
                else if(!p.isAbort()){
                    UpdatePlayerProfileAction response1 = new UpdatePlayerProfileAction(player, context.getGameModel().getPositionOnBoard(p.getUsername()));
                    context.sendAction(p.getUsername(), response1);

                    UpdateEnemyShipBoardAction response2 = new UpdateEnemyShipBoardAction(player.getShipBoard(), player.getUsername());
                    context.sendAction(p.getUsername(), response2);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

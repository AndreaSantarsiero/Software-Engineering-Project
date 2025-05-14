package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.Map;

public class MeteorDefenseAction extends ClientAction {

    private final Map<Battery, Integer> batteries;
    private final Cannon cannon;

    public MeteorDefenseAction(String username, Map<Battery, Integer> batteries, Cannon cannon) {
        super(username);
        this.batteries = batteries;
        this.cannon = cannon;
    }

    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.meteorDefense(getUsername(), batteries, cannon);

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

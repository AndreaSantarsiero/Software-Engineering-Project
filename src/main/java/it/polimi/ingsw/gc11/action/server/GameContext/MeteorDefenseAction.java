package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.Map;



public class MeteorDefenseAction extends ClientGameAction {

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
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, currentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, currentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

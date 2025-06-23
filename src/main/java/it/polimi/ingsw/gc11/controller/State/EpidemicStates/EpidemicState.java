package it.polimi.ingsw.gc11.controller.State.EpidemicStates;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import java.util.HashMap;
import java.util.Map;



public class EpidemicState extends AdventureState {

    public EpidemicState(AdventurePhase advContext) {
        super(advContext);
        GameContext context = advContext.getGameContext();

        try{
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                player.getShipBoard().epidemic();
            }


            //sending updates
            String currentPlayer = context.getCurrentPlayer().getUsername();
            Map<String, Player> enemies = new HashMap<>();
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                enemies.put(player.getUsername(), player);
            }

            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                enemies.remove(player.getUsername());
                UpdateEverybodyProfileAction response = new UpdateEverybodyProfileAction(player, enemies, currentPlayer);
                context.sendAction(player.getUsername(), response);
                enemies.put(player.getUsername(), player);
            }

            this.advContext.setAdvState(new IdleState(advContext));

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                context.sendAction(player.getUsername(), exception);
            }
        }
    }

}

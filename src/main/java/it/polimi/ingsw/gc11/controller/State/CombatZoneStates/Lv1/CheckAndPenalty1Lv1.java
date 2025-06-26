package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEverybodyProfileAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;

import java.util.HashMap;
import java.util.Map;

public class CheckAndPenalty1Lv1 extends AdventureState {
    public CheckAndPenalty1Lv1(AdventurePhase advContext) {
        super(advContext);

    }

    @Override
    public void initialize() {

        GameContext context = advContext.getGameContext();

        try{

            GameModel gameModel = advContext.getGameModel();
            int min = 100000;
            Player minPlayer = null;
            //Trova il player con il minor num di equipaggio in ordine di rotta
            for (Player player : gameModel.getPlayersNotAbort()) {
                if (player.getShipBoard().getMembers() < min){
                    min = player.getShipBoard().getMembers();
                    minPlayer = player;
                }
            }

            CombatZoneLv1 combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();

            gameModel.move(minPlayer.getUsername(), combatZoneLv1.getLostDays());

            advContext.setIdxCurrentPlayer(0);


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

            //next state
            this.advContext.setAdvState(new Check2Lv1(this.advContext, 10000, null));

        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
                context.sendAction(player.getUsername(), exception);
            }
        }
    }


}

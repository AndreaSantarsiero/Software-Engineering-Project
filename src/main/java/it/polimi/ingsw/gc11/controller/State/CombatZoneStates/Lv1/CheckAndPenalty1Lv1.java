package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;

public class CheckAndPenalty1Lv1 extends AdventureState {
    public CheckAndPenalty1Lv1(AdventurePhase advContext) {
        super(advContext);
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

        //next state
        this.advContext.setAdvState(new Check2Lv1(this.advContext, 10000, null));
    }
}

package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;

public class Check3Lv2 extends AdventureState {

    public Check3Lv2(AdventurePhase advContext) {
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

        CombatZoneLv2 combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();

        //next state
        this.advContext.setAdvState(new Penalty3Lv2(this.advContext, minPlayer, 0));
    }
}
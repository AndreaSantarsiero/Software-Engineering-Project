package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;

public class Penalty2Lv2 extends AdventureState {

    public Penalty2Lv2(AdventurePhase advContext, Player player) {
        super(advContext);
        CombatZoneLv2 combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();
        player.getShipBoard().removeMaterials(combatZoneLv2.getLostMaterials());
        //next state
        this.advContext.setAdvState(new Check3Lv2(this.advContext));
    }
}
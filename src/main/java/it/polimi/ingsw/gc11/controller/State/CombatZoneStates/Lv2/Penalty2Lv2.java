package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;


/**
 * Represents the penalty state at Combat Zone Level 2 where the player with the lowest engine power
 * must discard a set of materials from their ship.
 *
 * <p>This state is triggered after the engine power check. The penalty is defined by the
 * {@link CombatZoneLv2} card and applied to the losing player. Once the penalty is enforced,
 * the game transitions to the next check phase ({@link Check3Lv2}).</p>
 *
 */
public class Penalty2Lv2 extends AdventureState {

    /**
     * Constructs the {@code Penalty2Lv2} state, applies the material loss penalty to the given player,
     * and immediately transitions to the {@link Check3Lv2} state.
     *
     * @param advContext The current AdventurePhase context.
     * @param player The player who lost the engine power check and must lose materials.
     */
    public Penalty2Lv2(AdventurePhase advContext, Player player) {
        super(advContext);
        CombatZoneLv2 combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();
        player.getShipBoard().removeMaterials(combatZoneLv2.getLostMaterials());
        //next state
        this.advContext.setAdvState(new Check3Lv2(this.advContext));
    }
}
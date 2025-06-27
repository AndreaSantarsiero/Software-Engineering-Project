package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;


/**
 * Represents the penalty state at Combat Zone Level 2 where the player with the lowest firepower
 * is penalized by moving backward on the route.
 *
 * <p>This state is triggered after all players have completed the first firepower check. The player
 * identified as having the minimum firepower receives a penalty (lost days), and the game advances
 * to the next engine power check ({@link Check2Lv2}).</p>
 *
 */
public class Penalty1Lv2 extends AdventureState {

    /**
     * Constructs the {@code Penalty1Lv2} state, applies the movement penalty to the given player,
     * and immediately transitions the state machine to {@link Check2Lv2}.
     *
     * @param advContext The current AdventurePhase context.
     * @param player The player who lost the firepower check and must receive a penalty.
     */
    public Penalty1Lv2(AdventurePhase advContext, Player player) {
        super(advContext);
        CombatZoneLv2 combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();
        advContext.getGameModel().move(player.getUsername(), combatZoneLv2.getLostDays());
        //next state
        this.advContext.setAdvState(new Check2Lv2(this.advContext, 10000, null));
    }
}

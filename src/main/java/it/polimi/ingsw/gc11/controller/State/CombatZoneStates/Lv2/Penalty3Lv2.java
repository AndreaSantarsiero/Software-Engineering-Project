package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;


/**
 * Represents the third penalty state at Combat Zone Level 2, where a player must handle
 * one or more hits after being identified as the weakest based on crew count.
 *
 * <p>This state is responsible for managing the sequence of incoming hits to the player's ship.
 * If there are no more hits to process, the game transitions to the {@link IdleState}.
 * Otherwise, it prompts the player to generate the coordinate for the next shot.</p>
 *
 */
public class Penalty3Lv2 extends AdventureState {

    private final GameModel gameModel;
    private final Player playerDefeated;
    private final int iterationsHit;
    private final CombatZoneLv2 combatZoneLv2;

    /**
     * Constructs the {@code Penalty3Lv2} state and checks if any hits are left to process.
     * If no hits remain, transitions to {@link IdleState}. Otherwise, waits for coordinate input.
     *
     * @param advContext The current AdventurePhase context.
     * @param playerDefeated The player receiving the penalty hits.
     * @param iterationsHit The current shot index (number of hits already handled).
     */
    public Penalty3Lv2(AdventurePhase advContext, Player playerDefeated, int iterationsHit) {
        super(advContext);
        this.playerDefeated = playerDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;

        this.combatZoneLv2 = (CombatZoneLv2) this.advContext.getDrawnAdvCard();
        //No Hit left to handle
        if(iterationsHit == combatZoneLv2.getShots().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
    }

    /**
     * Generates the coordinate for the current hit based on dice rolls.
     * Transitions the game to the {@link HandleShotLv2} state to resolve the hit.
     *
     * @param username The username of the player receiving the hit.
     * @return The {@link Hit} object with the coordinate set.
     * @throws IllegalArgumentException if the player calling the method is not the expected one.
     */
    @Override
    public Hit getCoordinate(String username){

        if(!playerDefeated.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to roll dices");
        }

        int coordinate = gameModel.getValDice1() + gameModel.getValDice2();
        //La coordinata calcolata va poi inviata a tutti i client

        //NextState
        this.advContext.setAdvState(new HandleShotLv2(advContext, playerDefeated, coordinate, iterationsHit));

        Hit hit = combatZoneLv2.getShots().get(iterationsHit);
        hit.setCoordinate(coordinate);

        return hit;
    }
}

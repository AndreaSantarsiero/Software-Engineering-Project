package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1.CheckAndPenalty1Lv1;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;


/**
 * Represents a Level 1 Combat Zone adventure card.
 * <p>
 * This card challenges the players with a series of incoming shots and applies penalties
 * such as crew loss and delays if the defense fails.
 *
 * <p>When this card is drawn, the player must defend against the specified list of {@link Shot}s.
 * If they fail to block any of them, they suffer a penalty in terms of {@code lostMembers} and
 * {@code lostDays}.
 *
 * <p>This card delegates its gameplay logic to the {@link CheckAndPenalty1Lv1} state.
 */
public class CombatZoneLv1 extends AdventureCard {

    private final int lostDays;
    private final int lostMembers;
    private final ArrayList<Shot> shots;


    /**
     * Constructs a new {@code CombatZoneLv1} card with the given parameters.
     *
     * @param id           unique identifier for the card.
     * @param type         type of the adventure card (must not be {@code null}).
     * @param lostDays     number of days lost if the defense fails (must be ≥ 0).
     * @param lostMembers  number of crew members lost if the defense fails (must be ≥ 0).
     * @param shots        list of {@link Shot}s that the player must defend against (must not be null or empty).
     * @throws IllegalArgumentException if parameters are invalid.
     * @throws NullPointerException     if any shot in the list is {@code null}.
     */
    public CombatZoneLv1(String id, AdventureCard.Type type, int lostDays, int lostMembers, ArrayList<Shot> shots) {
        super(id, type);
        if ( type == null || shots == null ||
                shots.isEmpty() ||lostDays <0 || lostMembers <0 ) {
            throw new IllegalArgumentException("At least 1 param isn't valid.");
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        this.lostDays = lostDays;
        this.lostMembers = lostMembers;
        this.shots = shots;
    }


    /**
     * Returns the number of days the player loses if they fail to defend.
     *
     * @return number of days lost.
     */
    public int getLostDays() {
        return lostDays;
    }

    /**
     * Returns the number of crew members lost if the player fails to defend.
     *
     * @return number of crew lost.
     */
    public int getLostMembers() {
        return lostMembers;
    }

    /**
     * Returns the list of shots the player must defend against.
     *
     * @return list of {@link Shot} objects.
     */
    public ArrayList<Shot> getShots() {
        return shots;
    }


    /**
     * Returns the {@link AdventureState} that handles this card's logic during gameplay.
     *
     * @param advContext the current adventure phase context.
     * @return the corresponding state handler.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext) {
        return new CheckAndPenalty1Lv1(advContext);
    }

    /**
     * Prints the card on the CLI interface.
     *
     * @param adventureCardCLI the CLI rendering utility.
     * @param i                the card's index or order.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Sets a hint message describing this card in the user interface.
     *
     * @param adventurePhaseData the data container to update.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

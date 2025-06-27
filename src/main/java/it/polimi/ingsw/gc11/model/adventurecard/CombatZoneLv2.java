package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2.Check1Lv2;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;


/**
 * Represents a Level 2 Combat Zone adventure card.
 * <p>
 * This card challenges players with a set of incoming {@link Shot}s and applies penalties
 * in terms of lost days and lost materials if the player fails to block them.
 *
 * <p>This card is typically more punishing than {@link CombatZoneLv1}, reflecting its higher level.
 * When drawn, it transitions the game to the {@link Check1Lv2} state to resolve the combat interaction.
 */
public class CombatZoneLv2 extends AdventureCard{

    private final int lostDays;
    private final int lostMaterials;
    private final ArrayList<Shot> shots;


    /**
     * Constructs a new {@code CombatZoneLv2} adventure card with specified penalties and shots.
     *
     * @param id             unique identifier for the card.
     * @param type           the type of this adventure card (must not be {@code null}).
     * @param lostDays       number of days lost if the player fails the challenge (must be ≥ 0).
     * @param lostMaterials  number of materials lost if the player fails the challenge (must be ≥ 0).
     * @param shots          list of {@link Shot}s the player must defend against (must not be {@code null} or empty).
     * @throws IllegalArgumentException if parameters are invalid.
     * @throws NullPointerException     if any {@code shot} is {@code null}.
     */
    public CombatZoneLv2(String id, AdventureCard.Type type, int lostDays, int lostMaterials, ArrayList<Shot> shots) {
        super(id, type);
        if ( type == null || shots == null ||
                shots.isEmpty() ||lostDays <0 || lostMaterials <0 ) {
            throw new IllegalArgumentException("At least 1 param isn't valid.");
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        this.lostDays = lostDays;
        this.lostMaterials = lostMaterials;
        this.shots = shots;
    }


    /**
     * Gets the number of days lost as a penalty for failure.
     *
     * @return number of lost days.
     */
    public int getLostDays() {
        return lostDays;
    }

    /**
     * Gets the number of materials lost as a penalty for failure.
     *
     * @return number of lost materials.
     */
    public int getLostMaterials() {
        return lostMaterials;
    }

    /**
     * Gets the list of {@link Shot}s to be resolved in this challenge.
     *
     * @return list of shots.
     */
    public ArrayList<Shot> getShots() {
        return shots;
    }


    /**
     * Returns the initial {@link AdventureState} responsible for resolving this card’s logic.
     *
     * @param advContext the current adventure phase context.
     * @return the state {@link Check1Lv2} which handles combat resolution.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext) {
        return new Check1Lv2(advContext);
    }

    /**
     * Prints the card's information on the CLI using the provided utility.
     *
     * @param adventureCardCLI the CLI drawing tool.
     * @param i                index or display order.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Updates the given UI data structure with a message describing this card.
     *
     * @param adventurePhaseData the data holder to update with hint information.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

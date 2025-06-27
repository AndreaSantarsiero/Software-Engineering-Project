package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.PiratesState;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.List;


/**
 * Represents a Pirates adventure card.
 * <p>
 * This card initiates a combat scenario where the player must meet a required firepower level
 * to defeat the pirates. Failure to do so results in penalties such as lost days and damage from incoming shots.
 * Success is rewarded with coins.
 * <p>
 * The card defines a number of incoming {@link Shot} instances, which must be defended against
 * using batteries and cannons.
 */
public class Pirates extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int coins;
    public List<Shot> shots;


    /**
     * Constructs a new {@code Pirates} adventure card with the specified parameters.
     *
     * @param id        the unique identifier for the card.
     * @param type      the type of the card (usually {@code LEVEL1}, {@code LEVEL2}).
     * @param lostDays  the number of days lost if the player fails to defend.
     * @param firePower the firepower threshold required to defeat the pirates.
     * @param coins     the reward granted for successfully defeating the pirates.
     * @param shots     the list of incoming {@link Shot} instances representing pirate attacks.
     * @throws IllegalArgumentException if any value is negative or the shot list is null/empty.
     * @throws NullPointerException if any shot in the list is null.
     */
    public Pirates(String id, AdventureCard.Type type, int lostDays, int firePower, int coins, List<Shot> shots) throws IllegalArgumentException{
        super(id, type);
        if (shots == null || shots.isEmpty()|| lostDays < 0 || firePower < 0 || coins < 0){
            throw new IllegalArgumentException();
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.coins = coins;
        this.shots = shots;
    }


    /**
     * Returns the number of days lost when the pirates are not defeated.
     *
     * @return the number of lost days.
     */
    public int getLostDays() {return lostDays;}

    /**
     * Returns the firepower threshold required to win the combat.
     *
     * @return the required firepower.
     */
    public int getFirePower() {return firePower;}

    /**
     * Returns the number of coins awarded for defeating the pirates.
     *
     * @return the reward in coins.
     */
    public int getCoins() {return coins;}

    /**
     * Returns the list of shots inflicted by the pirates.
     *
     * @return the list of {@link Shot} instances.
     */
    public List<Shot> getShots() {return shots;}


    /**
     * Returns the initial {@link AdventureState} associated with this card.
     *
     * @param advContext the context of the current adventure phase.
     * @return the {@link PiratesState} for handling this card.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new PiratesState(advContext);
    }

    /**
     * Displays this card in the CLI.
     *
     * @param adventureCardCLI the CLI utility used for drawing.
     * @param i                the index of the card in the deck or list.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Supplies a hint message about the card's effects.
     *
     * @param adventurePhaseData the data object used to communicate hints.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

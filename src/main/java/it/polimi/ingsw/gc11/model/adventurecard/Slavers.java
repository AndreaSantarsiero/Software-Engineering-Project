package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.SlaversState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;


/**
 * Represents a Slavers adventure card.
 * <p>
 * This card introduces a hostile encounter in which players must decide whether to engage
 * the slavers in combat or suffer the consequences. Each player must meet a certain
 * firepower threshold to avoid penalties.
 * <p>
 * If a player fails to match the required firepower, they lose a number of crew members,
 * coins, and days of travel.
 */
public class Slavers extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int lostMembers;
    private final int coins;

    /**
     * Constructs a new {@code Slavers} card with the given parameters.
     *
     * @param id           the unique identifier for the card.
     * @param type         the level/type of the card (e.g. {@link Type#LEVEL2}).
     * @param lostDays     the number of travel days lost if the slavers are not defeated.
     * @param firePower    the firepower threshold required to defeat the slavers.
     * @param lostMembers  the number of crew members lost if the player fails to defeat the slavers.
     * @param coins        the number of coins lost if the player fails to defeat the slavers.
     * @throws IllegalArgumentException if any numeric value is negative or the type is null.
     */
    public Slavers(String id, AdventureCard.Type type, int lostDays, int firePower, int lostMembers, int coins) throws IllegalArgumentException{
        super(id, type);
        if (type == null || lostDays < 0 || firePower < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("At least 1 param isn't valid.");
        }
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.lostMembers = lostMembers;
        this.coins = coins;
    }


    /**
     * @return the number of days lost if the slavers are not defeated.
     */
    public int getLostDays() {return lostDays;}

    /**
     * @return the firepower threshold required to defeat the slavers.
     */

    public int getFirePower() {return firePower;}

    /**
     * @return the number of crew members lost if the slavers are not defeated.
     */
    public int getLostMembers() {return lostMembers;}

    /**
     * @return the number of coins lost if the slavers are not defeated.
     */
    public int getCoins() {return coins;}


    /**
     * Returns the initial state that will handle the logic of this card.
     *
     * @param advContext the current adventure phase context.
     * @return the {@link SlaversState} instance associated with this card.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new SlaversState(advContext);
    }

    /**
     * Displays this card in the CLI.
     *
     * @param adventureCardCLI the CLI renderer for cards.
     * @param i the index of this card in the deck or sequence.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Updates the client with a hint message describing the card's effect.
     *
     * @param adventurePhaseData the object carrying UI-related data to the client.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

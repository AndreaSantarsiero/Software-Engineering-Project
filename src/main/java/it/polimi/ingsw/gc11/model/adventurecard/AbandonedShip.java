package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.AbandonedShipState;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;


/**
 * Represents the {@code AbandonedShip} adventure card.
 * <p>
 * This card introduces a trade-off where the player loses a specified number of crew members and days
 * in exchange for a coin reward. The card can only be resolved once per game instance.
 * </p>
 *
 * <ul>
 *   <li>{@code lostDays}: the number of days lost if the card is accepted</li>
 *   <li>{@code lostMembers}: the number of crew members lost</li>
 *   <li>{@code coins}: the coin reward received upon resolution</li>
 *   <li>{@code resolved}: flag indicating whether the card has already been resolved</li>
 * </ul>
 */
public class AbandonedShip extends AdventureCard {

    private final int lostDays;
    private final int lostMembers;
    private final int coins;
    private boolean resolved;


    /**
     * Constructs an {@code AbandonedShip} card with specified parameters.
     *
     * @param id          the unique identifier of the card
     * @param type        the type of the card (e.g., LEVEL1, LEVEL2, TRIAL)
     * @param lostDays    number of days the player will lose
     * @param lostMembers number of crew members the player will lose
     * @param coins       amount of coins the player gains
     * @throws IllegalArgumentException if any argument is negative
     */
    public AbandonedShip(String id, AdventureCard.Type type, int lostDays, int lostMembers, int coins) throws IllegalArgumentException{
        super(id, type);

        if(lostDays < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        this.lostDays = lostDays;
        this.lostMembers = lostMembers;
        this.coins = coins;
        this.resolved = false;

    }


    /**
     * @return the number of days lost when the card is accepted
     */
    public int getLostDays() {return lostDays;}

    /**
     * @return the number of crew members lost when the card is accepted
     */
    public int getLostMembers() {return lostMembers;}

    /**
     * @return the number of coins gained when the card is accepted
     */
    public int getCoins() {return coins;}

    /**
     * @return {@code true} if the card has already been resolved; {@code false} otherwise
     */
    public boolean isResolved() {return resolved;}

    /**
     * Marks the card as resolved. This method should be called after applying its effects.
     */
    public void resolveCard(){
        this.resolved = true;
    }


    /**
     * Returns the initial {@link AdventureState} associated with this card.
     * Used to delegate behavior to the correct logic class in the AdventurePhase FSM.
     *
     * @param advContext the adventure phase context
     * @return the state object that handles the logic for this card
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext ){
        return new AbandonedShipState(advContext);
    }

    /**
     * Delegates the drawing of this card to the provided CLI renderer.
     *
     * @param adventureCardCLI the CLI component responsible for rendering
     * @param i                the card index or position
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Populates the hint message related to this card into the provided {@link AdventurePhaseData}.
     *
     * @param adventurePhaseData the data container to update with the card hint
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

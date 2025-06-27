package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.io.Serializable;


/**
 * Abstract base class for all adventure cards in the game.
 * <p>
 * Each {@code AdventureCard} represents a challenge, event, or opportunity that may occur
 * during the adventure phase of the game. Subclasses implement the specific behavior and
 * effects of each type of card.
 *
 * <p>Each card has a unique identifier, a {@link Type}, and a flag indicating whether it
 * has already been used (i.e., drawn or resolved) in the current game.
 *
 * <p>This class defines abstract methods for:
 * <ul>
 *   <li>{@link #getInitialState(AdventurePhase)}: to generate the state machine logic associated with the card.</li>
 *   <li>{@link #print(AdventureCardCLI, int)}: to render the card in the CLI view.</li>
 *   <li>{@link #getHintMessage(AdventurePhaseData)}: to populate UI hint or tooltip messages.</li>
 * </ul>
 */
public abstract class AdventureCard implements Serializable {

    public enum Type {
        TRIAL, LEVEL1, LEVEL2;
    }


    private final String id;
    private final Type type;
    private boolean used;

    /**
     * Constructs a new adventure card with the specified ID and type.
     *
     * @param id   the unique identifier for the card (used for debugging or card tracking).
     * @param type the difficulty/type level of the card.
     */
    public AdventureCard(String id, Type type) {
        this.id = id;
        this.type = type;
        this.used = false;
    }

    /**
     * Returns the unique identifier of the card.
     *
     * @return the card's ID.
     */
    public String getId() {return id;}

    /**
     * Marks this card as used (drawn and/or resolved).
     * This can be used to exclude the card from future draws.
     */
    public void useCard(){
        this.used = true;
    }

    /**
     * Returns the type (difficulty level) of this card.
     *
     * @return the card's type.
     */
    public Type getType(){
        return type;
    }

    /**
     * Returns whether this card has already been used.
     *
     * @return {@code true} if the card was used in this game instance; {@code false} otherwise.
     */
    public boolean isUsed(){
        return used;
    }

    /**
     * Returns the {@link AdventureState} responsible for handling the gameplay logic
     * when this card is drawn.
     *
     * @param advContext the current adventure phase context.
     * @return the state machine handling this card.
     */
    public abstract AdventureState getInitialState(AdventurePhase advContext);

    /**
     * Renders this card in the CLI interface using the appropriate utility.
     *
     * @param adventureCardCLI the CLI rendering utility.
     * @param i                the index of the card in the deck or display.
     */
    public abstract void print(AdventureCardCLI adventureCardCLI, int i);

    /**
     * Updates the provided {@link AdventurePhaseData} with this card's hint or description.
     *
     * @param adventurePhaseData the phase data object to update.
     */
    public abstract void getHintMessage(AdventurePhaseData adventurePhaseData);
}

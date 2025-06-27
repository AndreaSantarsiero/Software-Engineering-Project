package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.StarDustStates.StarDustState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;


/**
 * Represents a {@code StarDust} adventure card.
 * <p>
 * This card introduces a space event where players must handle an encounter with star dust.
 * The specific effects and interactions of the card are defined by its corresponding {@link StarDustState}.
 * <p>
 * The card follows the standard lifecycle of an {@link AdventureCard}:
 * it provides an initial state for state transitions, can print itself to the CLI,
 * and communicates a hint message to the client-side UI.
 */
public class StarDust extends AdventureCard {

    /**
     * Constructs a {@code StarDust} card with the specified ID and difficulty level.
     *
     * @param id   the unique identifier for this card.
     * @param type the adventure type (e.g., {@link Type#LEVEL1}, {@link Type#LEVEL2}).
     */
    public StarDust(String id, AdventureCard.Type type) {
        super(id, type);
    }


    /**
     * Returns the initial {@link AdventureState} that handles the logic and interaction
     * for this card, which is {@link StarDustState}.
     *
     * @param advContext the current adventure phase context.
     * @return a new instance of {@code StarDustState}.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new StarDustState(advContext);
    }

    /**
     * Renders the card visually using the provided CLI drawing utility.
     *
     * @param adventureCardCLI the CLI renderer.
     * @param i                the index or position of this card in the sequence.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Sets a context-specific hint message on the adventure phase client data model.
     * This allows the UI to display contextual information about the card.
     *
     * @param adventurePhaseData the client-side adventure phase data model.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

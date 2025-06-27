package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.EpidemicStates.EpidemicState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;


/**
 * Represents an Epidemic adventure card.
 * <p>
 * This card introduces a health-related crisis aboard the players' ships, such as
 * the spread of a disease that affects crew members.
 * It belongs to {@link AdventureCard.Type#LEVEL2}, implying it appears in the late phase
 * of the game and has a strong negative impact.
 *
 * <p>When this card is drawn, the game transitions to the {@link EpidemicState} to handle
 * the card's specific logic and apply effects accordingly.
 */
public class Epidemic extends AdventureCard {

    /**
     * Constructs an Epidemic card with the given ID.
     * <p>
     * This card is always of type {@link AdventureCard.Type#LEVEL2}.
     *
     * @param id the unique identifier for the card.
     */
    public Epidemic(String id) {
        super(id, Type.LEVEL2);
    }


    /**
     * Returns the initial {@link AdventureState} responsible for resolving the effects
     * of the epidemic.
     *
     * @param advContext the current adventure phase context.
     * @return the {@link EpidemicState} representing this card's logic.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new EpidemicState(advContext);
    }

    /**
     * Displays the epidemic card on the CLI interface.
     *
     * @param adventureCardCLI the CLI drawing tool.
     * @param i                the index of the card in the current phase.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Updates the hint message in the UI with a description of this card.
     *
     * @param adventurePhaseData the data structure to update.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

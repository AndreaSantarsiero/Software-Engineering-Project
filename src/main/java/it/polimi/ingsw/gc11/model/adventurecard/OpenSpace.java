package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;


/**
 * Represents an Open Space adventure card.
 * <p>
 * This card allows players to use their engines to move forward in the turn order.
 * It is a tactical opportunity for players to spend energy and overtake others,
 * which may be important for future adventure card resolution.
 * <p>
 * When played, this card transitions the game to the {@link OpenSpaceState}, where
 * players can activate engines using their available batteries.
 */
public class OpenSpace extends AdventureCard {

    /**
     * Represents an Open Space adventure card.
     * <p>
     * This card allows players to use their engines to move forward in the turn order.
     * It is a tactical opportunity for players to spend energy and overtake others,
     * which may be important for future adventure card resolution.
     * <p>
     * When played, this card transitions the game to the {@link OpenSpaceState}, where
     * players can activate engines using their available batteries.
     */
    public OpenSpace(String id, AdventureCard.Type type) {
        super(id, type);
    }


    /**
     * Returns the initial {@link AdventureState} for this card, which is an instance of {@link OpenSpaceState}.
     *
     * @param advContext the context of the current adventure phase.
     * @return the {@link OpenSpaceState} associated with this card.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new OpenSpaceState(advContext);
    }

    /**
     * Renders this card in the command-line interface.
     *
     * @param adventureCardCLI the CLI utility used to display the card.
     * @param i                the index of the card in the current deck or queue.
     */

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Updates the adventure phase data with a hint related to this card's effect.
     *
     * @param adventurePhaseData the data object used to convey hints to the player.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

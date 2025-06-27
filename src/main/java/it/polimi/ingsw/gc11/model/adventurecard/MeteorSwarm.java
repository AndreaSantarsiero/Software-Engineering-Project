package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.MeteorSwarmState;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;


/**
 * Represents a Meteor Swarm adventure card.
 * <p>
 * This card introduces a sequence of meteor impacts that players must defend against
 * using their ship's batteries, cannons, or by strategically accepting damage.
 * The meteors are defined with directions and types that determine how they affect the ship.
 * <p>
 * When drawn, this card transitions the game to the {@link MeteorSwarmState}, which handles
 * the step-by-step resolution of the impacts.
 */
public class MeteorSwarm extends AdventureCard {

    private final ArrayList<Meteor> meteors;


    /**
     * Constructs a new {@code MeteorSwarm} card with a specified ID, type, and meteor sequence.
     *
     * @param id      the unique identifier for this card.
     * @param type    the adventure card type (e.g., {@link AdventureCard.Type#TRIAL}).
     * @param meteors a list of {@link Meteor} objects representing the impacts to resolve.
     * @throws IllegalArgumentException if {@code meteors} is {@code null} or contains {@code null} elements.
     */
    public MeteorSwarm(String id, AdventureCard.Type type, ArrayList<Meteor> meteors) throws IllegalArgumentException {
        super(id, type);

        if (meteors == null){
            throw new IllegalArgumentException();
        }
        for(Meteor meteor: meteors) {
            if (meteor == null) {
                throw new NullPointerException("meteors is null.");
            }
        }
        this.meteors = meteors;
    }


    /**
     * Returns the list of meteors associated with this card.
     *
     * @return a list of {@link Meteor} objects.
     */
    public  ArrayList<Meteor> getMeteors() {
        return meteors;
    }


    /**
     * Returns the {@link MeteorSwarmState} responsible for managing the meteor impacts.
     * Starts from the first meteor in the list.
     *
     * @param advContext the current adventure phase context.
     * @return a new instance of {@link MeteorSwarmState}.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new MeteorSwarmState(advContext, 0);
    }

    /**
     * Displays the card on the command-line interface.
     *
     * @param adventureCardCLI the CLI renderer utility.
     * @param i                the card's index in the current adventure queue.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Updates the user interface with a hint about this card.
     *
     * @param adventurePhaseData the structure to carry hint information.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

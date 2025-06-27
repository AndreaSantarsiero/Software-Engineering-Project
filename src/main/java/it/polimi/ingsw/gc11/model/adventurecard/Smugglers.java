package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.SmugglersStates.SmugglersState;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;


/**
 * Represents a Smugglers adventure card.
 * <p>
 * This card introduces an encounter with space smugglers. Players must decide
 * whether to confront the smugglers using firepower or suffer penalties such as
 * losing days of travel and valuable materials.
 * <p>
 * If the smugglers are not defeated, the player loses a fixed number of days and materials.
 */
public class Smugglers extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int lostMaterials;
    private final ArrayList<Material> materials;
    private boolean defeated;

    /**
     * Constructs a new {@code Smugglers} card.
     *
     * @param id             the unique identifier for the card.
     * @param type           the type/level of the card (e.g. {@link Type#LEVEL2}).
     * @param lostDays       the number of days lost if the player does not defeat the smugglers.
     * @param firePower      the firepower threshold required to defeat the smugglers.
     * @param lostMaterials  the number of materials to be lost if the smugglers are not defeated.
     * @param materials      the list of materials at stake in this encounter.
     * @throws IllegalArgumentException if any parameter is invalid (e.g., negative values or null materials).
     */
    public Smugglers(String id, AdventureCard.Type type, int lostDays, int firePower, int lostMaterials,  ArrayList<Material> materials) throws IllegalArgumentException {
        super(id, type);

        if(lostDays < 0 || firePower < 0 || lostMaterials < 0 || materials == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        for(Material material: materials) {
            if (material == null) {
                throw new IllegalArgumentException("invalid material");
            }
        }
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.lostMaterials = lostMaterials;
        this.materials = materials;
        this.defeated = false;
    }


    /**
     * @return the number of travel days lost if the player does not defeat the smugglers.
     */
    public int getLostDays() {return lostDays;}

    /**
     * @return the required firepower to successfully defeat the smugglers.
     */
    public int getFirePower() {return firePower;}

    /**
     * @return the number of materials lost if the smugglers are not defeated.
     */
    public int getLostMaterials() {return lostMaterials;}

    /**
     * @return the list of materials involved in this encounter.
     */
    public ArrayList<Material> getMaterials() {return materials;}

    /**
     * @return {@code true} if the player has defeated the smugglers, {@code false} otherwise.
     */
    public boolean isDefeated() {return defeated;}

    /**
     * Marks the smugglers as defeated.
     */
    public void defeate() {defeated = true;}


    /**
     * Returns the initial state that handles this card’s logic.
     *
     * @param advContext the current adventure phase context.
     * @return a new {@link SmugglersState} for this card.
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new SmugglersState(advContext);
    }

    /**
     * Renders the card using the CLI utility.
     *
     * @param adventureCardCLI the CLI renderer.
     * @param i                the index of the card in the sequence.
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Sets a hint message in the adventure phase UI to inform the player about the card.
     *
     * @param adventurePhaseData the data object for client-side view hints.
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

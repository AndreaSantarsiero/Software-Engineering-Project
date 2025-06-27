package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AbandonedStationStates.AbandonedStationState;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;


/**
 * Represents the {@code AbandonedStation} adventure card.
 * <p>
 * This card presents a challenge where a player may sacrifice crew members and days
 * in order to retrieve valuable materials from an abandoned station.
 * </p>
 *
 * <ul>
 *   <li>{@code lostDays}: number of days lost during the operation</li>
 *   <li>{@code membersRequired}: number of crew members required to access the station</li>
 *   <li>{@code materials}: list of materials that can be obtained as reward</li>
 *   <li>{@code resolved}: whether the card has already been resolved by a player</li>
 * </ul>
 */
public class AbandonedStation extends AdventureCard {

    private final int lostDays;
    private final int membersRequired;
    private final ArrayList<Material> materials;
    private boolean resolved;


    /**
     * Constructs an {@code AbandonedStation} with the specified parameters.
     *
     * @param id              the identifier of the card
     * @param type            the type of adventure card (e.g., TRIAL, LEVEL1)
     * @param lostDays        number of days lost if the action is taken
     * @param membersRequired number of crew members required to activate the station
     * @param numBlue         number of blue materials provided
     * @param numGreen        number of green materials provided
     * @param numYellow       number of yellow materials provided
     * @param numRed          number of red materials provided
     * @throws IllegalArgumentException if any parameter is negative
     */
    public AbandonedStation(String id, AdventureCard.Type type, int lostDays, int membersRequired, int numBlue, int numGreen, int numYellow, int numRed)throws IllegalArgumentException {
        super(id, type);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (membersRequired < 0) {
            throw new IllegalArgumentException("negative members required.");
        }
        if (numBlue < 0 || numGreen < 0 || numYellow < 0 || numRed < 0) {
            throw new IllegalArgumentException("negative materials required.");
        }

        this.lostDays = lostDays;
        this.membersRequired = membersRequired;
        this.materials = new ArrayList<>();
        this.resolved = false;

        //add materials
        for (int i = 0; i < numBlue; i++)
            materials.add(new Material(Material.Type.BLUE));
        for (int i = 0; i < numGreen; i++)
            materials.add(new Material(Material.Type.GREEN));
        for (int i = 0; i < numYellow; i++)
            materials.add(new Material(Material.Type.YELLOW));
        for (int i = 0; i < numRed; i++)
            materials.add(new Material(Material.Type.RED));
    }


    /**
     * @return the number of days lost when interacting with the station
     */
    public int getLostDays() {return lostDays;}
    /**
     * @return the number of crew members required to explore the station
     */
    public int getMembersRequired() {return membersRequired;}

    /**
     * @return the list of materials retrieved from the station
     */
    public ArrayList<Material> getMaterials() {return materials;}

    /**
     * @return {@code true} if the card has already been resolved; {@code false} otherwise
     */
    public boolean isResolved() {return resolved;}

    /**
     * Marks this card as resolved.
     * This should be called after applying the effects of the card.
     */
    public void resolveCard() {this.resolved = true;}


    /**
     * Returns the initial {@link AdventureState} that handles the interaction logic for this card.
     *
     * @param advContext the context of the current adventure phase
     * @return the state instance that will manage this card's resolution logic
     */
    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new AbandonedStationState(advContext);
    }

    /**
     * Renders this card using a CLI-based drawer.
     *
     * @param adventureCardCLI the component responsible for rendering cards in CLI
     * @param i                the index or order in the current deck
     */
    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    /**
     * Updates the provided {@link AdventurePhaseData} with a contextual hint for this card.
     *
     * @param adventurePhaseData the data object to be populated with hint info
     */
    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

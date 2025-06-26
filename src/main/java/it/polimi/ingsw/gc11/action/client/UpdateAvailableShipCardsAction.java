package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;
import java.util.List;


/**
 * Action that updates the list of free ship cards during the Building phase.
 */
public class UpdateAvailableShipCardsAction extends ServerAction{

    private final List<ShipCard> availableShipCards;
    private final int shipCardsCount;
    private final boolean updateState;

    /**
     * Constructs a new UpdateAvailableShipCardsAction.
     *
     * @param availableShipCards the current pool of free ship cards
     * @param shipCardsCount     the total number of free ship cards remaining
     * @param updateState        {@code true} to trigger a state update after loading cards
     */
    public UpdateAvailableShipCardsAction(List<ShipCard> availableShipCards, int shipCardsCount, boolean updateState) {
        this.availableShipCards = availableShipCards;
        this.shipCardsCount = shipCardsCount;
        this.updateState = updateState;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Loads the updated ship cards into the Building phase data.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setFreeShipCards(availableShipCards, shipCardsCount, updateState);
    }

    /**
     * No-op for the Check phase.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}

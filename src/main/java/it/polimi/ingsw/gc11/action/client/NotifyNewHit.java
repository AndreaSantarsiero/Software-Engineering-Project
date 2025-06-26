package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;

/**
 * Action that notifies whether the player has a new hit
 * to process during the Adventure phase.
 */
public class NotifyNewHit extends ServerAction {

    private final Boolean newHit;


    /**
     * Constructs a new NotifyNewHit action.
     *
     * @param newHit {@code true} to indicate a new hit awaiting coordinates,
     *               {@code false} otherwise
     */
    public NotifyNewHit(Boolean newHit) {
        this.newHit = newHit ;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * No-op for the Building phase.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    /**
     * No-op for the Check phase.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * Loads the new-hit flag into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setNewHit(newHit);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {
    }
}

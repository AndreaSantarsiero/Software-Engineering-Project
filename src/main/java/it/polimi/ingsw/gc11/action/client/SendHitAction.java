package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.view.*;


/**
 * Action that sends a Hit event to the Adventure phase view.
 */
public class SendHitAction extends ServerAction{

    private final Hit hit;

    /**
     * Constructs a new SendHitAction with the specified hit.
     *
     * @param hit the Hit to be handled in the Adventure phase
     */
    public SendHitAction(Hit hit) {
        this.hit = hit;
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
     * Loads the hit into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setHit(hit);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}

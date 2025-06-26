package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;


/**
 * Action signaling that the previous server request
 * was successful. It triggers UI/state updates in the current phase.
 */
public class NotifySuccessAction extends ServerAction {

    /**
     * Constructs a new NotifySuccessAction.
     */
    public NotifySuccessAction() {}

    /**
     * Updates the state in the Joining phase upon success.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.updateState();
    }

    /**
     * Marks the last action as successful and updates the state
     * in the Building phase.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setActionSuccessful();
        buildingPhaseData.updateState();
    }

    /**
     * Updates the state in the Check phase upon success.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.updateState();
    }

    /**
     * Updates GUI and phase state in the Adventure phase upon success.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.updateGUIState();
        adventurePhaseData.updateState();
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}

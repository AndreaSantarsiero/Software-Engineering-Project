package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;


/**
 * Action that updates the identifier of the current player
 * in the Adventure phase and optionally triggers a GUI/state update.
 */
public class UpdateCurrentPlayerAction extends ServerAction {

    private final String currentPlayer;
    private final boolean updateState;


    /**
     * Constructs a new UpdateCurrentPlayerAction.
     *
     * @param currentPlayer the ID of the new current player
     * @param updateState   {@code true} to update the Adventure phase state after setting
     */
    public UpdateCurrentPlayerAction(String currentPlayer, boolean updateState) {
        this.currentPlayer = currentPlayer;
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
     * Updates the GUI state and sets the current player in the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.updateGUIState();
        adventurePhaseData.setCurrentPlayer(currentPlayer, updateState);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}

package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;

/**
 * Action that delivers a handle message to the Adventure phase view.
 */
public class SendHandleMessageAction extends ServerAction {

    private final String message;

    /**
     * Constructs a new SendHandleMessageAction with the specified message.
     *
     * @param message the handle message to show to the player
     */
    public SendHandleMessageAction(String message) {
        this.message = message;
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
     * Loads the handle message into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setHandleMessage(message);
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

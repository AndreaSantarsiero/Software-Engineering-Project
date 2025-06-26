package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;


/**
 * Action that carries an exception message from the server
 * and injects it into each phase's view data.
 */
public class NotifyExceptionAction extends ServerAction {

    private final String message;

    /**
     * Constructs a new action to notify the client of an exception.
     *
     * @param message the exception message from the server
     */
    public NotifyExceptionAction(String message) {
        this.message = message;
    }


    /**
     * Returns the exception message carried by this action.
     *
     * @return the server exception message
     */
    public String getMessage() {
        return message;
    }


    /**
     * Loads the exception message into the Joining phase data.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {
        joiningPhaseData.setServerMessage(message);
    }

    /**
     * Loads the exception message into the Building phase data.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setServerMessage(message);
    }

    /**
     * Loads the exception message into the Check phase data.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.setServerMessage(message);
    }

    /**
     * Loads the exception message into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setServerMessage(message);
    }

    /**
     * Loads the exception message into the End phase data.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {
        endPhaseData.setServerMessage(message);
    }
}

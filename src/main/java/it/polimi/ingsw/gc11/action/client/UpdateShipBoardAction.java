package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;

/**
 * Action that updates the ShipBoard view in both the
 * Building and Check phases.
 */
public class UpdateShipBoardAction extends ServerAction{
    private final ShipBoard shipBoard;

    /**
     * Constructs a new UpdateShipBoardAction.
     *
     * @param shipBoard the ShipBoard to set in the view data
     */
    public UpdateShipBoardAction(ShipBoard shipBoard){
        this.shipBoard = shipBoard;
    }

    /**
     * Returns the ShipBoard carried by this action.
     *
     * @return the ShipBoard instance
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Sets the ShipBoard in the Building phase data.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setShipBoard(shipBoard);
    }

    /**
     * Sets the ShipBoard in the Check phase data.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.setShipBoard(shipBoard);
    }

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

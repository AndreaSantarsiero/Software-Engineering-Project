package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;

/**
 * Action that loads the specified ShipBoard into the Building phase data.
 */
public class LoadACoolShipAction extends ServerAction {

    private final ShipBoard coolShip;

    /**
     * Creates a new action to load the given ShipBoard.
     *
     * @param coolShip the ShipBoard to be loaded
     */
    public LoadACoolShipAction(ShipBoard coolShip) {
        this.coolShip = coolShip;
    }


    /**
     * No-op for Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Loads the ShipBoard into the Building phase data.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setShipBoard(coolShip);
    }

    /**
     * No-op for Check phase.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * No-op for Adventure phase.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}

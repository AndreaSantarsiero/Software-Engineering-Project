package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;


/**
 * Action that removes a ship card from a specified ship board
 * during the Building phase.
 */
public class UpdateRemoveShipCardAction extends ServerAction{
    private final ShipBoard shipBoard;
    private final ShipCard heldShipCard;

    /**
     * Constructs a new UpdateRemoveShipCardAction.
     *
     * @param shipBoard    the ship board to update
     * @param heldShipCard the ship card to remove
     */
    public UpdateRemoveShipCardAction(ShipBoard shipBoard, ShipCard heldShipCard) {
        this.shipBoard = shipBoard;
        this.heldShipCard = heldShipCard;
    }

    /**
     * Returns the ship board associated with this action.
     *
     * @return the ShipBoard from which the card is removed
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
     * Removes the specified ship card from the board in the Building phase.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.removeShipCard(shipBoard, heldShipCard);
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
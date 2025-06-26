package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;


/**
 * Action that updates an enemy player's ShipBoard
 * in the Building and Check phases.
 */
public class UpdateEnemyShipBoardAction extends ServerAction{

    private final ShipBoard shipBoard;
    private final String username;


    /**
     * Constructs a new UpdateEnemyShipBoardAction.
     *
     * @param shipBoard the ShipBoard instance for the enemy player
     * @param username  the username of the enemy player
     */
    public UpdateEnemyShipBoardAction(ShipBoard shipBoard, String username) {
        this.shipBoard = shipBoard;
        this.username = username;
    }


    /**
     * Returns the ShipBoard to set for the enemy player.
     *
     * @return the enemy's ShipBoard
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * Returns the username of the enemy player.
     *
     * @return the enemy player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Sets the enemy's ShipBoard in the Building phase data.
     *
     * @param buildingPhaseData data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setEnemiesShipBoard(username, shipBoard);
    }

    /**
     * Sets the enemy's ShipBoard in the Check phase data.
     *
     * @param checkPhaseData data for the Check phase
     */
    @Override
    public void loadData(CheckPhaseData checkPhaseData) {
        checkPhaseData.setEnemiesShipBoard(username, shipBoard);
    }

    /**
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}

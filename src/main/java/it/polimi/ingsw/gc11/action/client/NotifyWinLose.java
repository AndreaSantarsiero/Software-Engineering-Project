package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.view.*;

/**
 * Action that notifies the game result (win, lose, or draw)
 * to the Adventure phase, so the UI can display the appropriate outcome.
 */
public class NotifyWinLose extends ServerAction{
    public enum Response{
        WIN, LOSE, DRAW,
    }
    private final Response youWon; //True if the player won, false if the player lost

    /**
     * Constructs a new NotifyWinLose action with the specified result.
     *
     * @param youWon the game outcome to notify (WIN, LOSE, or DRAW)
     */
    public NotifyWinLose(Response youWon) {
        this.youWon = youWon;
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
     * Loads the game outcome into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setYouWon(youWon);
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

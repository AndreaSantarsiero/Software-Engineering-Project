package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.*;


/**
 * Action that sends an AdventureCard to the client UI
 * and optionally updates the game state for a specified player during
 * the Adventure phase.
 */
public class SendAdventureCardAction extends ServerAction{

    private  final AdventureCard adventureCard;
    private final boolean updateState;
    private  final String currentPlayer;



    /**
     * Constructs a new action to send an adventure card for the given player.
     *
     * @param adventureCard the AdventureCard to display
     * @param updateState   {@code true} to update the phase state after setting the card
     * @param currentPlayer the player identifier receiving the card
     */
    public SendAdventureCardAction(AdventureCard adventureCard, boolean updateState, String currentPlayer) {
        this.adventureCard = adventureCard;
        this.updateState = updateState;
        this.currentPlayer = currentPlayer;
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
     * Loads the current player and adventure card into the Adventure phase data.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setCurrentPlayer(currentPlayer, false);
        adventurePhaseData.setAdventureCard(adventureCard, updateState);
    }

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override
    public void loadData(EndPhaseData endPhaseData) {}

}

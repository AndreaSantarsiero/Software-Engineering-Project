package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.*;

import java.util.List;


/**
 * Action that sends the mini deck of AdventureCards
 * to the Building phase view.
 */
public class SendMiniDeckAction extends ServerAction{
    private final List<AdventureCard> miniDeck;

    /**
     * Constructs a new SendMiniDeckAction.
     *
     * @param miniDeck the mini deck of AdventureCards to send
     */
    public SendMiniDeckAction(List<AdventureCard> miniDeck) {
        this.miniDeck = miniDeck;
    }

    /**
     * Returns the mini deck carried by this action.
     *
     * @return the list of AdventureCards in the mini deck
     */
    public List<AdventureCard> getMiniDeck() {
        return miniDeck;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Loads the mini deck into the Building phase data.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setMiniDeck(miniDeck);
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

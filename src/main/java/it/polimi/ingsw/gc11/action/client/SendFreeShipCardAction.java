package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.*;
import java.util.List;


/**
 * Action that sends the free ship card selection data
 * to the Building phase view, including the chosen card, pool of available cards,
 * and the count of remaining cards.
 */
public class SendFreeShipCardAction extends ServerAction{

    private final ShipCard shipCard;
    private final List<ShipCard> availableShipCards;
    private final int availableShipCardsCount;

    /**
     * Constructs a new SendFreeShipCardAction.
     *
     * @param shipCard                 the ship card chosen by the player
     * @param availableShipCards       the list of remaining available ship cards
     * @param availableShipCardsCount  the number of ship cards left after the choice
     */
    public SendFreeShipCardAction(ShipCard shipCard, List<ShipCard> availableShipCards, int availableShipCardsCount) {
        this.shipCard = shipCard;
        this.availableShipCards = availableShipCards;
        this.availableShipCardsCount = availableShipCardsCount;
    }

    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Loads the selected ship card and availability data into the Building phase view.
     *
     * @param buildingPhaseData the data for the Building phase
     */
    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.setHeldShipCard(shipCard, availableShipCards, availableShipCardsCount);
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

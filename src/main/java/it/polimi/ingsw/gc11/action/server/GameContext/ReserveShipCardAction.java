package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

/**
 * Action that allows a player to reserve a ship card, adding it to their board.
 * On success, sends an UpdateShipBoardAction with the updated board;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class ReserveShipCardAction extends ClientGameAction {
    private ShipCard shipCard;

    /**
     * Constructs a new ReserveShipCardAction.
     *
     * @param username the name of the player reserving the card
     * @param shipCard the ShipCard to reserve
     */
    public ReserveShipCardAction(String username, ShipCard shipCard) {
        super(username);
        this.shipCard = shipCard;
    }

    /**
     * Executes the reservation of the ship card in the game context.
     * <ol>
     *   <li>Resets the card orientation to DEG_0.</li>
     *   <li>Calls context.reserveShipCard(username, shipCard).</li>
     *   <li>Sends an UpdateShipBoardAction with the resulting ShipBoard.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message.</li>
     * </ol>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            ShipBoard shipBoard = context.reserveShipCard(username, shipCard);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

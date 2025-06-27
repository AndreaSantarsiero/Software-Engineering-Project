package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateRemoveShipCardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;


/**
 * Action that allows a player to remove a ship card from their ShipBoard
 * at the specified coordinates. On success, sends an UpdateRemoveShipCardAction
 * containing the updated board and the card moved to the player’s hand.
 * On failure, sends a NotifyExceptionAction with the error message.
 */
public class RemoveShipCardAction extends ClientGameAction {

    private final int x;
    private final int y;

    /**
     * Constructs a new RemoveShipCardAction.
     *
     * @param username the name of the player performing the removal
     * @param x        the x-coordinate of the card to remove
     * @param y        the y-coordinate of the card to remove
     */
    public RemoveShipCardAction(String username, int x, int y) {
        super(username);
        this.x = x;
        this.y = y;
    }

    /**
     * Executes the removal of the ship card in the game context.
     * <ul>
     *   <li>Calls context.removeShipCard(username, x, y) to update the board.</li>
     *   <li>Retrieves the card now held by the player from the GameModel.</li>
     *   <li>Sends an UpdateRemoveShipCardAction with the new ShipBoard and held card.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context){
        try {
            ShipBoard shipBoard = context.removeShipCard(username, x, y);
            ShipCard heldShipCard = context.getGameModel().getHeldShipCard(username);
            UpdateRemoveShipCardAction response = new UpdateRemoveShipCardAction(shipBoard, heldShipCard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

/**
 * Action that allows a player to use a previously reserved ShipCard at specified coordinates.
 * On success, sends an UpdateShipBoardAction with the updated board;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class UseReservedShipCardAction extends ClientGameAction {
    private ShipCard shipCard;
    private int x;
    private int y;

    /**
     * Constructs a new UseReservedShipCardAction.
     *
     * @param username the name of the player using the reserved card
     * @param shipCard the ShipCard to use
     * @param x        the x-coordinate on the board
     * @param y        the y-coordinate on the board
     */
    public UseReservedShipCardAction(String username, ShipCard shipCard, int x, int y) {
        super(username);
        this.shipCard = shipCard;
        this.x = x;
        this.y = y;
    }

    /**
     * Executes the placement of the reserved ShipCard in the game context.
     * Resets the card orientation to DEG_0 before placement. On exception,
     * sends a NotifyExceptionAction with the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            ShipCard.Orientation orientation = shipCard.getOrientation();
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            ShipBoard shipBoard = context.useReservedShipCard(username, shipCard, orientation, x, y);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

/**
 * Action that allows a player to place a ShipCard on their ShipBoard at specified coordinates.
 * On success, sends an UpdateShipBoardAction with the updated board;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class PlaceShipCardAction extends ClientGameAction {
    private int x;
    private int y;
    private ShipCard shipCard;

    /**
     * Constructs a new PlaceShipCardAction with position and card.
     *
     * @param username the name of the player placing the card
     * @param x        the x-coordinate on the board
     * @param y        the y-coordinate on the board
     * @param shipCard the ShipCard to be placed
     */
    public PlaceShipCardAction(String username, int x, int y, ShipCard shipCard) {
        super(username);
        this.x = x;
        this.y = y;
        this.shipCard = shipCard;
    }

    /**
     * Constructs a new PlaceShipCardAction without specifying position or card.
     * Useful for placeholder or delayed initialization.
     *
     * @param username the name of the player
     */
    public PlaceShipCardAction(String username) {
        super(username);
    }

    /**
     * Executes the placement of the ShipCard in the game context.
     * The card is reset to orientation DEG_0 before placement call,
     * then the board returned is sent back via UpdateShipBoardAction.
     * If an exception occurs, a NotifyExceptionAction is sent instead.
     *
     * @param context the GameContext in which to perform the placement
     */
    @Override
    public void execute(GameContext context) {
        try {
            ShipCard.Orientation orientation = shipCard.getOrientation();
            shipCard.setOrientation(ShipCard.Orientation.DEG_0);
            ShipBoard shipBoard = context.placeShipCard(username, shipCard, orientation, x, y);
            UpdateShipBoardAction response = new UpdateShipBoardAction(shipBoard);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

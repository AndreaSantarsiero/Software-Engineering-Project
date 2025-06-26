package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.LoadACoolShipAction;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;


/**
 * Action that allows a player to request a "cool" ship board from the server.
 * On success, sends a LoadACoolShipAction containing the retrieved ShipBoard;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class GimmeACoolShipAction extends ClientGameAction {

    private final int num;

    /**
     * Constructs a new GimmeACoolShipAction.
     *
     * @param username the name of the player requesting the ship
     * @param num      the index or identifier of the cool ship configuration
     */
    public GimmeACoolShipAction(String username, int num) {
        super(username);
        this.num = num;
    }

    /**
     * Executes the request for a cool ship in the game context.
     * - Calls context.gimmeACoolShip(username, num) to retrieve a ShipBoard.
     * - Sends a LoadACoolShipAction with the retrieved board to the requester.
     * - On exception, sends a NotifyExceptionAction containing the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try{
            ShipBoard coolShip = context.gimmeACoolShip(username, num);
            LoadACoolShipAction response = new LoadACoolShipAction(coolShip);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

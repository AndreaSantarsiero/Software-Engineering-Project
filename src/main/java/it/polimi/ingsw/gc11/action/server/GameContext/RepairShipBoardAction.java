package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import java.util.List;


/**
 * Action that allows a player to repair their ship by removing damaged cards at specified coordinates.
 * On success, the controller sends the appropriate success notification;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class RepairShipBoardAction extends ClientGameAction {

    private final List<Integer> cardsToEliminateX;
    private final List<Integer> cardsToEliminateY;

    /**
     * Constructs a new RepairShipBoardAction.
     *
     * @param username              the name of the player performing the repair
     * @param cardsToEliminateX     the x-coordinates of damaged cards to remove
     * @param cardsToEliminateY     the y-coordinates of damaged cards to remove
     */
    public RepairShipBoardAction(String username, List<Integer> cardsToEliminateX, List<Integer> cardsToEliminateY) {
        super(username);
        this.cardsToEliminateX = cardsToEliminateX;
        this.cardsToEliminateY = cardsToEliminateY;
    }

    /**
     * Executes the repair action in the game context.
     * <ul>
     *   <li>Calls context.repairShip with the player's username and lists of coordinates.</li>
     *   <li>On exception, sends a NotifyExceptionAction containing the error message.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.repairShip(username, cardsToEliminateX, cardsToEliminateY);
            //success action sent by the controller
        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

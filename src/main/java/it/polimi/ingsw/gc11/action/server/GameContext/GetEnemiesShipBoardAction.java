package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.Map;


/**
 * Action that allows a player to request the ship boards of all enemies.
 * On success, sends an UpdateEnemyShipBoardAction for each enemy board,
 * followed by a NotifySuccessAction. On failure, sends a NotifyExceptionAction.
 */
public class GetEnemiesShipBoardAction extends ClientGameAction {

    /**
     * Constructs a new GetEnemiesShipBoardAction for the specified player.
     *
     * @param username the name of the player requesting enemy ship boards
     */
    public GetEnemiesShipBoardAction(String username) {
        super(username);
    }


    /**
     * Executes the retrieval of enemy ship boards in the game context.
     * - Calls context.getPlayersShipBoard() and removes the requester’s board.
     * - Sends UpdateEnemyShipBoardAction(username, board) for each remaining entry.
     * - Sends NotifySuccessAction on successful completion.
     * - On exception, sends NotifyExceptionAction with the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Map<String, ShipBoard> enemiesShipBoard = context.getPlayersShipBoard();
            enemiesShipBoard.remove(this.username);

            for(Map.Entry<String, ShipBoard> entry : enemiesShipBoard.entrySet()) {
                UpdateEnemyShipBoardAction response = new UpdateEnemyShipBoardAction(entry.getValue(), entry.getKey());
                context.sendAction(username, response);
            }

            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

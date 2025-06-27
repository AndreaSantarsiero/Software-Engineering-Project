package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.SendHitAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;


/**
 * Action that allows a player to retrieve the next hit coordinate from the game context.
 * On success, broadcasts a SendHitAction with the Hit to all non-aborted players
 * and then sends a NotifySuccessAction to the requester.
 * In case of error, sends a NotifyExceptionAction with the error message.
 */
public class GetCoordinateAction extends ClientGameAction {

    /**
     * Constructs a new GetCoordinateAction for the specified player.
     *
     * @param username the name of the player requesting coordinates
     */
    public GetCoordinateAction(String username) {
        super(username);
    }


    /**
     * Executes the coordinate retrieval in the game context.
     * - Calls context.getCoordinate(username) to get a Hit.
     * - Sends SendHitAction(hit) to all non-aborted players.
     * - Sends NotifySuccessAction to the requester.
     * On exception, sends NotifyExceptionAction with the exception message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Hit hit = context.getCoordinate(username);

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                SendHitAction response = new SendHitAction(hit);
                context.sendAction(p.getUsername(), response);
            }

            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}


package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayersColorAction;
import java.util.Map;


/**
 * Action that allows a player to request the current mapping of all players' colors.
 * On success, sends an UpdatePlayersColorAction with the color mapping;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class GetPlayersColorAction extends ClientGameAction {

    /**
     * Constructs a new GetPlayersColorAction for the specified player.
     *
     * @param username the name of the player requesting the color mapping
     */
    public GetPlayersColorAction(String username) {
        super(username);
    }

    /**
     * Executes the retrieval of player colors in the game context.
     * - Calls context.getPlayersColor() to obtain the mapping.
     * - Sends an UpdatePlayersColorAction to the requester with the map.
     * - On exception, sends a NotifyExceptionAction containing the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Map<String, String> playersColor = context.getPlayersColor();
            UpdatePlayersColorAction response = new UpdatePlayersColorAction(playersColor);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

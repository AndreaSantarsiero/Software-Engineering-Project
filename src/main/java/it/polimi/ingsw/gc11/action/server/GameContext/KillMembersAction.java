package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Arrays;
import java.util.Map;


/**
 * Action that allows a player to kill crew members according to housing usage limits.
 * On success, updates the profiles of the acting player and all other players with the new state.
 * On failure, sends a NotifyExceptionAction containing the error message and stack trace.
 */
public class KillMembersAction extends ClientGameAction {

    private final Map<HousingUnit, Integer> housingUsage;

    /**
     * Constructs a new KillMembersAction.
     *
     * @param username     the name of the player performing the kill action
     * @param housingUsage the allocation of members to remove per HousingUnit
     */
    public KillMembersAction(String username, Map<HousingUnit, Integer> housingUsage) {
        super(username);
        this.housingUsage = housingUsage;
    }

    /**
     * Executes the kill members action in the game context.
     * <ol>
     *   <li>Calls context.killMembers to perform the removals and returns the updated Player.</li>
     *   <li>Determines the new current player.</li>
     *   <li>Sends UpdatePlayerProfileAction to the acting player with updateState=true.</li>
     *   <li>Sends UpdateEnemyProfileAction to all other non-aborted players with updateState=false.</li>
     * </ol>
     * On exception, catches it and sends a NotifyExceptionAction with the exception message and stack trace.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.killMembers(getUsername(), housingUsage);

            String newCurrentPlayer = context.getCurrentPlayer().getUsername();
            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, newCurrentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, newCurrentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}


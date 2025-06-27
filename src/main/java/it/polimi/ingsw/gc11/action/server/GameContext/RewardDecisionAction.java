package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;


/**
 * Action that allows a player to accept or decline a reward decision.
 * On success, updates the profile of the acting player and notifies all other players.
 * On failure, sends a NotifyExceptionAction containing the error message.
 */
public class RewardDecisionAction extends ClientGameAction {

    private final boolean decision;

    /**
     * Constructs a new RewardDecisionAction.
     *
     * @param username the name of the player making the reward decision
     * @param decision {@code true} to accept the reward, {@code false} to decline
     */
    public RewardDecisionAction(String username, boolean decision) {
        super(username);
        this.decision = decision;
    }

    /**
     * Executes the reward decision in the game context.
     * <ul>
     *   <li>Calls {@code context.rewardDecision(username, decision)} to apply the decision.</li>
     *   <li>Retrieves the updated Player object and the new current player.</li>
     *   <li>Sends {@link UpdatePlayerProfileAction} to the deciding player with a full state update.</li>
     *   <li>Sends {@link UpdateEnemyProfileAction} to all other non-aborted players without state update.</li>
     *   <li>On exception, sends {@link NotifyExceptionAction} with the error message to the requester.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.rewardDecision(username, decision);
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, currentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, currentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

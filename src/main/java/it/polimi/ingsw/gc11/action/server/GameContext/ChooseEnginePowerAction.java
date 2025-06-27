package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Arrays;
import java.util.Map;


/**
 * Action that allows a player to choose their engine power distribution across batteries.
 * On success, updates the profiles of the choosing player and all other players with the new state.
 * In case of error, sends a NotifyExceptionAction containing exception details to the requester.
 */
public class ChooseEnginePowerAction extends ClientGameAction {

    private final Map<Battery, Integer> batteries;

    /**
     * Constructs a new ChooseEnginePowerAction.
     *
     * @param username  the name of the player choosing engine power
     * @param batteries a map of Battery enums to allocated power values
     */
    public ChooseEnginePowerAction(String username, Map<Battery, Integer> batteries) {
        super(username);
        this.batteries = batteries;
    }

    /**
     * Executes the engine power choice in the game context.
     * Retrieves the updated Player object, then notifies all non-aborted players:
     * - The chooser with UpdatePlayerProfileAction
     * - Others with UpdateEnemyProfileAction
     * On exception, sends a NotifyExceptionAction with the message and stack trace.
     *
     * @param context the GameContext in which to apply the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.chooseEnginePower(getUsername(), batteries);
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
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}


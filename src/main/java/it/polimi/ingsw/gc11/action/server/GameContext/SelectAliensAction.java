package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.AlienUnit;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;


/**
 * Action that allows a player to select an AlienUnit and assign it to a HousingUnit.
 * On success, updates the player’s profile for the chooser and enemy profiles for others.
 * On failure, sends a NotifyExceptionAction with the error message.
 */
public class SelectAliensAction extends ClientGameAction {

    private final AlienUnit alienUnit;
    private final HousingUnit housingUnit;

    /**
     * Constructs a new SelectAliensAction.
     *
     * @param username    the name of the player selecting aliens
     * @param alienUnit   the AlienUnit to place
     * @param housingUnit the HousingUnit where the alien will reside
     */
    public SelectAliensAction(String username, AlienUnit alienUnit, HousingUnit housingUnit) {
        super(username);
        this.alienUnit = alienUnit;
        this.housingUnit = housingUnit;
    }

    /**
     * Executes the alien selection in the game context.
     * <ul>
     *   <li>Calls {@code context.selectAliens(username, alienUnit, housingUnit)}.</li>
     *   <li>Retrieves the updated Player object.</li>
     *   <li>Sends {@link UpdatePlayerProfileAction} to the chooser with no state update.</li>
     *   <li>Sends {@link UpdateEnemyProfileAction} to all other non-aborted players with no state update.</li>
     *   <li>On exception, sends {@link NotifyExceptionAction} with the error message.</li>
     * </ul>
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            context.selectAliens(username, alienUnit, housingUnit);
            Player player = context.getGameModel().getPlayer(username);

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, null);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, null);
                    context.sendAction(p.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

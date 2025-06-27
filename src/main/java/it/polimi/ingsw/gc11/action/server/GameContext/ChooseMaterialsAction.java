package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;


/**
 * Action that allows a player to choose which materials to move into or out of storage.
 * On success, updates the profiles of the choosing player and all other players with the new state.
 * On failure, sends a NotifyExceptionAction containing the error message to the requester.
 */
public class ChooseMaterialsAction extends ClientGameAction {

    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;

    /**
     * Constructs a new ChooseMaterialsAction for the given player.
     *
     * @param username         the name of the player choosing materials
     * @param storageMaterials the mapping of storage to material adjustments
     */
    public ChooseMaterialsAction(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        super(username);
        this.storageMaterials = storageMaterials;
    }

    /**
     * Executes the material selection in the game context.
     * Retrieves the updated Player object, then notifies all non-aborted players:
     * - The chooser with UpdatePlayerProfileAction
     * - Others with UpdateEnemyProfileAction
     * In case of exception, sends a NotifyExceptionAction back to the requester.
     *
     * @param context the GameContext in which to apply the action
     */
    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.chooseMaterials(username, storageMaterials);

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
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}


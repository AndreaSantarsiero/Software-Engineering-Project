package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;



public class ChooseMaterialsAction extends ClientGameAction {

    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;


    public ChooseMaterialsAction(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        super(username);
        this.storageMaterials = storageMaterials;
    }


    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.chooseMaterials(username, storageMaterials);
            String currentPlayer = context.getCurrentPlayerUsername().getUsername();

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(player.getUsername().equals(username)) {
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


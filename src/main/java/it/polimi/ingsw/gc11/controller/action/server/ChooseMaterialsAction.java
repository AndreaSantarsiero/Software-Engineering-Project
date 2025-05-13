package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;



public class ChooseMaterialsAction extends ClientAction {
    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;

    public ChooseMaterialsAction(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        super(username);
        this.storageMaterials = storageMaterials;
    }

    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.chooseMaterials(getUsername(), storageMaterials);

            if (player.getUsername().equals(username)) {
                UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
                context.sendAction(username, response);
            }
            else {
                UpdateEnemyShipBoardAction response1 = new UpdateEnemyShipBoardAction(player.getShipBoard(), username);
                context.sendAction(username, response1);

                UpdateEnemyProfileAction response2 = new UpdateEnemyProfileAction(player);
                context.sendAction(username, response2);
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}


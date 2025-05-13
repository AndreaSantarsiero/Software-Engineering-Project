package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;



public class ChoosePlanetMaterialsAction extends ClientAction {
    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;

    public ChoosePlanetMaterialsAction(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        super(username);
        this.storageMaterials = storageMaterials;
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.chooseMaterials(getUsername(), storageMaterials);
    }
}


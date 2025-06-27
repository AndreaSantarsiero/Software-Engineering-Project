package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;


/**
 * Action that allows a player to move materials into or out of a planet's storage.
 * Delegates the material selection to the game context without further notifications.
 */
public class ChoosePlanetMaterialsAction extends ClientGameAction {
    private final Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials;

    /**
     * Constructs a new ChoosePlanetMaterialsAction for the specified player.
     *
     * @param username         the name of the player choosing planet materials
     * @param storageMaterials the mapping of storage to material adjustments
     */
    public ChoosePlanetMaterialsAction(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        super(username);
        this.storageMaterials = storageMaterials;
    }

    /**
     * Executes the material movement on the planet by invoking the game context's
     * chooseMaterials method with the provided storage adjustments.
     *
     * @param ctx the GameContext in which to apply the material changes
     */
    @Override
    public void execute(GameContext ctx) {
        ctx.chooseMaterials(getUsername(), storageMaterials);
    }
}


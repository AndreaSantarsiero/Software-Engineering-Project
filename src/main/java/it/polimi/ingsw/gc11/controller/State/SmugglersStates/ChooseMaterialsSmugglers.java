package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ChooseMaterialsSmugglers extends AdventureState {

    private final Smugglers smugglers;
    private final GameModel gameModel;
    private final Player player;



    public  ChooseMaterialsSmugglers(AdventurePhase advContext, Player player) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.smugglers = (Smugglers) advContext.getDrawnAdvCard();
        this.player = player;
    }



    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        ArrayList<Material> availableMaterials = smugglers.getMaterials();
        for (Map.Entry<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            if(!availableMaterials.containsAll(entry.getValue().getKey())){
                throw new IllegalArgumentException("Materials not available");
            }
            availableMaterials.remove(entry.getValue().getKey());
        }
        player.getShipBoard().addMaterials(storageMaterials);

        gameModel.move(player.getUsername(), smugglers.getLostDays() * -1);

        //next state
        this.advContext.setAdvState(new IdleState(advContext));

        return player;
    }
}

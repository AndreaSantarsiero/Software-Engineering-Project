package it.polimi.ingsw.gc11.controller.State.AbandonedStationStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseMaterialStation extends AdventureState {

    AbandonedStation abandonedStation;
    GameModel gameModel;
    Player player;

    public ChooseMaterialStation(AdventurePhase advContext, Player player) {
        super(advContext);
        this.abandonedStation = (AbandonedStation) advContext.getDrawnAdvCard();
        this.gameModel = advContext.getGameModel();
        this.player = player;
    }

    @Override
    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        if(abandonedStation.isResolved()){
            throw new IllegalStateException("AbandonedStation already resolved");
        }
        if(player.getShipBoard().getMembers() < abandonedStation.getMembersRequired()){
            throw new IllegalStateException("Player does not have enough members");
        }

        ArrayList<Material> availableMaterials = abandonedStation.getMaterials();
        for (Map.Entry<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            if(!availableMaterials.containsAll(entry.getValue().getKey())){
                throw new IllegalArgumentException("Materials not available");
            }
            availableMaterials.remove(entry.getValue().getKey());
        }
        player.getShipBoard().addMaterials(storageMaterials);

        abandonedStation.resolveCard();
        gameModel.move(player.getUsername(), abandonedStation.getLostDays() * -1);

        //next state
        this.advContext.setAdvState(new IdleState(advContext));
    }
}

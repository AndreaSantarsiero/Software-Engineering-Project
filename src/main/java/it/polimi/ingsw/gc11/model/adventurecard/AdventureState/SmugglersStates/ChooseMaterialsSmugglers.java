package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.SmugglersStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseMaterialsSmugglers extends SmugglersState {

    private Smugglers smugglers;
    private GameModel gameModel;
    private Player player;

    public  ChooseMaterialsSmugglers(Smugglers smugglers, GameModel gameModel, Player player) {
        if(smugglers==null || gameModel==null || player==null){
            throw new NullPointerException();
        }

        this.smugglers = smugglers;
        this.gameModel = gameModel;
        this.player = player;
    }

    public void getMaterial(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){

        ArrayList<Material> availableMaterials = smugglers.getMaterials();
        for (Map.Entry<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            if(!availableMaterials.containsAll(entry.getValue().getKey())){
                throw new IllegalArgumentException("Materials not available");
            }
            availableMaterials.remove(entry.getValue().getKey());
        }
        player.getShipBoard().addMaterials(storageMaterials);


        if(player == gameModel.getLastPlayer()){
            //go to next state
        }
        else{
            //go to next state
        }
    }
}

package it.polimi.ingsw.gc11.controller.State.PlanetCardStates;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseMaterialsPlanet extends PlanetCardState{

    private PlanetsCard planetsCard;
    private Planet planet;
    private Player player;
    private GameModel gameModel;

    public ChooseMaterialsPlanet(PlanetsCard planetsCard, GameModel gameModel, Player player, Planet planet) {

        if(planetsCard==null || gameModel==null || player==null || planet==null){
            throw new NullPointerException();
        }

        this.planetsCard = planetsCard;
        this.gameModel = gameModel;
        this.player = player;
        this.planet = planet;
    }

    public void getMaterials(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        ArrayList<Material> availableMaterials = planetsCard.getMaterials(planet);
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

    @Override
    public void nextState(GameContext context) {

    }
}

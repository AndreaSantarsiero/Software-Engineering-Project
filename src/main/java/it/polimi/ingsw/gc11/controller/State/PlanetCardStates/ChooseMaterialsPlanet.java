package it.polimi.ingsw.gc11.controller.State.PlanetCardStates;

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
        this.planetsCard = planetsCard;
        this.gameModel = gameModel;
        this.player = player;
        this.planet = planet;
    }

    public void getMaterials(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        ArrayList<Material> availabeMaterials = planetsCard.getMaterials(planet);
        for (Map.Entry<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            if(!availabeMaterials.containsAll(entry.getValue().getKey())){
                throw new IllegalArgumentException("Materials not available");
            }
            availabeMaterials.remove(entry.getValue().getKey());
        }
        player.getShipBoard().addMaterials(storageMaterials);

    }
}

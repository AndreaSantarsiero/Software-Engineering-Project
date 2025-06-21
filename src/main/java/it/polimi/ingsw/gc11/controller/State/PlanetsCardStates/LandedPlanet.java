package it.polimi.ingsw.gc11.controller.State.PlanetsCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Storage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class LandedPlanet extends AdventureState {
    ArrayList<Material> materials;
    Player player;
    int numVisited;

    public LandedPlanet(AdventurePhase advContext, Player player, ArrayList<Material> materials, int numVisited) {
        super(advContext);
        this.player = player;
        this.materials = materials;
        this.numVisited = numVisited;
    }

    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        //cosa fa?
        for (Map.Entry<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            if(!materials.containsAll(entry.getValue().getKey())){
                throw new IllegalArgumentException("Materials not available");
            }
            materials.remove(entry.getValue().getKey());
        }
        //+controlli sulla mappa
        player.getShipBoard().addMaterials(storageMaterials);

        GameModel gameModel = this.advContext.getGameModel();
        PlanetsCard planetsCard = (PlanetsCard) this.advContext.getDrawnAdvCard();
        gameModel.move(player.getUsername(), planetsCard.getLostDays() * -1);

        this.advContext.setResolvingAdvCard(false);
        this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer()+1);

        //next state
        if (this.advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()  ||
            this.numVisited == planetsCard.getPlanets().size()) {
            //NoPlayersLeft or NoPlanetsLeft
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else {
            this.advContext.setAdvState(new PlanetsState(this.advContext, this.numVisited));
        }

        return player;
    }
}

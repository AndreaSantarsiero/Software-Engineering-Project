package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

//Ipotesi: questo è il PRIMO stato della advCard AbandonedShip,
//cioè quello che ottengo dal metodo initAdventureState() in AdventurePhase
public class ChooseHousing implements AdventureState {

    private AbandonedShip abandonedShip;
    private GameModel gameModel;
    private Player player;

    public ChooseHousing(AbandonedShip abandonedShip, GameModel gameModel, Player player) {
        if(abandonedShip == null ||  gameModel == null || player == null){
            throw new NullPointerException();
        }

        this.abandonedShip = abandonedShip;
        this.gameModel = gameModel;
        this.player = player;
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {
        advContext.setAdventureState(new ResolvedShip(abandonedShip, gameModel, player));
    }

    //Idea: farei ovveride e lo chiamerei resolveState()
    public void killMembers(Map<HousingUnit, Integer> housingUsage){
        if(abandonedShip.isResolved()){
            throw new IllegalStateException("Abandoned ship already resolved");
        }
        if(player.getShipBoard().getMembers() < abandonedShip.getLostMembers()){
            throw new IllegalStateException("Player does not have enough members");
        }

        player.getShipBoard().killMembers(housingUsage);
        abandonedShip.resolveCard();

        //go to next state
    }
}

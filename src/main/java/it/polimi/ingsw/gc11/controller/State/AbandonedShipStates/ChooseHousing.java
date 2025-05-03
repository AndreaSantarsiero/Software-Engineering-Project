package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

public class ChooseHousing extends AdventureState {
    private GameModel gameModel;
    private AbandonedShip abandonedShip;
    private Player player;

    public ChooseHousing(AdventurePhase advContext, Player player) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.abandonedShip = (AbandonedShip) this.advContext.getDrawnAdvCard();
        this.player = player;
    }

    //Idea: farei ovveride e lo chiamerei resolveState()
    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int sum = 0;
        for(HousingUnit housingUnit : housingUsage.keySet()){
            sum += housingUsage.get(housingUnit);
        }

        if(sum <= abandonedShip.getLostMembers()){
            throw new IllegalStateException("You are out of the game");
            //Il giocatore va eliminato dalla partita
        }

        if(abandonedShip.isResolved()){
            throw new IllegalStateException("Abandoned ship already resolved");
        }
        if(player.getShipBoard().getMembers() < abandonedShip.getLostMembers()){
            throw new IllegalStateException("Player does not have enough members");
        }

        player.getShipBoard().killMembers(housingUsage);
        abandonedShip.resolveCard();

        player.addCoins(abandonedShip.getCoins());
        gameModel.move(player.getUsername(), abandonedShip.getLostDays() * -1);

        //go to next state
        this.advContext.setAdvState(new IdleState(advContext));
    }
}

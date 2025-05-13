package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

public class Penalty2Lv1 extends AdventureState {
    Player player;
    public Penalty2Lv1(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
    }

    @Override
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){

        CombatZoneLv1 combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();
        int lostMembers = combatZoneLv1.getLostMembers();

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(player.getShipBoard().getMembers() < lostMembers){
            //Il giocatore va eliminato dalla partita
            throw new IllegalStateException("You don't have enough members... Game over");
        }

        int sum = 0;
        for(HousingUnit housingUnit : housingUsage.keySet()){
            sum += housingUsage.get(housingUnit);
        }

        if(sum <= lostMembers) {
            throw new IllegalStateException("You must select enough members to play");
        }

        player.getShipBoard().killMembers(housingUsage);

        //go to next state
        this.advContext.setAdvState(new Check3Lv1(advContext, 10000, null));

        return player;
    }


}

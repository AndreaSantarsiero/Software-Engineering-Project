package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.MeteorSwarnStates;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.HashMap;
import java.util.Map;

public class ChoseBatteriesMeteor {
    Player player;
    Map<Battery, Integer> batteriesToRemove = new HashMap<Battery, Integer>();
    MeteorSwarm meteorSwarm;

    public ChoseBatteriesMeteor(Player player, MeteorSwarm meteorSwarm) {
        if(player==null || meteorSwarm==null){
            throw new IllegalArgumentException();
        }
        this.player = player;
        this.meteorSwarm = meteorSwarm;
    }

    //method that ask the user to select which batteries use

    public void removeBatteries(Map<Battery,Integer> batteries){
        batteriesToRemove = batteries;
        player.getShipBoard().useBatteries(batteriesToRemove);
    }

    //next state
}

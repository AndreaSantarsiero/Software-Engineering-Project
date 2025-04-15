package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.MeteorSwarnStates;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;

public class ResolvedMeteor {
    Player player;
    MeteorSwarm meteorSwarm;

    public ResolvedMeteor(Player player, MeteorSwarm meteorSwarm) {
        if(player == null || meteorSwarm == null){
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.player = player;
        this.meteorSwarm = meteorSwarm;
    }
}

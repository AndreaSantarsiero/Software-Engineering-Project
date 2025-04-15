package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.EpidemicStates;

import it.polimi.ingsw.gc11.model.Player;

public class ResolvedEpidemic {
    Player player;
    public ResolvedEpidemic(Player player) {
        if(player==null){
            throw new NullPointerException("player");
        }
        this.player = player;
    }

    public void killMembers(){
        player.getShipBoard().epidemic();
    }
}

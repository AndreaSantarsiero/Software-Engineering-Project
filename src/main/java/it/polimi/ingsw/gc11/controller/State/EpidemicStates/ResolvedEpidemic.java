package it.polimi.ingsw.gc11.controller.State.EpidemicStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;

public class ResolvedEpidemic extends AdventureState {
    Player player;
    public ResolvedEpidemic(Player player) {
        if(player==null){
            throw new NullPointerException("player");
        }
        this.player = player;
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }

    public void killMembers(){
        player.getShipBoard().epidemic();
    }
}

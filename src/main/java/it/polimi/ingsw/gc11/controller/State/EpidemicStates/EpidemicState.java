package it.polimi.ingsw.gc11.controller.State.EpidemicStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.Player;



public class EpidemicState extends AdventureState {

    public EpidemicState(AdventurePhase advContext) {
        super(advContext);

        for (Player player : advContext.getGameModel().getPlayersNotAbort()) {
            player.getShipBoard().epidemic();
        }

        this.advContext.setAdvState(new IdleState(advContext));
    }

}

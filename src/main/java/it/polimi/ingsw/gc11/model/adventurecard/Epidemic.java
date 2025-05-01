package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.EpidemicStates.ResolvedEpidemic;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;



public class Epidemic extends AdventureCard {

    public Epidemic() {
        super(Type.LEVEL2);
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext, AbandonedShip advCard){
        return new ResolvedEpidemic(player);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}

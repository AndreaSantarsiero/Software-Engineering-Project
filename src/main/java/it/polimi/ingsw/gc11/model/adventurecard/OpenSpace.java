package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;



public class OpenSpace extends AdventureCard {

    public OpenSpace(AdventureCard.Type type) {
        super(type);
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new OpenSpaceState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }


}

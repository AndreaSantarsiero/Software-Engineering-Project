package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.EpidemicStates.EpidemicState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;



public class Epidemic extends AdventureCard {

    public Epidemic(String id) {
        super(id, Type.LEVEL2);
    }



    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new EpidemicState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    @Override
    public void getStates(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setStates(this);
    }
}

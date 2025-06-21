package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates.MeteorSwarmState;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;



public class MeteorSwarm extends AdventureCard {

    private final ArrayList<Meteor> meteors;



    public MeteorSwarm(String id, AdventureCard.Type type, ArrayList<Meteor> meteors) throws IllegalArgumentException {
        super(id, type);

        if (meteors == null){
            throw new IllegalArgumentException();
        }
        for(Meteor meteor: meteors) {
            if (meteor == null) {
                throw new NullPointerException("meteors is null.");
            }
        }
        this.meteors = meteors;
    }



    public  ArrayList<Meteor> getMeteors() {
        return meteors;
    }



    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new MeteorSwarmState(advContext, 0);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    @Override
    public void getInitialState(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setInitialState(this);
    }
}

package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.model.Shot;

import java.util.ArrayList;

public class MeteorSwarm extends AdventureCard {
    private ArrayList<Meteor> meteors;

    //can't use parameters to instance meteors inside the constructor because
    //they have too many attributes to describe them, easier with a list...
    // ...maybe it's ok with json file??
    public MeteorSwarm(AdventureCard.Type type, ArrayList<Meteor> meteors) throws IllegalArgumentException {
        super(type);

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

    @Override
    public void handler(GameModel model) {
        //to implement
    }
}

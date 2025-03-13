package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Meteor;

import java.util.ArrayList;

public class MeteorSwarm extends AdventureCard {
    private ArrayList<Meteor> meteors;

    //can't use parameters to instance meteors inside the constructor because
    //they have too many attributes to describe them, easier with a list...
    // ...maybe it's ok with json file??
    public MeteorSwarm(AdventureCard.Type type, ArrayList<Meteor> meteors) {
        super(type);
        this.meteors = meteors;
    }

//    @Override
//    public void handler() {
//        //to implement
//    }
}

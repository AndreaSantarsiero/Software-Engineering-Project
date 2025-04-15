package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Meteor;

import java.util.ArrayList;

public class MeteorSwarm extends AdventureCard {
    private final ArrayList<Meteor> meteors;

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

    public  ArrayList<Meteor> getMeteors() {
        return meteors;
    }

    @Override
    public void accept(AdventureCardVisitor adventureCardVisitor) {
        adventureCardVisitor.visit(this);
    }

}

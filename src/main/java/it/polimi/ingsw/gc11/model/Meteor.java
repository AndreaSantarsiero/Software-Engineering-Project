package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;



public class Meteor extends Hit {

    public Meteor(Hit.Type type, Hit.Direction direction) {
        super(type, direction);
    }


    @Override
    public void print(AdventureTemplate adventureTemplate){
        adventureTemplate.setHitType(this);
    }
}

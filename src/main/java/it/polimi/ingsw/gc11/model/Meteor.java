package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;


/**
 * Represents a meteor-type hit that can strike the spaceship.
 * A {@code Meteor} is a specific instance of {@link Hit}, with a given type and direction.
 *
 * <p>This class is used to model meteors impacting the game board,
 * and it can be displayed through an {@link AdventureTemplate} in the command-line interface.</p>
 */
public class Meteor extends Hit {

    /**
     * Constructs a new {@code Meteor} with the specified type and direction.
     *
     * @param type      the type of the meteor (BIG or SMALL)
     * @param direction the direction from which the meteor approaches (TOP, RIGHT, BOTTOM, LEFT)
     */
    public Meteor(Hit.Type type, Hit.Direction direction) {
        super(type, direction);
    }


    /**
     * Displays the meteor using the provided {@link AdventureTemplate}.
     * This method calls {@code setHitType} on the template, passing itself as the argument.
     *
     * @param adventureTemplate the CLI template used to render the meteor
     */
    @Override
    public void print(AdventureTemplate adventureTemplate){
        adventureTemplate.setHitType(this);
    }
}

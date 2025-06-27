package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;


/**
 * Represents a shot that can strike the game board.
 * This is a concrete implementation of the abstract {@link Hit} class.
 *
 * <p>A {@code Shot} is defined by a type (BIG or SMALL) and a direction
 * (TOP, RIGHT, BOTTOM, LEFT) from which it originates. It is used to model
 * damage-causing events initiated by the player or the game system.</p>
 */
public class Shot extends Hit {

    /**
     * Constructs a new {@code Shot} with the specified type and direction.
     *
     * @param type      the type of the shot (BIG or SMALL)
     * @param direction the direction from which the shot originates (TOP, RIGHT, BOTTOM, LEFT)
     */
    public Shot(Hit.Type type, Hit.Direction direction) {
        super(type, direction);
    }

    /**
     * Displays the shot using the provided {@link AdventureTemplate}.
     * This method is called to graphically render the shot in the CLI view.
     *
     * @param adventureTemplate the template used to render the shot in the CLI
     */
    @Override
    public void print(AdventureTemplate adventureTemplate){
        adventureTemplate.setHitType(this);
    }
}

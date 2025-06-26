package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;


/**
 * Action that allows a player to land on a specified planet.
 * On success, sends a NotifySuccessAction to the player;
 * on failure, sends a NotifyExceptionAction with the error message.
 */
public class LandOnPlanetAction extends ClientGameAction {

    private final int numPlanet;

    /**
     * Constructs a new LandOnPlanetAction.
     *
     * @param username  the name of the player landing on a planet
     * @param numPlanet the identifier of the target planet
     */
    public LandOnPlanetAction(String username, int numPlanet) {
        super(username);
        this.numPlanet = numPlanet;
    }

    /**
     * Executes the land-on-planet action in the game context.
     * - Calls context.landOnPlanet with the player’s username and planet index.
     * - On success, sends a NotifySuccessAction to the player.
     * - On exception, sends a NotifyExceptionAction containing the error message.
     *
     * @param context the GameContext in which to perform the action
     */
    @Override
    public void execute(GameContext context) {
        try{
            context.landOnPlanet(getUsername(), numPlanet);
            NotifySuccessAction action = new NotifySuccessAction();
            context.sendAction(username, action);
        }
        catch (Exception e){
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(getUsername());
            context.sendAction(username, exceptionAction);
        }
    }
}


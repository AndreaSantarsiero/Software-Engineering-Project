package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;



public class LandOnPlanetAction extends ClientGameAction {

    private final int numPlanet;


    public LandOnPlanetAction(String username, int numPlanet) {
        super(username);
        this.numPlanet = numPlanet;
    }


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


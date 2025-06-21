package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.model.Material;
import java.util.List;



public class LandOnPlanetAction extends ClientGameAction {

    private final int numPlanet;


    public LandOnPlanetAction(String username, int numPlanet) {
        super(username);
        this.numPlanet = numPlanet;
    }


    @Override
    public void execute(GameContext context) {
        try{
            List<Material> materials = context.landOnPlanet(getUsername(), numPlanet);
            //risposta da cambiare
            NotifySuccessAction notifySuccessAction = new NotifySuccessAction();
            context.sendAction(username, notifySuccessAction);
        }
        catch (Exception e){
            NotifyExceptionAction exceptionAction = new NotifyExceptionAction(getUsername());
            context.sendAction(username, exceptionAction);
        }
    }
}


package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class LandOnPlanetAction extends ClientAction {
    private final int numPlanet;

    public LandOnPlanetAction(String username, int numPlanet) {
        super(username);
        this.numPlanet = numPlanet;
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.landOnPlanet(getUsername(), numPlanet);
    }
}


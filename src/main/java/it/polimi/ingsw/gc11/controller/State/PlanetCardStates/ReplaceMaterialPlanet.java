package it.polimi.ingsw.gc11.controller.State.PlanetCardStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Player;

public class ReplaceMaterialPlanet extends AdventureState {
    private Player player;

    public ReplaceMaterialPlanet(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
    }

    @Override
    public void replaceMaterial(String username, ){

    }
}

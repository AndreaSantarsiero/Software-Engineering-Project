package it.polimi.ingsw.gc11.controller.State.StarDustStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class StarDustState extends AdventureState {
    public StarDustState(AdventurePhase advContext) {
        super(advContext);
        GameModel gameModel = this.advContext.getGameModel();
        ArrayList<Player> reverseOrderPlayers = new ArrayList<Player> (gameModel.getPlayers());
        Collections.reverse(reverseOrderPlayers);
        for (Player player : reverseOrderPlayers) {
            int numExposedConnectors = player.getShipBoard().getExposedConnectors();
            String username = player.getUsername();
            gameModel.move(username, numExposedConnectors);
        }
        this.advContext.setAdvState(new IdleState(advContext));
    }
}

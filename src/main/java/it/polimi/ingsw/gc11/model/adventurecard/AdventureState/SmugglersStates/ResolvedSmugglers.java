package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.SmugglersStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;

public class ResolvedSmugglers extends SmugglersState {
    private Smugglers smugglers;
    private GameModel gameModel;
    private Player player;

    public ResolvedSmugglers(Smugglers smugglers, GameModel gameModel, Player player) {
        if(smugglers == null ||  gameModel == null || player == null){
            throw new NullPointerException();
        }

        this.smugglers = smugglers;
        this.gameModel = gameModel;
        this.player = player;
    }

    public void resolve(){
        gameModel.move(player.getUsername(), smugglers.getLostDays() * -1);
        //go to next state
    }
}

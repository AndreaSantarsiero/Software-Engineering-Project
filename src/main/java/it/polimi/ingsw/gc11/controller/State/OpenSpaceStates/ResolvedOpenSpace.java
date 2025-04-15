package it.polimi.ingsw.gc11.controller.State.OpenSpaceStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.OpenSpace;

public class ResolvedOpenSpace extends OpenSpaceState {
    private OpenSpace openSpace;
    private GameModel gameModel;
    private Player player;
    private int enginePower;

    public ResolvedOpenSpace(OpenSpace openSpace, GameModel gameModel, Player player,  int enginePower) {
        if(openSpace == null || gameModel == null || player == null) {
            throw new NullPointerException();
        }
        if(enginePower < 0) {
            throw new IllegalArgumentException();
        }
        this.openSpace = openSpace;
        this.gameModel = gameModel;
        this.player = player;
        this.enginePower = enginePower;
    }

    public void resolve(){
        gameModel.move(player.getUsername(), enginePower);

        if(player == gameModel.getLastPlayer()){
            //go to next state
        }
        else{
            //go to next state
        }
    }
}

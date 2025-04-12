package it.polimi.ingsw.gc11.controller.State.AbandonedStationStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;

public class ResolvedStation extends AbandonedStationState{

    private AbandonedStation abandonedStation;
    private GameModel gameModel;
    private Player player;

    public ResolvedStation(AbandonedStation abandonedStation, GameModel gameModel, Player player){
        if(abandonedStation == null || gameModel == null || player == null){
            throw new NullPointerException();
        }

        this.abandonedStation = abandonedStation;
        this.gameModel = gameModel;
        this.player = player;
    }

    public void resolve(){
        gameModel.move(player.getUsername(), abandonedStation.getLostDays());

        if(player == gameModel.getLastPlayer()){
            //go to next state
        }
        else{
            //go to next state
        }
    }
}

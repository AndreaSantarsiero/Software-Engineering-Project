package it.polimi.ingsw.gc11.controller.State.AbandonedStationStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedStation;

public class ResolvedStation implements AdventureState {

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

    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }

    public void resolve(){
        gameModel.move(player.getUsername(), abandonedStation.getLostDays() * -1);

        if(player == gameModel.getLastPlayer()){
            //go to next state
        }
        else{
            //go to next state
        }
    }
}

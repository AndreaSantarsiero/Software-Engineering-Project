package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;



public class Check2Lv1 extends AdventureState {
    GameModel gameModel;
    int minEnginePower;
    Player minPlayer;

    public Check2Lv1(AdventurePhase advContext, int minEnginePower, Player minPlayer ) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.minEnginePower = minEnginePower;
        this.minPlayer = minPlayer;
    }

    @Override
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        int usedBatteries = 0;

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            usedBatteries += entry.getValue();
        }

        player.getShipBoard().useBatteries(Batteries);


        int enginePower = player.getShipBoard().getEnginesPower(usedBatteries);

        if (enginePower < this.minEnginePower){
            this.minEnginePower = enginePower;
            minPlayer = player;
        }


        this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer()+1);

        if(this.advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()){
            //NoPlayersLeft
            this.advContext.setAdvState(new Penalty2Lv1(this.advContext, this.minPlayer));
        }
        //Rimane allo stato corrente

        return player;
    }
}
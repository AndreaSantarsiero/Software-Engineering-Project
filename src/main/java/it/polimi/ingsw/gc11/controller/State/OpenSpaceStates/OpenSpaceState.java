package it.polimi.ingsw.gc11.controller.State.OpenSpaceStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;



public class OpenSpaceState extends AdventureState {
    private GameModel gameModel;

    public OpenSpaceState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
    }

    @Override
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());
        int usedBatteries = 0;

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        //Imposto che il giorcatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);

        if(Batteries == null){
            usedBatteries = 0;
        }
        else{
            for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
                usedBatteries += entry.getValue();
            }

            player.getShipBoard().useBatteries(Batteries);
        }

        int enginePower = player.getShipBoard().getEnginesPower(usedBatteries);

        this.gameModel.move(username, enginePower);

        this.advContext.setResolvingAdvCard(false);
        this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer()+1);

        if(this.advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()){
            //NoPlayersLeft
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else{
            this.advContext.setAdvState(new OpenSpaceState(this.advContext));
        }

        return player;
    }
}
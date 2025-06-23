package it.polimi.ingsw.gc11.controller.State.OpenSpaceStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;



public class OpenSpaceState extends AdventureState {

    private final GameModel gameModel;



    public OpenSpaceState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
    }



    @Override
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        int usedBatteries = 0;

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(Batteries == null){
            throw new IllegalArgumentException("batteries is null");
        }

        //Sets that the player is currently playing this card
        if(this.advContext.isResolvingAdvCard()){
            throw new IllegalStateException("You have already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);


        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            usedBatteries += entry.getValue();
        }

        int enginePower = player.getShipBoard().getEnginesPower(usedBatteries);
        player.getShipBoard().useBatteries(Batteries);

        this.gameModel.move(username, enginePower);
        this.advContext.setResolvingAdvCard(false);
        this.advContext.setIdxCurrentPlayer(this.advContext.getIdxCurrentPlayer()+1);

        if(this.advContext.getIdxCurrentPlayer() == gameModel.getPlayersNotAbort().size()){
            //NoPlayersLeft
            this.advContext.setAdvState(new IdleState(this.advContext));
        }
        else{
            this.advContext.setAdvState(new OpenSpaceState(this.advContext));
        }

        return player;
    }
}
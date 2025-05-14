package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;


public class SlaversState extends AdventureState {
    private GameModel gameModel;
    private Slavers slavers;
    private double playerFirePower;

    public SlaversState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
        this.playerFirePower = 0;
    }


    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        int sum = 0;
        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        //Imposto che il giorcatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);

        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException();
        }
        else{
            for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
                sum += entry.getValue();
            }

            if(sum != doubleCannons.size()){
                throw new IllegalArgumentException("Batteries and Double Cannons do not match");
            }

            player.getShipBoard().useBatteries(Batteries);
        }

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);

        if(playerFirePower > slavers.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinState(advContext, player));
        } else if (playerFirePower == slavers.getFirePower()) {

            //Imposto che la carta non è più giocata da nessun player e passo al prossimo player.
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new SlaversState(advContext));
        }
        else {//Go LoseState
            advContext.setAdvState(new LooseState(advContext, player));
        }

        return player;
    }

}


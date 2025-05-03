package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.controller.State.NextPlayer;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.LoseState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.SlaversState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.WinState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PiratesState extends AdventureState {
    private GameModel gameModel;
    private Pirates pirates;
    private List<Player> playersDefeated;
    private double playerFirePower;


    public PiratesState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.playersDefeated = new ArrayList<>();
        this.playerFirePower = 0;
    }


    @Override
    public void chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        if(this.advContext.getIdxCurrentPlayer() == this.advContext.getGameModel().getPlayers().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException();
        }

        int sum = 0;
        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            sum += entry.getValue();
        }

        if(sum != doubleCannons.size()){
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        player.getShipBoard().useBatteries(Batteries);

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);

        if(playerFirePower > pirates.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinAgainstPirates(advContext, player, playersDefeated));
        } else if (playerFirePower == pirates.getFirePower()) {
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new PiratesState(advContext));
        }
        else {//Go LoseState
            this.playersDefeated.add(player);

            if (this.advContext.getIdxCurrentPlayer() == this.advContext.getGameModel().getPlayers().size()) {
                advContext.setAdvState(new LoseAgainstPirates(advContext, this.playersDefeated));
            }
        }
    }
}

package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class PiratesState extends AdventureState {

    private final GameModel gameModel;
    private final Pirates pirates;
    private final List<Player> playersDefeated;
    private double playerFirePower;



    public PiratesState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.playersDefeated = new ArrayList<>();
        this.playerFirePower = 0;
    }



    public PiratesState(AdventurePhase advContext, List<Player> playersDefeated) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.playersDefeated = playersDefeated;
        this.playerFirePower = 0;
    }



    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {

        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(playersDefeated.contains(player)){
            throw new IllegalArgumentException("You already fight with pirate");
        }

        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException();
        }

        //Imposto che il giorcatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);


        int sum = 0;
        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            sum += entry.getValue();
        }

        //Potrebbe essere <= al posto che !=
        if(sum < doubleCannons.size()){
            this.advContext.setResolvingAdvCard(false);
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);


        if(playerFirePower > pirates.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinAgainstPirates(advContext, player, playersDefeated));
        } else if (playerFirePower == pirates.getFirePower()) {
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new PiratesState(advContext, playersDefeated));
        }
        else {
            this.playersDefeated.add(player);
            this.advContext.setResolvingAdvCard(false);

            if (this.advContext.getIdxCurrentPlayer() == this.advContext.getGameModel().getPlayers().size() - 1) {
                advContext.setAdvState(new CoordinateState(advContext, this.playersDefeated, 0));
            }
            else{
                advContext.setAdvState(new PiratesState(advContext, playersDefeated));
            }
        }

        return player;
    }
}

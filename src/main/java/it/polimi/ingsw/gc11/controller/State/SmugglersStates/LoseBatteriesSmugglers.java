package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;



public class LoseBatteriesSmugglers extends AdventureState{
    private Player player;
    private int numBatteries;

    public LoseBatteriesSmugglers(AdventurePhase advContext, Player player, int numBatteries) {
        super(advContext);
        this.player = player;
        this.numBatteries = numBatteries;
    }

    @Override
    public void useBatteries(String username, Map<Battery, Integer> batteries) {
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        //Imposto che il giorcatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);

        if(batteries == null){
            //Controllo che effettivamente il giocatore non abbia più batterie altrimenti gliele richiedo
            int num = player.getShipBoard().getTotalAvailableBatteries();

            if(num > 0){
                this.advContext.setAdvState(new LoseBatteriesSmugglers(advContext, player, numBatteries));
                throw new IllegalStateException("You don't have selected any batteries");
            }
            else{
                this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
                this.advContext.setAdvState(new SmugglersState(advContext));
            }
        }
        else{
            int availableBatteries = player.getShipBoard().getTotalAvailableBatteries();
            int selectedBatteries = 0;

            for(Map.Entry<Battery, Integer> entry : batteries.entrySet()){
                selectedBatteries += entry.getValue();
            }

            if(selectedBatteries < numBatteries && availableBatteries >= numBatteries){
                this.advContext.setAdvState(new LoseBatteriesSmugglers(advContext, player, numBatteries));
                throw new IllegalStateException("You don't have selected enough batteries");
            }
            else if(selectedBatteries < numBatteries && availableBatteries < numBatteries && selectedBatteries < availableBatteries) {

                this.advContext.setAdvState(new LoseBatteriesSmugglers(advContext, player, numBatteries));
                throw new IllegalStateException("You don't have selected enough batteries");
            }
            else{
                player.getShipBoard().useBatteries(batteries);
                this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);

                if(advContext.getIdxCurrentPlayer() == advContext.getGameModel().getPlayers().size()){
                    this.advContext.setAdvState(new IdleState(advContext));
                }
                else{
                    this.advContext.setResolvingAdvCard(false);
                    this.advContext.setAdvState(new SmugglersState(advContext));
                }
            }
        }
    }
}

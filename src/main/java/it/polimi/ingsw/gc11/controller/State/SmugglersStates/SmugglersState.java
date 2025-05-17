package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;

public class SmugglersState extends AdventureState {
    private GameModel gameModel;
    private Smugglers smugglers;
    private double playerFirePower;

    public SmugglersState(AdventurePhase advContext){
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.smugglers = (Smugglers) advContext.getDrawnAdvCard();
        this.playerFirePower = 0;
    }

    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {
        int sum = 0;
        Player player = gameModel.getPlayers().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException("Batteries and DoubleCannons cannot be null");
        }

        //Imposto che il giorcatore sta effettivamente giocando la carta
        if(this.advContext.isResolvingAdvCard() == true){
            throw new IllegalStateException("You are already accepted this adventure card!");
        }
        this.advContext.setResolvingAdvCard(true);


        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            sum += entry.getValue();
        }
        if(sum < doubleCannons.size()){
            this.advContext.setResolvingAdvCard(false);
            throw new IllegalArgumentException("Batteries and Double Cannons do not match");
        }

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(Batteries);



        if(playerFirePower > smugglers.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinSmugglersState(advContext, player));
        } else if (playerFirePower == smugglers.getFirePower()) {

            //Imposto che la carta non è più giocata da nessun player e passo al prossimo player.
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new SmugglersState(advContext));

        }
        else {// Gestisco la sconfitta
            int num = player.getShipBoard().removeMaterials(smugglers.getLostMaterials());

            if(num > 0){
                this.advContext.setResolvingAdvCard(false);
                this.advContext.setAdvState(new LooseBatteriesSmugglers(advContext, player, num));
            }
            else{
                this.advContext.setResolvingAdvCard(false);
                this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);

                if(advContext.getIdxCurrentPlayer() == gameModel.getPlayers().size()){
                    this.advContext.setAdvState(new IdleState(advContext));
                }
                else {
                    this.advContext.setAdvState(new SmugglersState(advContext));
                }

            }
        }

        return player;
    }
}

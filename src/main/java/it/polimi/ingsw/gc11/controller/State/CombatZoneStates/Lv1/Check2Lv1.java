package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;


/**
 * Represents the state within the Adventure Phase at Combat Zone Level 1,
 * where each player must determine their engine power using available batteries.
 *
 * <p>This state manages player interactions, validates engine power calculations,
 * tracks the player with the lowest engine power, and advances the state appropriately.</p>
 */
public class Check2Lv1 extends AdventureState {

    private final GameModel gameModel;
    private int minEnginePower;
    private Player minPlayer;



    /**
     * Constructs a Check2Lv1 state with the provided context, initial minimum engine power,
     * and the player initially identified as having the minimum power.
     *
     * @param advContext The current AdventurePhase context.
     * @param minEnginePower The initial minimum engine power to start comparisons.
     * @param minPlayer The initial player associated with the lowest engine power.
     */
    public Check2Lv1(AdventurePhase advContext, int minEnginePower, Player minPlayer ) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.minEnginePower = minEnginePower;
        this.minPlayer = minPlayer;
    }



    /**
     * Allows a player to choose how many batteries they will use to determine their ship's engine power.
     *
     * <p>This method calculates engine power based on battery usage, updates the minimum engine power
     * if applicable, advances the turn to the next player, and potentially transitions to the penalty state
     * if all players have completed their engine power checks.</p>
     *
     * @param username The username of the player currently taking action.
     * @param Batteries A map of batteries to the number of units each battery is being used.
     * @return The player object after updating their engine power and battery usage.
     * @throws IllegalArgumentException If the username does not correspond to the current player.
     */
    @Override
    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){

        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());
        int usedBatteries = 0;

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        for(Map.Entry<Battery, Integer> entry : Batteries.entrySet()){
            usedBatteries += entry.getValue();
        }

        int enginePower = player.getShipBoard().getEnginesPower(usedBatteries);
        player.getShipBoard().useBatteries(Batteries);

        if (enginePower < this.minEnginePower){
            this.minEnginePower = enginePower;
            minPlayer = player;
        }


        int idx = this.advContext.getIdxCurrentPlayer();

        if (idx + 1 == gameModel.getPlayersNotAbort().size()) {
            //NoPlayersLeft
            this.advContext.setAdvState(new Penalty2Lv1(this.advContext, this.minPlayer));
        }
        else{
            //The advState remains the same as before
            this.advContext.setIdxCurrentPlayer(idx+1);
        }

        return player;
    }
}
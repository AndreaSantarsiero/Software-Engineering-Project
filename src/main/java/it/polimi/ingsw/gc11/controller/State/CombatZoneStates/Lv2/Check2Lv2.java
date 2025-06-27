package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;


/**
 * Represents the second combat check state at Combat Zone Level 2 during the Adventure Phase.
 *
 * <p>Each player must choose how many batteries to use to power their engines.
 * The player with the lowest total engine power at the end of this phase receives a penalty.</p>
 */
public class Check2Lv2 extends AdventureState {

    private final GameModel gameModel;
    private int minEnginePower;
    private Player minPlayer;


    /**
     * Constructs a {@code Check2Lv2} state using the provided adventure context, initial minimum engine power,
     * and the corresponding player.
     *
     * @param advContext The current AdventurePhase context.
     * @param minEnginePower The initial minimum engine power.
     * @param minPlayer The player currently associated with the minimum engine power.
     */
    public Check2Lv2(AdventurePhase advContext, int minEnginePower, Player minPlayer ) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.minEnginePower = minEnginePower;
        this.minPlayer = minPlayer;
    }


    /**
     * Allows the current player to choose how many batteries to use for powering their engines.
     *
     * <p>After computing engine power based on battery usage, the method updates the minimum engine power
     * if applicable, and advances to either the next player or the penalty phase if all players have acted.</p>
     *
     * @param username The username of the player taking the action.
     * @param Batteries A map specifying how many units of each battery are being used.
     * @return The updated {@code Player} object after applying engine power usage.
     *
     * @throws IllegalArgumentException If the action is attempted by someone other than the current player.
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
            this.advContext.setAdvState(new Penalty2Lv2(this.advContext, this.minPlayer));
        }
        else{
            //The advState remains the same as before
            this.advContext.setIdxCurrentPlayer(idx+1);
        }

        return player;
    }
}
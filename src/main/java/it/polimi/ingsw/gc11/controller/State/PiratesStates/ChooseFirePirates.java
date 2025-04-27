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

/**
 * Represents the state where a player must decide how much firepower to use against the Pirates.
 *
 * <p>This state is part of the Adventure phase involving Pirate encounters.
 * The player attempts to defeat the Pirates by using cannons and activating batteries.
 * The outcome determines whether the player wins, loses and passes the challenge to the next player.</p>
 *
 * <p>Key behaviors:
 * <ul>
 *     <li>Calculates the total firepower available based on player's cannons and used batteries.</li>
 *     <li>Decides the transition to WinAgainstPirates or LoseAgainstPirates depending on firepower comparison.</li>
 *     <li>Accumulates defeated players if the encounter is sequential and the player loses.</li>
 * </ul>
 * </p>
 */

public class ChooseFirePirates implements AdventureState {
    Player player;
    GameModel gameModel;
    Pirates pirates;
    List<Player> playersDefeated;
    double playerFirePower;
    boolean lastPlayer;

    /**
     * Constructs the ChooseFirePirates state for a given player and Pirates encounter.
     *
     * @param player The player attempting to fire at the Pirates.
     * @param gameModel The game model containing the state of the game.
     * @param pirates The Pirates adventure card the player is facing.
     * @param lastPlayer True if this is the last player attempting the challenge; false otherwise.
     * @throws NullPointerException if any of the parameters are null.
     */
    public ChooseFirePirates(Player player, GameModel gameModel, Pirates pirates, boolean lastPlayer) {
        if(player == null || gameModel == null || pirates == null){
            throw new NullPointerException();
        }
        this.player = player;
        this.gameModel = gameModel;
        this.pirates = pirates;
        this.playersDefeated = new ArrayList<>();
        this.playerFirePower = 0;
        this.lastPlayer = lastPlayer;
    }

    /**
     * Computes the firepower the player will use against the Pirates.
     *
     * <p>This method applies the usage of batteries (which can increase the effective cannon usage)
     * and calculates the resulting firepower based on the player's available cannons.</p>
     *
     * @param Batteries A map indicating which batteries the player uses and how many.
     * @param doubleCannons A list of cannons that the player wishes to double (boosting their firepower).
     * @return The total firepower computed after batteries and cannon boosts are applied.
     * @throws NullPointerException if the parameters are null.
     */
    public double getFireAgainstPirates(Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {
        if(Batteries == null || doubleCannons == null){
            throw new NullPointerException();
        }
        player.getShipBoard().useBatteries(Batteries);

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);

        return playerFirePower;
    }

    /**
     * Returns the first player who was defeated by the Pirates.
     *
     * @return The first player in the defeated list.
     */
    public Player getFirstPlayerDefeated() {
        return playersDefeated.getFirst();
    }

    /**
     * Handles the transition to the next adventure state based on the outcome of the firepower comparison.
     *
     * <p>If the player's firepower exceeds the Pirates' firepower, the player wins and transitions
     * to {@link WinAgainstPirates}. If the player loses and it is not the last player's turn,
     * the defeated player is recorded and the next player will face the challenge.
     * If it is the last player's turn and there are defeated players,
     * the game transitions to {@link LoseAgainstPirates} for all defeated players.</p>
     *
     * @param advContext The AdventurePhase context controlling the state machine.
     */
    @Override
    public void nextAdvState(AdventurePhase advContext) {
        if(playerFirePower > pirates.getFirePower()){
            //VictoryState
            advContext.setAdventureState(new WinAgainstPirates(this.player, this.gameModel, this.pirates));
        }
        else if(playerFirePower < pirates.getFirePower() && !lastPlayer){
            playersDefeated.add(this.player);
            //Controller take the nextplayer, recall ChooseFirePirates(player2) and so on...
        }

        if(lastPlayer && !playersDefeated.isEmpty()){
            advContext.setAdventureState(new LoseAgainstPirates(this.playersDefeated, this.gameModel, this.pirates));
        }
    }
}

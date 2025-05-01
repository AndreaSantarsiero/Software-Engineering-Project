package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.NextPlayer;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;

/**
 * Represents the state where a player has successfully defeated the Pirates in an adventure.
 *
 * <p>Upon victory, the player is offered a choice:
 * <ul>
 *     <li>Accept coins as a reward and lose a certain number of days (i.e., advance fewer steps on the track).</li>
 *     <li>Alternatively, refuse the reward and avoid any penalty.</li>
 * </ul>
 * </p>
 *
 * <p>The {@link WinAgainstPirates} state reflects a successful outcome, but introduces a strategic decision
 * regarding immediate resources versus future positioning.</p>
 *
 * <p>The class assumes that the player will make a decision explicitly and that this decision will be passed to the controller logic.</p>
 *
 * @see Pirates
 * @see AdventurePhase
 * @see AdventureState
 */
public class WinAgainstPirates extends AdventureState {
    Player player;
    GameModel gameModel;
    Pirates pirates;

    /**
     * Constructs the WinAgainstPirates state.
     *
     * @param player The player who has defeated the Pirates.
     * @param gameModel The current game model instance.
     * @param pirates The Pirates adventure card defeated by the player.
     * @throws NullPointerException if any of the arguments are {@code null}.
     */
    public WinAgainstPirates(Player player, GameModel gameModel, Pirates pirates) {
        if(player == null || gameModel == null || pirates == null){
            throw new NullPointerException();
        }
        this.player = player;
        this.gameModel = gameModel;
        this.pirates = pirates;
    }

    /**
     * Processes the player's decision after defeating the Pirates.
     *
     * <p>If {@code decision} is {@code true}, the player:
     * <ul>
     *     <li>Receives a number of coins equal to {@link Pirates#getCoins()}.</li>
     *     <li>Moves backward on the track by a number of days specified by {@link Pirates#getLostDays()}.</li>
     * </ul>
     * </p>
     *
     * <p>If {@code decision} is {@code false}, no reward is given and no penalty is applied.</p>
     *
     * @param decision {@code true} if the player chooses to accept coins and suffer the penalty; {@code false} otherwise.
     */
    public void getCoinsAndLoseDays(boolean decision){
        if(decision){
            player.addCoins(pirates.getCoins());

            gameModel.move(player.getUsername(), pirates.getLostDays());
        }
    }


    @Override
    public void nextAdvState(AdventurePhase advContext) {
        advContext.setAdventureState(new NextPlayer(this.pirates));
    }
}

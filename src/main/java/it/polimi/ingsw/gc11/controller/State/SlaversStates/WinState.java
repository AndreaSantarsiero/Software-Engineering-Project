package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;

/**
 * Represents the game state where the player has defeated the Slavers
 * during the adventure phase.
 * <p>
 * In this state, the player can decide whether to collect a coin reward,
 * at the cost of losing days of flight. The state handles the reward allocation
 * and the penalty application accordingly.
 * </p>
 *
 * <p>
 * All constructor parameters must be non-null; otherwise, an {@link IllegalArgumentException} is thrown
 * to ensure a consistent and valid state.
 * </p>
 */
public class WinState implements AdventureState {
    Player player;
    GameModel gameModel;
    Slavers slavers;

    /**
     * Constructs a {@code WinState} with the specified player, game model, and Slavers encounter.
     *
     * @param player    the player who has defeated the Slavers; must not be {@code null}.
     * @param gameModel the current state of the game; must not be {@code null}.
     * @param slavers   the Slavers adventure card that was defeated; must not be {@code null}.
     * @throws IllegalArgumentException if any of the arguments is {@code null}.
     */
    public WinState(Player player, GameModel gameModel, Slavers slavers) {
        if(player == null || gameModel == null || slavers == null){
            throw new IllegalArgumentException();
        }
        this.player = player;
        this.gameModel = gameModel;
        this.slavers = slavers;
    }

    /**
     * Handles the player's decision to accept or refuse the reward for defeating the Slavers.
     * <p>
     * If the player accepts ({@code decision} is {@code true}), they receive the coins from the Slavers card
     * but also lose a certain number of days of flight.
     * If the player refuses, no changes are applied.
     * </p>
     *
     * @param decision {@code true} if the player decides to take the reward (and suffer the penalty),
     *                 {@code false} if they refuse.
     */
    public void getCoinsAndLoseDays(boolean decision){
        if(decision){
            player.addCoins(slavers.getCoins());

            gameModel.move(player.getUsername(), slavers.getLostDays());
        }
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }
}



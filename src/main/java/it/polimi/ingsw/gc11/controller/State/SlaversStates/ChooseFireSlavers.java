package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;

/**
 * Represents the state in the adventure phase where the player decides how much firepower
 * to allocate against the Slavers encounter.
 * <p>
 * This state is responsible for:
 * <ul>
 *     <li>Consuming selected batteries from the player's shipboard</li>
 *     <li>Calculating the player's firepower based on selected cannons</li>
 *     <li>Determining the outcome of the encounter based on the firepower comparison</li>
 * </ul>
 * </p>
 *
 * <p>
 * Upon instantiation, all parameters must be non-null; otherwise, a {@link NullPointerException} is thrown.
 * </p>
 *
 */
public class ChooseFireSlavers implements AdventureState {
    Player player;
    GameModel gameModel;
    Slavers slavers;
    double playerFirePower;

    /**
     * Constructs a {@code ChooseFireSlavers} state with the given player, game model, and slavers encounter.
     *
     * @param player    the player who is engaging the Slavers; must not be {@code null}.
     * @param gameModel the current game model; must not be {@code null}.
     * @param slavers   the Slavers adventure card being resolved; must not be {@code null}.
     * @throws NullPointerException if any of the arguments is {@code null}.
     */

    public ChooseFireSlavers(Player player, GameModel gameModel, Slavers slavers) {
        if(player == null || gameModel == null || slavers == null){
            throw new NullPointerException();
        }
        this.player = player;
        this.gameModel = gameModel;
        this.slavers = slavers;
        this.playerFirePower = 0;
    }

    /**
     * Calculates the player's firepower against the Slavers based on the selected batteries and cannons.
     * <p>
     * The method:
     * <ul>
     *     <li>Consumes the specified batteries from the player's shipboard</li>
     *     <li>Computes the total firepower considering the selected double cannons</li>
     * </ul>
     * </p>
     *
     * @param Batteries      a map associating each {@link Battery} to the quantity the player wants to use.
     * @param doubleCannons   a list of {@link Cannon} representing double cannons selected for the attack.
     * @return the total firepower value calculated for the attack.
     */
    public double getFireAgainstSlavers(Map<Battery, Integer> Batteries, List<Cannon> doubleCannons) {
        player.getShipBoard().useBatteries(Batteries);

        playerFirePower = player.getShipBoard().getCannonsPower(doubleCannons);

        return playerFirePower;
    }


    /**
     * Determines the next adventure state based on the player's firepower compared to the Slavers' firepower.
     * <p>
     * If the player's firepower exceeds that of the Slavers, transitions to a Victory state;
     * otherwise, transitions to a Lose state.
     * </p>
     *
     * @param advContext the adventure phase context managing the state transitions.
     */
    @Override
    public void nextAdvState(AdventurePhase advContext) {
        if(playerFirePower > slavers.getFirePower()){
            //VictoryState
            advContext.setAdventureState(new WinState(this.player, this.gameModel, this.slavers));
        }
        //Go LoseState
        advContext.setAdventureState(new LoseState(this.player, this.gameModel, this.slavers));
    }
}


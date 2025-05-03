package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
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
public class SlaversState extends AdventureState {
    private GameModel gameModel;
    private Slavers slavers;
    private double playerFirePower;

    public SlaversState(AdventurePhase advContext) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
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

        if(playerFirePower > slavers.getFirePower()){
            //VictoryState
            advContext.setAdvState(new WinState(advContext, player));
        } else if (playerFirePower == slavers.getFirePower()) {
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);
            advContext.setAdvState(new SlaversState(advContext));
        }
        else {//Go LoseState
            advContext.setAdvState(new LoseState(advContext, player));
        }
    }

}


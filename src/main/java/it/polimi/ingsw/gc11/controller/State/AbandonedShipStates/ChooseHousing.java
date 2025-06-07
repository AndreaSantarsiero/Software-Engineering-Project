package it.polimi.ingsw.gc11.controller.State.AbandonedShipStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AbandonedShip;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

import java.util.Map;

/**
 * Represents the state in which a player must choose which crew members to sacrifice
 * after accepting an {@link AbandonedShip} adventure card.
 * <p>
 * This state requires the player to remove a specific number of crew members from their
 * {@link ShipBoard} and then receives a reward (coins) in return. Once the action is complete,
 * the game transitions to the {@link IdleState}.
 */
public class ChooseHousing extends AdventureState {
    private GameModel gameModel;
    private AbandonedShip abandonedShip;
    private Player player;

    /**
     * Constructs a new {@code ChooseHousing} state.
     *
     * @param advContext the current adventure phase context.
     * @param player     the player who accepted the {@link AbandonedShip} and must now sacrifice members.
     */
    public ChooseHousing(AdventurePhase advContext, Player player) {
        super(advContext);
        this.gameModel = advContext.getGameModel();
        this.abandonedShip = (AbandonedShip) this.advContext.getDrawnAdvCard();
        this.player = player;
    }


    /**
     * Removes the required number of crew members from the player's ship board as specified,
     * gives coins as a reward, and moves the player's position back in time (days lost).
     * <p>
     * The number of members removed must be at least equal to the number required by the abandoned ship card.
     * <p>
     * After execution, the game returns to {@link IdleState}.
     *
     * @param username      the player performing the action.
     * @param housingUsage  a map specifying how many members to remove from each {@link HousingUnit}.
     * @return the updated {@link Player} object.
     * @throws IllegalArgumentException if:
     *         - it is not the calling player's turn,
     *         - the number of members selected is insufficient.
     * @throws IllegalStateException if the player has fewer members than required and is thus eliminated.
     */
    @Override
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(player.getShipBoard().getMembers() < abandonedShip.getLostMembers()){
            //Il giocatore va eliminato dalla partita
            throw new IllegalStateException("You don't have enough members... Game over");
        }

        int sum = 0;
        for(HousingUnit housingUnit : housingUsage.keySet()){
            sum += housingUsage.get(housingUnit);
        }

        if(sum < abandonedShip.getLostMembers()){
            throw new IllegalArgumentException("You don't select enough members");
        }


        player.getShipBoard().killMembers(housingUsage);
        player.addCoins(abandonedShip.getCoins());
        gameModel.move(player.getUsername(), abandonedShip.getLostDays() * -1);

        //go to next state
        this.advContext.setAdvState(new IdleState(advContext));

        return player;
    }
}

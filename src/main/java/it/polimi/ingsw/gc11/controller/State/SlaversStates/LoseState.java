package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

/**
 * Represents the game state where the player has lost the encounter against the Slavers
 * during the adventure phase.
 * <p>
 * In this state, the player suffers consequences such as the loss of crew members.
 * The class provides methods to handle the penalties associated with the defeat.
 * </p>
 *
 * <p>
 * All constructor parameters must be non-null; otherwise, an {@link IllegalArgumentException} is thrown
 * to enforce state consistency.
 * </p>
 **/

public class LoseState implements AdventureState {
    Player player;
    GameModel gameModel;
    Slavers slavers;

    /**
     * Constructs a {@code LoseState} with the specified player, game model, and Slavers encounter.
     *
     * @param player    the player who has been defeated by the Slavers; must not be {@code null}.
     * @param gameModel the current state of the game; must not be {@code null}.
     * @param slavers   the Slavers card involved in the encounter; must not be {@code null}.
     * @throws IllegalArgumentException if any argument is {@code null}.
     */
    public LoseState(Player player, GameModel gameModel, Slavers slavers) {
        if(player == null || gameModel == null || slavers == null){
            throw new IllegalArgumentException();
        }
        this.player = player;
        this.gameModel = gameModel;
        this.slavers = slavers;
    }

    /**
     * Applies the consequence of losing by removing crew members from the player's ship.
     * <p>
     * The number of crew members to be removed is specified for each {@link HousingUnit}.
     * This method delegates to the shipboard to actually remove the members.
     * </p>
     *
     * @param housingUsage a map where keys are {@link HousingUnit}s and values are the number
     *                     of crew members to eliminate from each unit.
     */
    public void killMembers(Map<HousingUnit, Integer> housingUsage){
        player.getShipBoard().killMembers(housingUsage);
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {
        throw new UnsupportedOperationException("Not supported");
    }
}

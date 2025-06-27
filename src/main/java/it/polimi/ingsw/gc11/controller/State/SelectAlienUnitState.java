package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.AlienUnit;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the state within the {@link AdventurePhase} where each player selects an {@link AlienUnit}
 * and places it into a {@link HousingUnit} on their {@link ShipBoard}.
 *
 * <p>This state is entered at the beginning of an adventure and remains active until all players
 * have completed their alien selection. Once every active player signals completion, the state
 * automatically transitions to {@link IdleState}.</p>
 *
 * <p>Responsibilities of this state include:</p>
 * <ul>
 *     <li>Allowing each player to select and place one alien unit</li>
 *     <li>Tracking which players have completed their selection</li>
 *     <li>Enforcing that no player may place more than one alien</li>
 * </ul>
 */
public class SelectAlienUnitState extends AdventureState {

    private final List<Player> playerFinished;



    /**
     * Constructs a new {@code SelectAlienUnitState} with a reference to the adventure context.
     *
     * @param advContext the {@link AdventurePhase} managing this state
     */
    public SelectAlienUnitState(AdventurePhase advContext) {
        super(advContext);
        this.playerFinished = new ArrayList<>();
    }


    /**
     * Marks the player as having completed their alien selection.
     * <p>
     * If all active (non-aborted) players have completed their selection, the state automatically
     * transitions to {@link IdleState}.
     *
     * @param username the username of the player completing the selection
     * @throws IllegalArgumentException if the player has already completed the selection
     */
    @Override
    public void completedAlienSelection(String username){
        GameModel gameModel = this.advContext.getGameModel();
        Player player = gameModel.getPlayer(username);

        if(!playerFinished.contains(player)){
            playerFinished.add(player);
        }
        else{
            throw new IllegalArgumentException("You have already finished the alien's selection");
        }

        if(playerFinished.size() == advContext.getGameModel().getPlayersNotAbort().size()){
            advContext.setAdvState(new IdleState(advContext));
        }
    }

    /**
     * Allows the player to assign an {@link AlienUnit} to a specified {@link HousingUnit} on their {@link ShipBoard}.
     * <p>
     * This method is valid only during the {@code SelectAlienUnitState}. Each player can perform this action
     * only once, before calling {@link #completedAlienSelection(String)}. Once a player is marked as finished,
     * further attempts will result in an exception.
     * </p>
     *
     * @param username     the username of the player performing the selection
     * @param alienUnit    the alien unit the player wants to place
     * @param housingUnit  the housing unit where the alien is to be placed
     * @throws IllegalArgumentException if the player has already completed alien selection
     */
    @Override
    public void selectAliens(String username, AlienUnit alienUnit, HousingUnit housingUnit) {
        GameModel gameModel = this.advContext.getGameModel();
        Player player = gameModel.getPlayer(username);
        ShipBoard shipBoard = player.getShipBoard();

        if (playerFinished.contains(player)) {
            throw new IllegalArgumentException("You have already finished the alien's selection");
        }

        shipBoard.connectAlienUnit(alienUnit, housingUnit);
    }
}



package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import java.util.List;


/**
 * Represents the state where a player has successfully defeated the pirates during the Pirate encounter.
 *
 * <p>In this state, the winning player is given the option to claim a reward. If they accept the reward,
 * they receive a number of coins specified by the {@link Pirates} card, and their ship advances forward
 * by a number of days (i.e., negative lost days). If they decline, no benefit is gained.</p>
 *
 * <p>After the decision, the state transitions as follows:
 * <ul>
 *     <li>If no players were defeated by the pirates, the game returns to {@link IdleState}.</li>
 *     <li>If there are defeated players, it transitions to {@link CoordinateState} to handle incoming pirate hits.</li>
 * </ul>
 *
 */
public class WinAgainstPirates extends AdventureState {

    private final Player player;
    private final List<Player> playersDefeated;
    private final GameModel gameModel;
    private final Pirates pirates;


    /**
     * Constructs a state where a player has won against the pirates and must decide on the reward.
     *
     * @param advContext The current AdventurePhase context.
     * @param player The player who won the fight.
     * @param playersDefeated The list of players who were defeated by the pirates.
     */
    public WinAgainstPirates(AdventurePhase advContext, Player player, List<Player> playersDefeated) {
        super(advContext);
        this.player = player;
        this.playersDefeated = playersDefeated;
        this.gameModel = this.advContext.getGameModel();
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
    }


    /**
     * Handles the decision of the winning player regarding whether to claim the reward.
     *
     * @param username The username of the player making the decision.
     * @param decision True if the player accepts the reward; false otherwise.
     * @return The updated {@link Player} object after the reward resolution.
     * @throws IllegalArgumentException If another player tries to act instead.
     */
    @Override
    public Player rewardDecision(String username, boolean decision){

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(decision){
            player.addCoins(pirates.getCoins());
            gameModel.move(player.getUsername(), pirates.getLostDays() * -1);
        }

        //NextState
        if(playersDefeated.isEmpty()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
        else {
            this.advContext.setAdvState(new CoordinateState(advContext, this.playersDefeated, 0));
        }

        return player;
    }

}

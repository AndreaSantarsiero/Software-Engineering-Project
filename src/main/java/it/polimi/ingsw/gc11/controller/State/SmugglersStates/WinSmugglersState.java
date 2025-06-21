package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;


/**
 * Adventure state triggered when a player wins a confrontation
 * against a {@link Smugglers} card.
 *
 * <p>In this state, the player is presented with a binary choice:
 * whether to accept the smugglers' materials reward.
 * If the player accepts, they gain movement on the main track and
 * proceed to the {@link ChooseMaterialsSmugglers} state to pick materials.
 * If the player declines, the state transitions directly to {@link IdleState}.</p>
 *
 * <p>This state is reached from {@link SmugglersState} when the player's firepower
 * exceeds that of the smugglers.</p>
 *
 * <h2>Invariants</h2>
 * <ul>
 *   <li>{@code player != null}</li>
 *   <li>{@code smugglers != null}</li>
 *   <li>{@code gameModel != null}</li>
 * </ul>
 */
public class WinSmugglersState extends AdventureState {

    GameModel gameModel;
    Player player;
    Smugglers smugglers;


    /**
     * Constructs the {@code WinSmugglersState} for a player who has successfully
     * defeated the smugglers in the combat phase.
     *
     * @param advContext the current adventure phase context
     * @param player the player who won the confrontation
     * @throws ClassCastException if the drawn adventure card is not a {@link Smugglers} card
     */
    public WinSmugglersState(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
        this.gameModel = advContext.getGameModel();
        this.smugglers = (Smugglers) advContext.getDrawnAdvCard();
    }

    /**
     * Processes the player's decision whether to accept the smugglers' reward.
     *
     * <p>If the player accepts:
     * <ul>
     *   <li>They gain movement on the main track equal to {@code -lostDays} from the card.</li>
     *   <li>The state transitions to {@link ChooseMaterialsSmugglers} to select the materials.</li>
     * </ul>
     * If the player refuses:
     * <ul>
     *   <li>The game skips the reward and moves directly to {@link IdleState}.</li>
     * </ul>
     *
     * @param username the username of the player making the decision
     * @param decision {@code true} if the player accepts the reward, {@code false} otherwise
     * @return the updated {@link Player} object
     * @throws IllegalArgumentException if the username does not match the expected player
     */
    @Override
    public Player rewardDecision(String username, boolean decision){
        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(decision){
            gameModel.move(player.getUsername(), smugglers.getLostDays() * -1);

            this.advContext.setAdvState(new ChooseMaterialsSmugglers(advContext, player));
        }
        else{
            this.advContext.setAdvState(new IdleState(advContext));
        }

        return player;
    }
}

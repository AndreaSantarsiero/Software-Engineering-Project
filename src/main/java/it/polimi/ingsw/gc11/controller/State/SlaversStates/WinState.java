package it.polimi.ingsw.gc11.controller.State.SlaversStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Slavers;


/**
 * Represents the state reached when a player successfully wins against the {@link Slavers} card
 * during the adventure phase of the game.
 * <p>
 * This state handles the logic for granting the reward to the player and transitioning
 * the game to the next appropriate state.
 * </p>
 *
 * <p>
 * Upon instantiation, the class retrieves the drawn Slavers card and saves a reference
 * to the player and game model involved in this specific resolution.
 * </p>
 *
 */
public class  WinState extends AdventureState {

    private final Player player;
    private final GameModel gameModel;
    private final Slavers slavers;


    /**
     * Constructs a new {@code WinState} for the provided player who defeated the
     * Slavers during the adventure phase.
     *
     * @param advContext the adventure phase context in which this state occurs.
     * @param player     the player who has won the encounter.
     * @throws ClassCastException if the drawn adventure card is not of type {@link Slavers}.
     */
    public WinState(AdventurePhase advContext, Player player) {
        super(advContext);
        this.player = player;
        this.gameModel = advContext.getGameModel();
        this.slavers = (Slavers) advContext.getDrawnAdvCard();
    }


    /**
     * Processes the player's decision to accept or refuse the reward after winning against the Slavers.
     * <p>
     * If the decision is accepted ({@code decision == true}), the player gains coins specified by
     * the Slavers card and is moved forward in time (lost days are subtracted).
     * Regardless of the decision, the state transitions back to {@link IdleState}, and the
     * resolving flag is reset.
     * </p>
     *
     * @param username the username of the player making the decision.
     * @param decision {@code true} to accept the reward; {@code false} to skip it.
     * @return the updated {@link Player} object after processing the decision.
     * @throws IllegalArgumentException if the username does not match the current player.
     */
    @Override
    public Player rewardDecision(String username, boolean decision){
        gameModel.checkPlayerUsername(username);

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(decision){
            player.addCoins(slavers.getCoins());
            gameModel.move(player.getUsername(), slavers.getLostDays() * -1);
        }

        //Resetto resolving card
        this.advContext.setResolvingAdvCard(false);
        //La carta è finita torno in IDLE
        this.advContext.setAdvState(new IdleState(advContext));

        return player;
    }

}



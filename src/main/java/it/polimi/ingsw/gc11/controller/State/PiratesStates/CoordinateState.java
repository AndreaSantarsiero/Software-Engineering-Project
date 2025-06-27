package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the state in which players defeated by pirates must roll to determine
 * the coordinates of the next incoming shot during a {@link Pirates} event.
 *
 * <p>This state manages the coordination phase of the pirates attack: it checks if there are remaining hits,
 * prompts the appropriate player to roll dice to determine the target coordinate,
 * and transitions to {@link HandleHit} for shot resolution. If all shots have been handled,
 * it transitions to the {@link IdleState}.</p>
 *
 */
public class CoordinateState extends AdventureState {

    private final GameModel gameModel;
    private final List<Player> playersDefeated;
    private final int iterationsHit;
    private final Pirates pirates;


    /**
     * Constructs a {@code CoordinateState} for resolving coordinate selection
     * during a pirates attack.
     *
     * @param advContext The current AdventurePhase context.
     * @param playersDefeated The list of players that must take hits.
     * @param iterationsHit The number of hits already resolved.
     */
    public CoordinateState(AdventurePhase advContext, List<Player> playersDefeated, int iterationsHit) {
        super(advContext);
        this.playersDefeated = playersDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;
        pirates = (Pirates) this.advContext.getDrawnAdvCard();
    }

    /**
     * Initializes the coordinate selection phase of the pirates attack.
     *
     * <p>If there are no more hits to resolve, all players are notified,
     * and the game transitions to {@link IdleState}. Otherwise, the first
     * defeated player is prompted to roll for the hit coordinate.</p>
     */
    @Override
    public void initialize() {
        //No Hit left to handle
        if(iterationsHit == pirates.getShots().size()){
            this.advContext.setAdvState(new IdleState(advContext));
            //Notify all the player that there are no more hits to handle
            for(Player p : advContext.getGameModel().getPlayersNotAbort()) {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyNewHit(false) //false because there are no more hits to handle
                );
            }
        }
        else {
            //there are still Hit to handle
            //Notify the first player defeated that it's his turn to roll dices
            advContext.getGameContext().sendAction(
                    playersDefeated.getFirst().getUsername(),
                    new NotifyNewHit(true)
            );
        }
    }


    /**
     * Allows the next player to roll dice to determine the hit coordinate.
     *
     * <p>The coordinate is computed from two dice values and stored in the corresponding {@link Hit} object.
     * The state then transitions to {@link HandleHit}, which will apply damage to affected players.</p>
     *
     * @param username The username of the player rolling the dice.
     * @return The {@link Hit} object with the updated coordinate.
     * @throws IllegalArgumentException If a player tries to act out of turn.
     */
    @Override
    public Hit getCoordinate(String username){
        if(!playersDefeated.getFirst().getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to roll dices");
        }

        int coordinates = gameModel.getValDice1() + gameModel.getValDice2();
        //La coordinata calcolata va poi inviata a tutti i client

        //NextState
        List<Boolean> alreadyPlayed = new ArrayList<>();
        for(int i = 0; i < playersDefeated.size(); i++){
            alreadyPlayed.add(false);
        }

        this.advContext.setAdvState(new HandleHit(advContext, playersDefeated, coordinates, iterationsHit,
                                    0, alreadyPlayed));

        Hit hit = pirates.getShots().get(iterationsHit);
        hit.setCoordinate(coordinates);

        return hit;
    }
}

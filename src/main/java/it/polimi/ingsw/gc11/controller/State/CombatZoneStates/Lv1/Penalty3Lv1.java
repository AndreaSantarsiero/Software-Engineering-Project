package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
import it.polimi.ingsw.gc11.action.client.NotifyWinLose;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;


/**
 * Represents the state in the Adventure Phase at Combat Zone Level 1,
 * where a defeated player must handle a sequence of hits, one at a time.
 *
 * <p>This state determines whether there are remaining hits to resolve. If so,
 * the player is prompted to generate coordinates for the incoming hit. If not,
 * the state transitions back to IdleState.</p>
 *
 */
public class Penalty3Lv1 extends AdventureState {

    private final GameModel gameModel;
    private final Player playerDefeated;
    private final int iterationsHit;
    private final CombatZoneLv1 combatZoneLv1;

    /**
     * Constructs a Penalty3Lv1 state for handling the defeated player's hits.
     *
     * @param advContext The current AdventurePhase context.
     * @param playerDefeated The player who is resolving the penalty hits.
     * @param iterationsHit The number of hits that have already been resolved.
     */
    public Penalty3Lv1(AdventurePhase advContext, Player playerDefeated, int iterationsHit) {
        super(advContext);
        this.playerDefeated = playerDefeated;
        this.gameModel = advContext.getGameModel();
        this.iterationsHit = iterationsHit;

        this.combatZoneLv1 = (CombatZoneLv1) this.advContext.getDrawnAdvCard();

    }

    /**
     * Initializes the penalty handling process. If there are remaining hits to resolve,
     * notifies the defeated player to provide coordinates. If no hits remain,
     * transitions the game to the IdleState.
     */
    @Override
    public void initialize() {
        //No Hit left to handle
        if(iterationsHit == combatZoneLv1.getShots().size()){
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
            //Notify the player with less fire powers that he has to get coordinates
            String lostPlayerUsername = playerDefeated.getUsername();
            for (Player p : advContext.getGameModel().getPlayersNotAbort()) {
                if (p.getUsername().equals(lostPlayerUsername)) {
                    advContext.getGameContext().sendAction(
                            lostPlayerUsername,
                            new NotifyWinLose(NotifyWinLose.Response.LOSE) //false because the player lost and now has to get coordinates
                    );
                    advContext.getGameContext().sendAction(
                            lostPlayerUsername,
                            new NotifyNewHit(true) //true because there is a new hit to handle
                    );
                } else {
                    advContext.getGameContext().sendAction(
                            p.getUsername(),
                            new NotifyWinLose(NotifyWinLose.Response.LOSE)
                    );
                }
            }
        }
    }

    /**
     * Computes the coordinate where the next hit will strike the player's ship.
     *
     * <p>Retrieves the value from two dice rolls and sets the computed coordinate on the current hit.
     * Transitions the state to {@link HandleShotLv1} for actual damage resolution.</p>
     *
     * @param username The username of the player responding to the hit.
     * @return The hit object with its coordinate set.
     * @throws IllegalArgumentException If the request is made by a player who is not expected to act.
     */
    @Override
    public Hit getCoordinate(String username){

        if(!playerDefeated.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to roll dices");
        }

        int coordinate = gameModel.getValDice1() + gameModel.getValDice2();
        //La coordinata calcolata va poi inviata a tutti i client

        //NextState
        this.advContext.setAdvState(new HandleShotLv1(advContext, playerDefeated, coordinate, iterationsHit));

        Hit hit = combatZoneLv1.getShots().get(iterationsHit);
        hit.setCoordinate(coordinate);

        return hit;
    }
}

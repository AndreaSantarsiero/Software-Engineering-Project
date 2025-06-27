package it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;


/**
 * Represents the state in which a meteor from a {@link MeteorSwarm} adventure card is about to impact.
 *
 * <p>In this state, the game determines whether there are more meteors to resolve. If so,
 * it prompts the appropriate player to roll dice and generate a coordinate for the next incoming meteor.
 * If all meteors have already been handled, the state transitions to {@link IdleState}.</p>
 *
 * <p>The state is responsible for informing players of whether a new hit is incoming or if the meteor swarm is over.</p>
 *
 */
public class MeteorSwarmState extends AdventureState {

    private final GameModel gameModel;
    private final int iterationsHit;
    private final MeteorSwarm meteorSwarm;


    /**
     * Constructs a {@code MeteorSwarmState} with the given context and the current meteor index.
     *
     * @param advContext The current AdventurePhase context.
     * @param iterationsHit The number of meteors already handled.
     */
    public MeteorSwarmState(AdventurePhase advContext, int iterationsHit) {
        super(advContext);
        this.gameModel = this.advContext.getGameModel();
        this.iterationsHit = iterationsHit;
        this.meteorSwarm = (MeteorSwarm) advContext.getDrawnAdvCard();
    }

    /**
     * Initializes the meteor swarm sequence.
     *
     * <p>If all meteors have been resolved, notifies all players and transitions to {@link IdleState}.
     * Otherwise, notifies the first player to roll dice and begin the next meteor resolution.</p>
     */
    @Override
    public void initialize() {
        //No Hit left to handle
        if(iterationsHit == meteorSwarm.getMeteors().size()){
            this.advContext.setAdvState(new IdleState(advContext));
            //Notify ALL the player that there are no more hits to handle
            for(Player p : advContext.getGameModel().getPlayersNotAbort()) {
                advContext.getGameContext().sendAction(
                        p.getUsername(),
                        new NotifyNewHit(false) //false because there are no more hits to handle
                );
            }
        }
        else {
            //there are still Hit to handle
            //Notify the first player that it's his turn to roll dices
            advContext.getGameContext().sendAction(
                    gameModel.getPlayersNotAbort().getFirst().getUsername(),
                    new NotifyNewHit(true)
            );
        }
    }


    /**
     * Generates the coordinate for the next meteor hit based on dice rolls.
     *
     * <p>Once the coordinate is determined, the state transitions to {@link HandleMeteor}
     * to handle the meteor resolution.</p>
     *
     * @param username The username of the player rolling the dice.
     * @return The {@link Hit} object with the computed coordinate.
     * @throws IllegalArgumentException If it is not the calling player's turn.
     */
    @Override
    public Hit getCoordinate(String username){
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        int coordinate = gameModel.getValDice1() + gameModel.getValDice2();

        //NextState
        MeteorSwarm meteorSwarm = (MeteorSwarm) this.advContext.getDrawnAdvCard();
        if(iterationsHit == meteorSwarm.getMeteors().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
        else{
            this.advContext.setAdvState(new HandleMeteor(advContext, coordinate, iterationsHit, 0));
        }

        Hit hit = meteorSwarm.getMeteors().get(iterationsHit);
        hit.setCoordinate(coordinate);

        return hit;
    }

}
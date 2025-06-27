package it.polimi.ingsw.gc11.controller.State.MeteorSwarmStates;

import it.polimi.ingsw.gc11.action.client.NotifyNewHit;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import java.util.Map;


/**
 * Represents the state in which a player must defend against an incoming meteor
 * during a {@link MeteorSwarm} event in the game.
 *
 * <p>This state handles meteor defense for each player in order, checking shield protection,
 * cannon defense, and battery usage. Depending on the type and direction of the meteor,
 * ship components may be destroyed if not properly defended.</p>
 *
 * <p>Once all players have responded to a meteor, or all meteors have been resolved,
 * the game transitions to the appropriate next state: {@link MeteorSwarmState} for the next meteor,
 * or {@link IdleState} if the event is fully resolved.</p>
 */
public class HandleMeteor extends AdventureState {

    private final GameModel gameModel;
    private final int coordinates;
    private int iterationsHit;
    private int iterationsPlayer;
    private final MeteorSwarm meteorSwarm;


    /**
     * Constructs a {@code HandleMeteor} state for resolving a specific meteor impact
     * on the current player.
     *
     * @param advContext The current AdventurePhase context.
     * @param coordinates The ship coordinate being targeted by the meteor.
     * @param iterationsHit The number of meteors already handled.
     * @param iterationsPlayer The index of the current player being processed.
     */
    public HandleMeteor(AdventurePhase advContext, int coordinates, int iterationsHit, int iterationsPlayer) {
       super(advContext);
       this.gameModel = advContext.getGameModel();
       this.coordinates = coordinates;
       this.iterationsHit = iterationsHit;
       this.iterationsPlayer = iterationsPlayer;
       this.meteorSwarm = (MeteorSwarm) advContext.getDrawnAdvCard();
    }

    /**
     * Resolves a player's defense against a meteor, considering shields, batteries, and cannons.
     *
     * <p>For small meteors, shields and battery-powered defenses are considered. If no valid defense is found,
     * the affected ship component is destroyed. For large meteors, cannon defenses are checked, with batteries
     * required if a double cannon is used.</p>
     *
     * <p>After processing, the method transitions to the next player, the next meteor, or ends the event.</p>
     *
     * @param username The username of the player currently defending.
     * @param batteries A map of batteries and the amount of charge to use.
     * @param cannon The cannon chosen by the player to defend against a large meteor (can be {@code null}).
     * @return The player after processing the meteor defense.
     *
     * @throws IllegalArgumentException If the username is incorrect or if batteries are {@code null}.
     * @throws IndexOutOfBoundsException If an invalid meteor index is encountered.
     */
    @Override
    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon) {
        gameModel.checkPlayerUsername(username);
        Player player = gameModel.getPlayersNotAbort().get(advContext.getIdxCurrentPlayer());

        if(!player.getUsername().equals(username)){
            throw new IllegalArgumentException("It's not your turn to play");
        }

        if(batteries == null){
            throw new IllegalArgumentException("batteries is null");
        }

        if(iterationsHit >= meteorSwarm.getMeteors().size()){
            throw new IndexOutOfBoundsException();
        }

        Meteor meteor = meteorSwarm.getMeteors().get(iterationsHit);

        if(meteor.getType() == Hit.Type.SMALL){
            //Player doesn't have a shield and has an exposed connector, ShipCard destroyed
            if(!player.getShipBoard().isBeingProtected(meteor.getDirection()) && player.getShipBoard().hasAnExposedConnector(meteor.getDirection(), coordinates)){
                player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
            } else if (player.getShipBoard().hasAnExposedConnector(meteor.getDirection(), coordinates) && player.getShipBoard().isBeingProtected(meteor.getDirection())){
                //Player protetto ma con connettore esposto, verifico abbia attivato lo scudo con delle batterie
                if(batteries.isEmpty()){
                    player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
                }
                else{
                    //Controllo che abbia usato almeno una batteria, nel caso l'abbia usata elimino batterie usate e avanzo. Altrimenti distruggo il pezzo
                    boolean usedBatteries = false;
                    for(Battery battery : batteries.keySet()){
                        if(batteries.get(battery) > 0){
                            usedBatteries = true;
                        }
                    }

                    if(!usedBatteries){
                        player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
                    }
                    else{
                        player.getShipBoard().useBatteries(batteries);
                    }
                }

            }

        }
        else{
            if(cannon != null){
                if(player.getShipBoard().canDestroy(meteor.getDirection(), coordinates).contains(cannon)){
                    //Player select a cannon to protect him against the big meteor
                    if(cannon.getType() == Cannon.Type.DOUBLE){
                        //Use batteries
                        if(!batteries.isEmpty()){
                            player.getShipBoard().useBatteries(batteries);
                        }
                    }
                }
                else{
                    player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
                }

            }
            else{
                player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
            }

        }

        this.iterationsPlayer++;

        //next state
        if(iterationsPlayer == gameModel.getPlayersNotAbort().size() && iterationsHit < meteorSwarm.getMeteors().size()){
            //No more player left to handle
            this.iterationsHit++;
            this.advContext.setResolvingAdvCard(false);
            this.advContext.setAdvState(new MeteorSwarmState(advContext, iterationsHit));
        }
        else if(iterationsHit == meteorSwarm.getMeteors().size() && iterationsPlayer == gameModel.getPlayersNotAbort().size()){
            this.advContext.setAdvState(new IdleState(advContext));
        }
        else{
            //Passo al prossimo player
            this.advContext.setIdxCurrentPlayer(advContext.getIdxCurrentPlayer() + 1);

            this.advContext.setAdvState(new HandleMeteor(advContext, coordinates, iterationsHit, iterationsPlayer));
        }

        return player;
    }
}

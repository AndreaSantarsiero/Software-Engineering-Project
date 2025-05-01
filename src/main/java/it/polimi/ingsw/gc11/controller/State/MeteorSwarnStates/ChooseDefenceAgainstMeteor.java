package it.polimi.ingsw.gc11.controller.State.MeteorSwarnStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Meteor;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.MeteorSwarm;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.Map;

/**
 * Represents the state where a specific player must defend against a sequence of meteor impacts
 * during the adventure phase of the game.
 *
 * <p>Each meteor may either be a small or big meteor, requiring different defensive strategies.
 * Players must use ship shields, batteries, and cannons accordingly to prevent damage to their ship.</p>
 *
 * <p>Each invocation of {@link #meteorHit(int, Map, Cannon)} handles the resolution of one meteor impact
 * for the given player.</p>
 *
 * <p>If the defense fails or is insufficient, the corresponding component of the player's ship will be destroyed.</p>
 *
 * <p>This class focuses on resolving meteors for a single player, unlike multiplayer variants where the
 * defense is resolved simultaneously for all players.</p>
 *
 * @see MeteorSwarm
 * @see AdventureState
 * @see AdventurePhase
 */
public class ChooseDefenceAgainstMeteor extends AdventureState {
    Player player;
    GameModel gameModel;
    MeteorSwarm meteorSwarm;
    int iterations;

    /**
     * Constructs a new {@code ChooseDefenceAgainstMeteor} state for a specific player.
     *
     * @param player The player who must defend against the meteor impacts.
     * @param gameModel The current game model.
     * @param meteorSwarm The meteor swarm adventure card containing the sequence of incoming meteors.
     * @throws NullPointerException if any of {@code player}, {@code gameModel}, or {@code meteorSwarm} are {@code null}.
     */
    public ChooseDefenceAgainstMeteor(Player player, GameModel gameModel, MeteorSwarm meteorSwarm) {
        if(gameModel == null || meteorSwarm == null || player == null){
            throw new NullPointerException();
        }

        this.player = player;
        this.gameModel = gameModel;
        this.meteorSwarm = meteorSwarm;
        this.iterations = 0;
    }

    /**
     * Processes the impact of a single meteor on the player's ship, allowing them to attempt defense
     * using provided batteries and a selected cannon.
     *
     * <p>The defensive resolution depends on the meteor type:
     * <ul>
     *     <li><b>Small Meteor:</b>
     *         <ul>
     *             <li>If the ship has an exposed connector and no active shield, the impacted component is destroyed.</li>
     *             <li>If a shield is present, the player must have used batteries to activate it; otherwise destruction occurs.</li>
     *         </ul>
     *     </li>
     *     <li><b>Big Meteor:</b>
     *         <ul>
     *             <li>If the player uses an appropriate cannon, defense succeeds. If the cannon is a double cannon, battery usage is required.</li>
     *             <li>If no valid cannon is provided, the ship component is destroyed.</li>
     *         </ul>
     *     </li>
     * </ul>
     * </p>
     *
     * @param coordinates The target coordinates on the player's ship affected by the meteor.
     * @param batteries A map from {@link Battery} objects to the number of batteries used for shield or double-cannon activation. Can be {@code null} if no batteries are used.
     * @param cannon The cannon selected to defend against a big meteor. Can be {@code null} if no cannon is used.
     * @throws IndexOutOfBoundsException if this method is called more times than the number of meteors in the {@link MeteorSwarm}.
     * @throws NullPointerException if a double cannon is selected against a big meteor without providing any batteries.
     */
    public void meteorHit(int coordinates, Map<Battery, Integer> batteries, Cannon cannon) {
        if(iterations >= meteorSwarm.getMeteors().size()){
            throw new IndexOutOfBoundsException();
        }

        Meteor meteor = meteorSwarm.getMeteors().get(iterations);

        if(meteor.getType() == Hit.Type.SMALL){
            //Player doesn't have a shield and has an exposed connector, ShipCard destroyed
            if(!player.getShipBoard().isBeingProtected(meteor.getDirection()) && player.getShipBoard().hasAnExposedConnector(meteor.getDirection(), coordinates)){
                player.getShipBoard().destroyHitComponent(meteor.getDirection(), coordinates);
            } else if (player.getShipBoard().hasAnExposedConnector(meteor.getDirection(), coordinates) && player.getShipBoard().isBeingProtected(meteor.getDirection())){
                //Player protetto ma con connettore esposto, verifico abbia attivato lo scudo con delle batterie
                if(batteries == null){
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
                        if(batteries == null){
                            throw new NullPointerException();
                        }
                        player.getShipBoard().useBatteries(batteries);
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

        this.iterations++;
    }


    /**
     * Advances to the next adventure state.
     *
     * <p>No specific transition logic is implemented in this class.
     * It is expected that external components (such as the adventure phase controller)
     * handle the progression once all meteor impacts have been processed.</p>
     *
     * @param advContext The current adventure phase context.
     */
    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }
}

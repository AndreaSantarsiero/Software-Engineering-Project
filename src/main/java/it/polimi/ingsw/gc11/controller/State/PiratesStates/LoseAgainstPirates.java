package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.List;
import java.util.Map;

/**
 * Represents the state where players, having lost against the Pirates,
 * must withstand a series of shots targeting their ship.
 *
 * <p>Each shot described in the {@link Pirates} card is processed sequentially for every defeated player.
 * Players can attempt to protect their ship components using batteries; otherwise, components are destroyed.</p>
 *
 * <p>Key behaviors:
 * <ul>
 *     <li>Processes each shot in order as described in the Pirates' card.</li>
 *     <li>Checks if players have protection active via batteries; if not, ship components are destroyed.</li>
 *     <li>Ensures correct consumption of batteries if used to block a shot.</li>
 *     <li>Iterates shots across multiple players that were defeated.</li>
 * </ul>
 * </p>
 *
 * <p>This state does not allow normal progression through {@code nextAdvState}, which is intentionally unsupported.</p>
 *
 * @see Pirates
 * @see AdventurePhase
 * @see AdventureState
 */
public class LoseAgainstPirates implements AdventureState {
    List<Player> playerDefeated;
    GameModel gameModel;
    Pirates pirates;
    int iterations;
    int coordinates;

    /**
     * Constructs the LoseAgainstPirates state.
     *
     * @param player The list of players who have lost against the Pirates.
     * @param gameModel The current game model instance.
     * @param pirates The Pirates adventure card causing the damage.
     * @throws NullPointerException if any parameter is null.
     */
    public LoseAgainstPirates(List<Player> player, GameModel gameModel, Pirates pirates) {
        if(player == null || gameModel == null || pirates == null){
            throw new NullPointerException();
        }
        this.playerDefeated = player;
        this.gameModel = gameModel;
        this.pirates = pirates;
        this.iterations = 0;
        this.coordinates = 0;
    }

    /**
     * Processes a single shot from the Pirates towards all defeated players' ships.
     *
     * <p>For each shot:
     * <ul>
     *     <li>If the shot is of type {@code SMALL} and the targeted component is not protected, the component is destroyed.</li>
     *     <li>If the shot is protected by batteries, batteries are consumed if available; otherwise, the component is destroyed.</li>
     *     <li>If the shot is not of type {@code SMALL}, the component is destroyed regardless of protection.</li>
     * </ul>
     * </p>
     *
     * <p>It is assumed that this method is called once per shot, in sequence, by the controller logic.</p>
     *
     * @param coordinates The coordinate on the ship board that the shot targets.
     * @param batteries The map of batteries and their remaining available uses; can be null.
     * @throws IndexOutOfBoundsException if all shots have already been processed.
     */
    public void hitPirate(int coordinates, Map<Battery, Integer> batteries) {
        if(iterations >= pirates.getShots().size()){
            throw new IndexOutOfBoundsException();
        }

        this.coordinates = coordinates;
        Shot shot = pirates.getShots().get(iterations);

        for(Player player : playerDefeated){
            if(shot.getType() == Hit.Type.SMALL){
                //Direction it's not protected
                if(!player.getShipBoard().isBeingProtected(shot.getDirection())){
                    player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
                }
                else{
                    if(batteries == null){
                        player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
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
                            player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
                        }
                        else{
                            player.getShipBoard().useBatteries(batteries);
                        }
                    }
                }
            }
            else{
                player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
            }
        }
        this.iterations++;
    }

    /**
     * This method is unsupported in the LoseAgainstPirates state.
     *
     * @param advContext The current AdventurePhase context.
     * @throws UnsupportedOperationException Always thrown to indicate invalid operation.
     */
    @Override
    public void nextAdvState(AdventurePhase advContext) {
        throw new UnsupportedOperationException("Not supported");
    }
}

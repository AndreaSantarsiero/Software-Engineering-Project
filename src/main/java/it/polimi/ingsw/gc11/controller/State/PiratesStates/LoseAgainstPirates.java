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
 * Represents the state when a player has lost against pirates during an adventure phase.
 *
 * <p>This state handles the consequences for defeated players. Players who lose must defend their ship
 * from pirate shots using shields and batteries. If they fail to properly defend, parts of their ship are destroyed.</p>
 * <p>The state will process each pirate shot until all the defeated players have been handled.</p>
 *
 * @see AdventureState
 * @see AdventurePhase
 * @see Pirates
 * @see WinAgainstPirates
 */
public class LoseAgainstPirates extends AdventureState {
    List<Player> playerDefeated;
    GameModel gameModel;
    Pirates pirates;
    int iterations;
    int coordinates;

    /**
     * Constructs the LoseAgainstPirates state for the given defeated players and pirates encounter.
     *
     * @param player The list of players defeated by pirates.
     * @param gameModel The game model containing the state of the game.
     * @param pirates The Pirates adventure card the players are facing.
     * @throws NullPointerException if any of the parameters are null.
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
     * Handles the impact of a pirate shot on a defeated player.
     *
     * <p>This method checks if the shot type is small or large and applies the appropriate defense mechanism
     * (using shields and batteries if available) to prevent ship damage. If the defense fails, the affected component is destroyed.</p>
     *
     * @param coordinates The coordinates of the affected ship component.
     * @param batteries The batteries used by the player for defense.
     * @param player The player who is being attacked by pirates.
     * @throws IllegalArgumentException if the player is not in the list of defeated players.
     * @throws IndexOutOfBoundsException if there are no more pirate shots to process.
     */
    public void hitPirate(int coordinates, Map<Battery, Integer> batteries, Player player) {
        if(!playerDefeated.contains(player)){
            throw new IllegalArgumentException();
        }
        if(iterations >= pirates.getShots().size()){
            throw new IndexOutOfBoundsException();
        }

        this.coordinates = coordinates;
        Shot shot = pirates.getShots().get(iterations);

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

package it.polimi.ingsw.gc11.controller.State.PiratesStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.Pirates;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.List;
import java.util.Map;


/**
 * Represents the state in the Pirates event where each defeated player must
 * handle an incoming {@link Shot} at a specific coordinate.
 *
 * <p>This state manages the defensive response of each player who was previously defeated
 * during a pirates attack. Each player in the {@code playersDefeated} list gets one chance to
 * defend against the current hit. The shot is either blocked using batteries if the ship is protected,
 * or the corresponding ship component is destroyed.</p>
 *
 * <p>Once all players have responded to the current shot, the state transitions back to
 * {@link CoordinateState} to determine the coordinates of the next shot, if any.</p>
 *
 */
public class HandleHit extends AdventureState {

    private final List<Player> playersDefeated;
    private final List<Boolean> alreadyPlayed;
    private final Pirates pirates;
    private final int coordinates;
    private int iterationsHit;
    private int iterationsPlayers;


    /**
     * Constructs a new HandleHit state for resolving a pirates hit at a fixed coordinate.
     *
     * @param advContext        The adventure context containing game information.
     * @param playersDefeated   The list of defeated players who must respond to the hit.
     * @param coordinates       The coordinate determined by a dice roll where the shot will strike.
     * @param iterationsHit     The number of pirate shots already resolved.
     * @param iterationsPlayers The number of players who have responded to the current shot.
     * @param alreadyPlayed     A parallel list indicating whether each defeated player has already acted.
     */
    public HandleHit(AdventurePhase advContext, List<Player> playersDefeated, int coordinates, int iterationsHit, int iterationsPlayers, List<Boolean> alreadyPlayed) {
        super(advContext);
        this.playersDefeated = playersDefeated;
        this.alreadyPlayed = alreadyPlayed;
        this.pirates = (Pirates) this.advContext.getDrawnAdvCard();
        this.coordinates = coordinates;
        this.iterationsHit = iterationsHit;
        this.iterationsPlayers = iterationsPlayers;
    }



    /**
     * Handles the player's response to a pirate shot at the specified coordinate.
     *
     * <p>Depending on whether the ship is protected and whether batteries are used,
     * the component may be destroyed or preserved. Once all players have responded,
     * moves to the next shot or loops again for remaining players.</p>
     *
     * @param username  The player attempting to respond.
     * @param batteries The map of batteries used to activate protection (may be empty).
     * @return The {@link Player} object after applying the shot effect.
     * @throws IllegalArgumentException If the player is not eligible or already played, or if input is invalid.
     */
    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries) {
        Player player = this.advContext.getGameModel().getPlayer(username);

        //Checko che il player sia stato effettivamente sconfitto
        if(!playersDefeated.contains(player)){
            throw new IllegalArgumentException("You are not in the List of defeated players");
        }

        //Controllo non si sia già difeso
        int index = playersDefeated.indexOf(player);

        if(batteries == null){
            throw new IllegalArgumentException("Batteries cannot be null");
        }

        if(alreadyPlayed.get(index)){
            throw new IllegalArgumentException("You have already defended this shot");
        }

        Shot shot = pirates.getShots().get(iterationsHit);

        if(shot.getType() == Hit.Type.SMALL){
            //Direction it's not protected
            if(!player.getShipBoard().isBeingProtected(shot.getDirection())){
                player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinates);
                player.getShipBoard().useBatteries(batteries);
            }
            else{
                if(batteries.isEmpty()){
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

        //Setto che il giocatore si è già difeso da questo shot
        alreadyPlayed.set(index, true);
        this.iterationsPlayers++;

        if(this.iterationsPlayers == this.playersDefeated.size()){
            this.iterationsHit++;
            //nextstate
            this.advContext.setAdvState(new CoordinateState(advContext, playersDefeated, iterationsHit));
        }
        else{
            this.advContext.setAdvState(new HandleHit(advContext, playersDefeated, coordinates, iterationsHit, iterationsPlayers, alreadyPlayed));
        }

        return player;
    }

}

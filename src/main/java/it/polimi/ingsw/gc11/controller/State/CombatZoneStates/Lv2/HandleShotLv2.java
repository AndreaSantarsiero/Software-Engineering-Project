package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv2;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv2;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Map;

/**
 * Represents the state at Combat Zone Level 2 where a single shot is applied to a player's ship.
 *
 * <p>This state handles the logic for processing a specific {@link Shot}, determining whether it is
 * blocked or mitigated using batteries, and applying damage to the ship's components.</p>
 *
 * <p>Once the current shot is processed, the state transitions to the next {@link Penalty3Lv2}
 * state with updated iteration.</p>
 *
 */
public class HandleShotLv2 extends AdventureState {
    private Player player;
    private int coordinate;
    private int iterationsHit;
    private CombatZoneLv2 combatZoneLv2;

    /**
     * Constructs a new {@code HandleShotLv2} state for processing a shot against a player's ship.
     *
     * @param advContext The current adventure phase context.
     * @param player The player whose ship is being hit.
     * @param coordinate The coordinate where the shot is targeted.
     * @param iterationsHit The current index of the shot being handled.
     */
    public HandleShotLv2(AdventurePhase advContext, Player player, int coordinate, int iterationsHit) {
        super(advContext);
        this.player = player;
        this.coordinate = coordinate;
        this.iterationsHit = iterationsHit;
        this.combatZoneLv2 = (CombatZoneLv2) advContext.getDrawnAdvCard();
    }


    /**
     * Applies the current shot to the specified player's ship.
     *
     * <p>The method checks the type of shot (small or large), whether the shot direction is protected,
     * and if the player is attempting to use batteries. Depending on the player's choice and protection
     * status, either the ship component is destroyed or batteries are consumed.</p>
     *
     * <p>After handling the shot, the method transitions to {@link Penalty3Lv2} to continue processing
     * remaining shots (if any).</p>
     *
     * @param username The username of the player expected to take the hit.
     * @param batteries A map of batteries and how many charges the player intends to use.
     * @return The player object after applying damage or battery usage.
     * @throws IllegalArgumentException if the username does not match or if batteries are null.
     */
    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries) {

        Player player = this.advContext.getGameModel().getPlayer(username);

        Shot shot = combatZoneLv2.getShots().get(iterationsHit);

        if(batteries == null){
            throw new IllegalArgumentException("batteries null");
        }

        if(shot.getType() == Hit.Type.SMALL){
            //Direction it's not protected
            if(!player.getShipBoard().isBeingProtected(shot.getDirection())){
                player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinate);
            }
            else{
                if(batteries.isEmpty()){
                    player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinate);
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
                        player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinate);
                    }
                    else{
                        player.getShipBoard().useBatteries(batteries);
                    }
                }
            }
        }
        else{
            player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinate);
        }

        //+Gestione Rottura Nave

        this.iterationsHit++;

        //nextstate
        this.advContext.setAdvState(new Penalty3Lv2(advContext, player, iterationsHit));

        return player;
    }

}

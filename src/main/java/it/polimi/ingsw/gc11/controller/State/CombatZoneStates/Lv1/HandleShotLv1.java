package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;


import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Map;

/**
 * Represents the state in the Adventure Phase at Combat Zone Level 1,
 * handling the logic when a player's ship is hit by a shot.
 *
 * <p>This state processes a single shot, determining if it is blocked by protection or batteries,
 * updating the ship's components accordingly, and transitions to the next penalty state.</p>
 */
public class HandleShotLv1 extends AdventureState {
    private Player player;
    private int coordinate;
    private int iterationsHit;
    private CombatZoneLv1 combatZoneLv1;

    /**
     * Constructs a HandleShotLv1 state with the given context, player, coordinate, and hit iteration.
     *
     * @param advContext The current AdventurePhase context.
     * @param player The player whose ship is being hit.
     * @param coordinate The ship coordinate targeted by the shot.
     * @param iterationsHit The current number of handled shots.
     */
    public HandleShotLv1(AdventurePhase advContext, Player player, int coordinate, int iterationsHit) {
        super(advContext);
        this.player = player;
        this.coordinate = coordinate;
        this.iterationsHit = iterationsHit;
        this.combatZoneLv1 = (CombatZoneLv1) advContext.getDrawnAdvCard();
    }


    /**
     * Processes a shot fired at the player's ship, determining protection status, battery usage,
     * component destruction, and state transition to penalty handling.
     *
     * <p>Throws an exception if battery usage data is missing or invalid. Differentiates between small and large hits,
     * managing protection mechanisms and battery usage accordingly.</p>
     *
     * @param username The username of the player whose ship is being hit.
     * @param batteries Map indicating the batteries the player intends to use.
     * @return The player object after handling the shot and updating their ship state.
     * @throws IllegalArgumentException If the batteries parameter is null.
     */
    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries) {

        Player player = this.advContext.getGameModel().getPlayer(username);

        Shot shot = combatZoneLv1.getShots().get(iterationsHit);

        if(batteries == null){
            throw new IllegalArgumentException("batteries is null");
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
        this.advContext.setAdvState(new Penalty3Lv1(advContext, player, iterationsHit));
        this.advContext.getCurrentAdvState().initialize();

        return player;
    }

}

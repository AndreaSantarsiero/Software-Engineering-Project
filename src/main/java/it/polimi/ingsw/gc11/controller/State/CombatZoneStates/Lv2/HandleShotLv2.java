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


public class HandleShotLv2 extends AdventureState {
    private Player player;
    private int coordinate;
    private int iterationsHit;
    private CombatZoneLv2 combatZoneLv2;

    public HandleShotLv2(AdventurePhase advContext, Player player, int coordinate, int iterationsHit) {
        super(advContext);
        this.player = player;
        this.coordinate = coordinate;
        this.iterationsHit = iterationsHit;
    }


    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries) {

        Player player = this.advContext.getGameModel().getPlayer(username);

        Shot shot = combatZoneLv2.getShots().get(iterationsHit);

        if(shot.getType() == Hit.Type.SMALL){
            //Direction it's not protected
            if(!player.getShipBoard().isBeingProtected(shot.getDirection())){
                player.getShipBoard().destroyHitComponent(shot.getDirection(), coordinate);
            }
            else{
                if(batteries == null){
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

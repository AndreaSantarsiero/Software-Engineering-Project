package it.polimi.ingsw.gc11.controller.State.CombatZoneStates.Lv1;

import it.polimi.ingsw.gc11.action.client.UpdateCurrentPlayerAction;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.IdleState;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZoneLv1;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Map;


public class HandleShotLv1 extends AdventureState {
    private Player player;
    private int coordinate;
    private int iterationsHit;
    private CombatZoneLv1 combatZoneLv1;

    public HandleShotLv1(AdventurePhase advContext, Player player, int coordinate, int iterationsHit) {
        super(advContext);
        this.player = player;
        this.coordinate = coordinate;
        this.iterationsHit = iterationsHit;
        this.combatZoneLv1 = (CombatZoneLv1) advContext.getDrawnAdvCard();

        //Notify the player with less fire powers that he has get to handle the shot
        String newCurrentPlayer = player.getUsername();
        for(Player p : advContext.getGameModel().getPlayersNotAbort()){
            if(p.getUsername().equals(newCurrentPlayer)){
                UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, true);
                advContext.getGameContext().sendAction(player.getUsername(), response);
            }
            else {
                UpdateCurrentPlayerAction response = new UpdateCurrentPlayerAction(newCurrentPlayer, false);
                advContext.getGameContext().sendAction(player.getUsername(), response);
            }
        }
    }


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

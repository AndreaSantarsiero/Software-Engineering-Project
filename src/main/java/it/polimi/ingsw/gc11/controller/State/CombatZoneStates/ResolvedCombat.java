package it.polimi.ingsw.gc11.controller.State.CombatZoneStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.model.adventurecard.CombatPhase;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZone;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

public class ResolvedCombat extends CombatZoneState{
    private CombatZone combatZone;
    private int numPhase;
    private GameModel gameModel;
    private Player player;

    public ResolvedCombat(CombatZone combatZone, GameModel gameModel, int numPhase, Player player) {
        if(gameModel == null || combatZone == null) {
            throw new NullPointerException();
        }
        if(numPhase < 0 || numPhase >= 3) {
            throw new IllegalArgumentException();
        }

        this.gameModel = gameModel;
        this.combatZone = combatZone;
        this.numPhase = numPhase;
        this.player = player;
    }

    public void resolveLostDays(){
        if(combatZone.getCombatPhase(numPhase).getPenalty() != CombatPhase.Penalty.LOST_DAYS) {
            throw new IllegalStateException();
        }

        gameModel.move(player.getUsername(), combatZone.getCombatPhase(numPhase).getAmount() * -1);

        //go to next state
    }

    public void resolveLostMaterials(){
        if(combatZone.getCombatPhase(numPhase).getPenalty() != CombatPhase.Penalty.LOST_MATERIALS) {
            throw new IllegalStateException();
        }

        player.getShipBoard().removeMaterials(combatZone.getCombatPhase(numPhase).getAmount());

        //go to next state
    }

    public void resolveLostMembers(Map<HousingUnit, Integer> housingUsage){
        int numMembers = 0;

        if(combatZone.getCombatPhase(numPhase).getPenalty() !=  CombatPhase.Penalty.LOST_MEMBERS) {
            throw new IllegalStateException();
        }

        for(HousingUnit housingUnit : housingUsage.keySet()){
            numMembers += housingUsage.get(housingUnit);
        }

        if(numMembers != combatZone.getCombatPhase(numPhase).getAmount()) {
            throw new IllegalArgumentException("number of members is incorrect");
        }

        player.getShipBoard().killMembers(housingUsage);

        // go to next state
    }

    public void resolveShot(int shotIndex, int coord, Map<Battery, Integer> batteryUsage){
        int numBatteries = 0;

        if(shotIndex < 0 || shotIndex >= combatZone.getCombatPhase(numPhase).getShots().size()) {
            throw new IllegalArgumentException();
        }

        if(batteryUsage == null){
            throw new NullPointerException();
        }

        for(Battery battery : batteryUsage.keySet()) {
            numBatteries += batteryUsage.get(battery);
        }

        if(numBatteries > player.getShipBoard().getTotalAvailableBatteries()) {
            throw new  IllegalArgumentException("number of batteries out of bounds");
        }

        if(combatZone.getCombatPhase(numPhase).getShots().get(shotIndex).getType() == Hit.Type.SMALL){
            if(numBatteries == 0 || !player.getShipBoard().isBeingProtected(combatZone.getCombatPhase(numPhase).getShots().get(shotIndex).getDirection())){
                player.getShipBoard().destroyHitComponent(combatZone.getCombatPhase(numPhase).getShots().get(shotIndex).getDirection(), coord);
            }
        } else if (combatZone.getCombatPhase(numPhase).getShots().get(shotIndex).getType() == Hit.Type.BIG) {
            player.getShipBoard().destroyHitComponent(combatZone.getCombatPhase(numPhase).getShots().get(shotIndex).getDirection(), coord);
        }

        player.getShipBoard().useBatteries(batteryUsage);
        int nextShotIndex = shotIndex + 1;

        if(shotIndex == combatZone.getCombatPhase(numPhase).getShots().size() - 1) {
            //go to next state
        }

         //go to next state
    }
}

package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.CombatZoneStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.CombatZone;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class SelectPlayerCombat extends CombatZoneState {
    private CombatZone combatZone;
    private int numPhase;
    private GameModel gameModel;

    public SelectPlayerCombat(CombatZone combatZone, GameModel gameModel, int numPhase) {
        if(gameModel == null || combatZone == null) {
            throw new NullPointerException();
        }
        if(numPhase < 0 || numPhase >= 3) {
            throw new IllegalArgumentException();
        }

        this.gameModel = gameModel;
        this.combatZone = combatZone;
        this.numPhase = numPhase;
    }

    public void chooseLessFirePowerPlayer(Map<Player, AbstractMap.SimpleEntry<List<Cannon>,Map<Battery, Integer>>> playersCannons){
        Player minPlayer = null;
        double minCannonsPower = 0;
        int numBatteries = 0;

        if(playersCannons == null || playersCannons.keySet().size() != gameModel.getNumPlayers()) {
            throw new IllegalArgumentException("playersCannons must have same number of players");
        }

        for(Player player : playersCannons.keySet()) {
            for(Cannon cannon : playersCannons.get(player).getKey()){
                if(cannon.getType() != Cannon.Type.DOUBLE){
                    throw new IllegalArgumentException("single cannon in double cannons list");
                }
            }

            numBatteries = 0;
            for(Battery battery : playersCannons.get(player).getValue().keySet()) {
                numBatteries += playersCannons.get(player).getValue().get(battery);
            }
            if(playersCannons.get(player).getKey().size() > player.getShipBoard().getDoubleCannonsNumber() || playersCannons.get(player).getKey().size() > numBatteries
                || numBatteries > player.getShipBoard().getTotalAvailableBatteries()) {
                throw new IllegalArgumentException("number of cannons or batteries out of bounds");
            }

            player.getShipBoard().useBatteries(playersCannons.get(player).getValue());
            if(minPlayer == null || player.getShipBoard().getCannonsPower(playersCannons.get(player).getKey()) < minCannonsPower ){
                minPlayer = player;
                minCannonsPower = player.getShipBoard().getCannonsPower(playersCannons.get(player).getKey());
            }
        }

        switch(combatZone.getCombatPhase(numPhase).getPenalty()){
            case LOST_DAYS:
                //go to next state
                break;
            case LOST_MATERIALS:
                //go to next state
                break;
            case LOST_MEMBERS:
                //go to next state
                break;
            case SHOTS:
                //go to next state
                break;
        }
    }

    public void chooseLessEnginePowerPlayer(Map<Player,Map<Battery, Integer>> playersEngines){
        Player minPlayer = null;
        int minEnginePower = 0;
        int numBatteries = 0;

        if(playersEngines == null || playersEngines.keySet().size() != gameModel.getNumPlayers()) {
            throw new IllegalArgumentException("playersEngines must have same number of players");
        }

        for(Player player : playersEngines.keySet()) {
            numBatteries = 0;
            for(Battery battery : playersEngines.get(player).keySet()) {
                numBatteries += playersEngines.get(player).get(battery);
            }

            if(numBatteries > player.getShipBoard().getTotalAvailableBatteries()) {
                throw new  IllegalArgumentException("number of batteries out of bounds");
            }

            player.getShipBoard().useBatteries(playersEngines.get(player));
            if(minPlayer == null || player.getShipBoard().getEnginesPower(numBatteries) < minEnginePower ){
                minPlayer = player;
                minEnginePower = numBatteries;
            }
        }

        switch(combatZone.getCombatPhase(numPhase).getPenalty()){
            case LOST_DAYS:
                //go to next state
                break;
            case LOST_MATERIALS:
                //go to next state
                break;
            case LOST_MEMBERS:
                //go to next state
                break;
            case SHOTS:
                //go to next state
                break;
        }
    }

    public void chooseLessMembersPlayer(){
        Player minPlayer = null;
        int minMembersNum = 0;

        for(Player player : gameModel.getPlayers()){
            if(minPlayer == null || player.getShipBoard().getMembers() < minMembersNum){
                minPlayer = player;
                minMembersNum = player.getShipBoard().getMembers();
            }
        }

        switch(combatZone.getCombatPhase(numPhase).getPenalty()){
            case LOST_DAYS:
                //go to next state
                break;
            case LOST_MATERIALS:
                //go to next state
                break;
            case LOST_MEMBERS:
                //go to next state
                break;
            case SHOTS:
                //go to next state
                break;
        }
    }
}

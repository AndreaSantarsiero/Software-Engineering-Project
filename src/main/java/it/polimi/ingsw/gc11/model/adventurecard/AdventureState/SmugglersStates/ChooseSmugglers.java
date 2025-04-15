package it.polimi.ingsw.gc11.model.adventurecard.AdventureState.SmugglersStates;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;

public class ChooseSmugglers extends SmugglersState {

    private Smugglers smugglers;
    private GameModel gameModel;
    private Player player;

    public  ChooseSmugglers(Smugglers smugglers, GameModel gameModel, Player player) {
        if(smugglers==null || gameModel==null || player==null){
            throw new NullPointerException();
        }

        this.smugglers = smugglers;
        this.gameModel = gameModel;
        this.player = player;
    }

    public void ChooseBatteries(List<Cannon> doubleCannons, Map<Battery, Integer> batteryUsage){

        if(smugglers.isDefeated()){
            throw new IllegalStateException("Smugglers is defeated");
        }

        if(doubleCannons==null || batteryUsage==null){
            throw new NullPointerException("Double cannons or Battery usage");
        }

        for(Cannon cannon : doubleCannons){
            if(cannon==null){
                throw new NullPointerException("Cannon is null");
            }
            if(cannon.getType()!= Cannon.Type.DOUBLE){
                throw new IllegalArgumentException("Cannon's type is not DOUBLE");
            }
        }

        int numBatteries = 0;
        for(Battery battery : batteryUsage.keySet()){
            numBatteries += batteryUsage.get(battery);
        }

        if(numBatteries != doubleCannons.size() || doubleCannons.size() != player.getShipBoard().getDoubleCannonsNumber()){
            throw new IllegalArgumentException("Number of batteries or cannons is not correct");
        }

        double totCannonPower = player.getShipBoard().getCannonsPower(doubleCannons);
        player.getShipBoard().useBatteries(batteryUsage);

        // go to next state

    }
}

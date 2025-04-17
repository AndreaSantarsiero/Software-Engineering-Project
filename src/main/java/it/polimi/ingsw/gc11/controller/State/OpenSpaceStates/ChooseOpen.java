package it.polimi.ingsw.gc11.controller.State.OpenSpaceStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.OpenSpace;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Map;

public class ChooseOpen implements AdventureState {

    private OpenSpace openSpace;
    private GameModel gameModel;
    private Player player;

    public ChooseOpen(OpenSpace openSpace, GameModel gameModel, Player player) {
        if(openSpace == null || gameModel == null || player == null) {
            throw new NullPointerException();
        }
        this.openSpace = openSpace;
        this.gameModel = gameModel;
        this.player = player;
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }

    public void chooseBatteries(Map<Battery, Integer> batteryUsage){

        int numBatteries = 0;
        for(Battery battery : batteryUsage.keySet()){
            numBatteries += batteryUsage.get(battery);
        }

        int totEnginePower = player.getShipBoard().getEnginesPower(numBatteries);
        player.getShipBoard().useBatteries(batteryUsage);

        //go to next state
    }
}

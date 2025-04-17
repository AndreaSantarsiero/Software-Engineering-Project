package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Map;

public class FightSmugglers implements AdventureState {

    private Smugglers smugglers;
    private GameModel gameModel;
    private Player player;
    private int cannonPower;

    public FightSmugglers(Smugglers smugglers, GameModel gameModel, Player player,  int cannonPower) {

        if (smugglers == null || gameModel == null || player == null) {
            throw new NullPointerException();
        }
        if (cannonPower < 0) {
            throw new IllegalArgumentException();
        }

        this.smugglers = smugglers;
        this.gameModel = gameModel;
        this.player = player;
        this.cannonPower = cannonPower;
    }

    @Override
    public void nextAdvState(AdventurePhase advContext) {

    }

    public void fight(Map<Battery, Integer> batteryUsage){
        if(cannonPower < smugglers.getFirePower()){
            int batteries = player.getShipBoard().removeMaterials(smugglers.getLostMaterials());
            if(batteries > 0){
                player.getShipBoard().useBatteries(batteryUsage);
            }
            //go to next state
        }
        if(cannonPower == smugglers.getFirePower()){
            //go to next state
        }
        if(cannonPower > smugglers.getFirePower()){
            smugglers.defeate();
            //go to next state
        }
    }
}

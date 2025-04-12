package it.polimi.ingsw.gc11.controller.State.SmugglersStates;

import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.Smugglers;

public class FightSmugglers extends AdventureState {

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

    public void fight(){
        if(cannonPower < smugglers.getFirePower()){
            player.getShipBoard().removeMaterials(smugglers.getFirePower());
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

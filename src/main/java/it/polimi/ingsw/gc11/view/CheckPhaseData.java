package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class CheckPhaseData extends GamePhaseData {

    public enum CheckState {
        CHOOSE_MAIN_MENU,
        CHOOSE_SHIPCARD_TO_REMOVE,
        WAIT_ENEMIES_SHIP, SHOW_ENEMIES_SHIP,
        REMOVE_SHIPCARDS_SETUP
    }



    private CheckState state;
    private CheckState previousState;
    private ShipBoard shipBoard;
    private boolean shipBoardLegal = false;
    private ArrayList<String> playersUsername;
    private final Map<String, ShipBoard> enemiesShipBoard;



    public CheckPhaseData() {
        enemiesShipBoard = new HashMap<>();
        state = CheckState.CHOOSE_MAIN_MENU;
    }

    public void initialize(ShipBoard shipBoard, ArrayList<String> playersUsername){
        this.shipBoard = shipBoard;
        shipBoardLegal = shipBoard.checkShip();
        this.playersUsername = playersUsername;
        notifyListener();
    }



    @Override
    public void notifyListener() {
        if(listener != null) {
            listener.update(this);
        }
    }



    public CheckState getState() {
        return state;
    }

    @Override
    public void updateState() {
        actualizePreviousState();

        if(state == CheckState.CHOOSE_SHIPCARD_TO_REMOVE || state == CheckState.SHOW_ENEMIES_SHIP || state == CheckState.REMOVE_SHIPCARDS_SETUP) {
            state = CheckState.CHOOSE_MAIN_MENU;
        }
        else if (state.ordinal() < CheckState.values().length - 1) {
            state = CheckState.values()[state.ordinal() + 1];
        }

        notifyListener();
    }

    public void setState(CheckState state) {
        actualizePreviousState();
        this.state = state;
        notifyListener();
    }

    public void actualizePreviousState() {
        previousState = state;
    }

    public boolean isStateNew() {
        return !state.equals(previousState);
    }



    @Override
    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        setState(CheckState.CHOOSE_MAIN_MENU);
    }



    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        shipBoardLegal = shipBoard.checkShip();
        updateState();
    }

    public boolean isShipBoardLegal() {
        return shipBoardLegal;
    }


    public ArrayList<String> getPlayersUsername() {
        return playersUsername;
    }

    public void setPlayersUsername(ArrayList<String> playersUsername) {
        this.playersUsername = playersUsername;
    }


    public Map<String, ShipBoard> getEnemiesShipBoard() {
        return enemiesShipBoard;
    }

    public void setEnemiesShipBoard(String username, ShipBoard shipBoard) {
        this.enemiesShipBoard.put(username, shipBoard);
    }



    @Override
    public void handle(ServerAction action) {
        action.loadData(this);
    }
}

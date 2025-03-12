package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level2ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.FlightBoard;
import java.io.Serializable;



public class Player {
    private String username;
    private int coins;
    private int position;
    private boolean abort;
    private ShipBoard shipBoard;
    //It will contain client's socket


    public Player(String username) {
        this.username = username;
        coins = 0;
        position = 0;
        abort = false;
        shipBoard = null;
    }


    public String getUsername() {
        return username;
    }
    public int getCoins() {
        return coins;
    }
    public void addCoins(int delta) {
        this.coins += delta;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public void setAbort() { this.abort = true; }

    public void setShipBoard(FlightBoard.Type flightType) throws NullPointerException, IllegalArgumentException {
        if (flightType == null)
            throw new NullPointerException();
        if (flightType == FlightBoard.Type.TRIAL)
            this.shipBoard = new Level1ShipBoard();
        else if (flightType == FlightBoard.Type.LEVEL2)
            this.shipBoard = new Level2ShipBoard();
        else
            throw new IllegalArgumentException();
    }
    public ShipBoard getShipBoard() {
        if(shipBoard instanceof Level1ShipBoard){
            Level1ShipBoard shipBoard1 = (Level1ShipBoard) shipBoard;
            return shipBoard1;  //aggiunta per far compilare
        }


        throw new IllegalArgumentException();
    }
}

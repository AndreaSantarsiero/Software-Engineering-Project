package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level2ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import java.io.Serializable;
import java.util.Map;



public class Player implements Serializable {

    private final String username;
    private Color color;
    private int coins;
    private int position;
    private boolean abort;
    private ShipBoard shipBoard;



    public enum Color implements Serializable{
        RED, GREEN, BLUE, YELLOW;
    }



    public Player(String username) {
        this.username = username;
        this.color = null;
        this.coins = 0;
        this.position = -1;
        this.abort = false;
        this.shipBoard = null;
    }



    public String getColor() {
        if (this.color == null) {
            return null;
        }
        return color.toString().toLowerCase();
    }

    public void setColor(String color, Map<String, HousingUnit> centralUnits) {
        switch (color) {
            case "red" -> {
                this.color = Color.RED;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
            case "green" -> {
                this.color = Color.GREEN;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
            case "blue" -> {
                this.color = Color.BLUE;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
            case "yellow" -> {
                this.color = Color.YELLOW;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
        }
    }


    public String getUsername() {
        return username;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int delta) {
        if(delta < 0) {
            throw new IllegalArgumentException("Delta value cannot be negative");
        }

        this.coins += delta;
    }

    public void removeCoins(int delta) {
        if(delta < 0) {
            throw new IllegalArgumentException("Delta value cannot be negative");
        }

        if (coins > delta) {
            coins -= delta;
        } else {
            coins = 0;
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isAbort() {return abort;}

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
        return shipBoard;
    }
}

package it.polimi.ingsw.gc11.model.shipboard;

import com.sun.jdi.InvalidTypeException;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ShipBoard {

    private ShipCard[][] components;
    private List<ShipCard> reservedComponents;
    private int lastModifiedX;
    private int lastModifiedY;


    public ShipBoard(int X_MAX, int Y_MAX) {
        ShipCard[][] components = new ShipCard[Y_MAX][X_MAX];
        reservedComponents = new ArrayList<ShipCard>();
        lastModifiedX = -1;
        lastModifiedY = -1;
    }


    private Boolean checkCoordinates(int x, int y) {
        return switch (this) {
            case Level1ShipBoard level1ShipBoard -> level1ShipBoard.validateCoordinates(x, y);
            case Level2ShipBoard level2ShipBoard -> level2ShipBoard.validateCoordinates(x, y);
            case Level3ShipBoard level3ShipBoard -> level3ShipBoard.validateCoordinates(x, y);
            default -> throw new IllegalStateException("Unknown ShipBoard type");
        };
    }

    public void addShipCard(ShipCard shipCard, int x, int y) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }
        if (!checkCoordinates(x, y)) {
            throw new IllegalArgumentException("Coordinates out of the ship");
        }
        components[y][x] = shipCard;
        lastModifiedX = x;
        lastModifiedY = y;
    }

    public void removeShipCard(int x, int y) {
        if (!checkCoordinates(x, y)) {
            throw new IllegalArgumentException("Coordinates out of the ship");
        }
        if (x == lastModifiedX && y == lastModifiedY) {
            components[y][x] = null;
        }
        else {
            throw new IllegalArgumentException("Ship card already welded");
        }
    }

    public ShipCard getShipCard(int x, int y) {
        if (!checkCoordinates(x, y)) {
            throw new IllegalArgumentException("Coordinates out of the ship");
        }
        return components[y][x];
    }



    public void reserveShipCard(ShipCard shipCard) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }
        if (reservedComponents.size() < 2) {
            reservedComponents.add(shipCard);
        }
        else {
            throw new TooManyShipCardsAlreadyReservedException("Ship card can't be reserved");
        }
    }

    public void useReservedShipCard(ShipCard shipCard) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }
        if (reservedComponents.isEmpty()) {
            throw new ReservedShipCardNotFoundException("Ship card not reserved");
        }
        if (reservedComponents.contains(shipCard)) {
            reservedComponents.remove(shipCard);
            components.add(shipCard);
        }
        else {
            throw new ReservedShipCardNotFoundException("Ship card not reserved");
        }
    }



    public int getTotalAvailableBatteries(){
        int availableBatteries = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Battery battery) {
                    availableBatteries += battery.getAvailableBatteries();
                }
            }
        }

        return availableBatteries;
    }



    public int getDoubleEnginesNumber(){
        int doubleEngines = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Engine engine) {
                    if(engine.getType() == Engine.Type.DOUBLE){
                        doubleEngines++;
                    }
                }
            }
        }

        return doubleEngines;
    }

    public int getEnginePower (int numBatteries) {
        if (numBatteries < 0) {
            throw new IllegalArgumentException("numBatteries can't be negative");
        }
        if(numBatteries > getDoubleEnginesNumber()){
            throw new IllegalArgumentException("numBatteries can't be greater than the number of double engines");
        }
        if(numBatteries > getTotalAvailableBatteries()){
            throw new IllegalArgumentException("numBatteries can't be greater than the number of available batteries");
        }

        int enginePower = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Engine engine) {
                    if(engine.getType() == Engine.Type.SINGLE){
                        enginePower++;
                    }
                }
            }
        }

        enginePower += 2*numBatteries;
        return enginePower;
    }



    public int getDoubleCannonsNumber(){
        int doubleCannons = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Cannon cannon) {
                    if(cannon.getType() == Cannon.Type.DOUBLE){
                        doubleCannons++;
                    }
                }
            }
        }

        return doubleCannons;
    }

    public int getCannonPower (int numBatteries) {
        if (numBatteries < 0) {
            throw new IllegalArgumentException("numBatteries can't be negative");
        }
        if(numBatteries > getDoubleCannonsNumber()){
            throw new IllegalArgumentException("numBatteries can't be greater than the number of double cannons");
        }
        if(numBatteries > getTotalAvailableBatteries()){
            throw new IllegalArgumentException("numBatteries can't be greater than the number of available batteries");
        }

        int cannonPower = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Cannon cannon) {
                    if(cannon.getType() == Cannon.Type.SINGLE){
                        cannonPower++;
                    }
                }
            }
        }

        cannonPower += 2*numBatteries;
        return cannonPower;
    }


//    + getHumans
//    + getBrownAliens
//    + getPurpleAliens
//    + getExposedConnectors
//    + getPositionShipCard (ShipCard card)
//
//    + checkShip
//
//    + killMember
//    + useBatteries
//    + removeMaterials
}

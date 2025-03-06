package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.ArrayList;
import java.util.List;


public abstract class ShipBoard {

    private ShipCard[][] components;
    private final List<ShipCard> reservedComponents;
    private int lastModifiedX;
    private int lastModifiedY;


    public ShipBoard(int X_MAX, int Y_MAX) {
        ShipCard[][] components = new ShipCard[Y_MAX][X_MAX];
        reservedComponents = new ArrayList<ShipCard>();
        lastModifiedX = -1;
        lastModifiedY = -1;
    }



    private void checkCoordinates(int x, int y) {
        Boolean result = switch (this) {
            case Level1ShipBoard level1ShipBoard -> level1ShipBoard.validateCoordinates(x, y);
            case Level2ShipBoard level2ShipBoard -> level2ShipBoard.validateCoordinates(x, y);
            case Level3ShipBoard level3ShipBoard -> level3ShipBoard.validateCoordinates(x, y);
            default -> throw new IllegalStateException("Unknown ShipBoard type");
        };

        if (!result) {
            throw new IllegalArgumentException("Coordinates out of the ship's bounds");
        }
    }


    public void addShipCard(ShipCard shipCard, int x, int y) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }

        checkCoordinates(x, y);
        components[y][x] = shipCard;
        lastModifiedX = x;
        lastModifiedY = y;
    }


    public void removeShipCard(int x, int y) {
        checkCoordinates(x, y);
        if (components[y][x] == null) {
            throw new IllegalArgumentException("Ship card already null");
        }
        if (x == lastModifiedX && y == lastModifiedY) {
            components[y][x] = null;
        }
        else {
            throw new IllegalArgumentException("Ship card already welded");
        }
    }


    public ShipCard getShipCard(int x, int y) {
        checkCoordinates(x, y);
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
            throw new IllegalStateException("Ship card can't be reserved");
        }
    }


    public void useReservedShipCard(ShipCard shipCard) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }
        if (reservedComponents.isEmpty()) {
            throw new IllegalStateException("Ship card not reserved");
        }
        if (reservedComponents.contains(shipCard)) {
            reservedComponents.remove(shipCard);
            components.add(shipCard);
        }
        else {
            throw new IllegalStateException("Ship card not reserved");
        }
    }



    public int getExposedConnectors(){
        int exposedConnectors = 0;

        return exposedConnectors;
    }


    public void finalizeShip(){
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                checkCoordinates(j, i);
            }
        }
    }


    public void checkShipConnections() {

    }


    public void checkShip() {

    }



    public int getBrownAliens() {
        int brownAliens = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof AlienUnit alienUnit) {
                    if(alienUnit.getType() == AlienUnit.Type.BROWN){
                        brownAliens++;
                    }
                }
            }
        }

        return brownAliens;
    }


    public int getPurpleAliens() {
        int purpleAliens = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof AlienUnit alienUnit) {
                    if(alienUnit.getType() == AlienUnit.Type.PURPLE){
                        purpleAliens++;
                    }
                }
            }
        }

        return purpleAliens;
    }


    public int getMembers(){
        int members = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof HousingUnit housingUnit) {
                    members += housingUnit.getNumHumans();
                }
            }
        }

        return members;
    }


    public void killMembers(List<HousingUnit> housingUnitModules, List<Integer> killedMembers) {
        if (housingUnitModules == null || killedMembers == null) {
            throw new IllegalArgumentException("Housing unit modules or killed members is null");
        }
        if (housingUnitModules.size() != killedMembers.size()) {
            throw new IllegalArgumentException("Housing unit modules and killed members do not match");
        }
        for (int i = 0; i < housingUnitModules.size(); i++) {
            HousingUnit housingUnit = housingUnitModules.get(i);
            int numMembers = killedMembers.get(i);
            housingUnit.killHumans(numMembers);
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


    public void useBatteries(List<Battery> batteryModules, List<Integer> usedBatteries) {
        if (batteryModules == null || usedBatteries == null) {
            throw new IllegalArgumentException("Battery modules or used batteries is null");
        }
        if (batteryModules.size() != usedBatteries.size()) {
            throw new IllegalArgumentException("Battery modules and used batteries do not match");
        }
        for (int i = 0; i < batteryModules.size(); i++) {
            Battery battery = batteryModules.get(i);
            int numBatteries = usedBatteries.get(i);
            battery.useBatteries(numBatteries);
        }
    }



    public int getTotalMaterialsValue(){
        int totalMaterialsValue = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Storage storage) {
                    totalMaterialsValue += storage.getMaterialsValue();
                }
            }
        }

        return totalMaterialsValue;
    }


    public int removeMaterials(int numMaterials) {
        int mostValuableMaterial = 0;
        Storage targetStorage = null;
        Material targetMaterial = null;
        Material materialToRemove = null;

        for (int k = 0; k < numMaterials; k++) {
            for (int i = 0; i < components.length; i++) {
                for (int j = 0; j < components[i].length; j++) {
                    if (components[i][j] instanceof Storage storage) {
                        try{
                            materialToRemove = storage.getMostValuedMaterial();
                            if(materialToRemove.getValue() > mostValuableMaterial) {
                                mostValuableMaterial = materialToRemove.getValue();
                                targetStorage = storage;
                                targetMaterial = materialToRemove;
                            }
                        }
                        catch(IllegalArgumentException _){

                        }
                    }
                }
            }

            if(targetStorage != null) {
                targetStorage.removeMaterial(targetMaterial);
            }
            else{
                return (numMaterials - k);
            }
        }

        return 0;
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
        enginePower += getBrownAliens();
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
        cannonPower += getPurpleAliens();
        return cannonPower;
    }
}

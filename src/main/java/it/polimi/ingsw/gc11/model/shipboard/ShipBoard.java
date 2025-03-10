package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a ship's board where ship components can be placed and managed
 * The board maintains information about ship cards, reserved components, and the last modified position
 */
public abstract class ShipBoard {

    private ShipCard[][] components;
    private final List<ShipCard> reservedComponents;
    private int lastModifiedX;
    private int lastModifiedY;


    /**
     * Constructs a ShipBoard with the specified dimensions
     *
     * @param X_MAX The maximum width of the board
     * @param Y_MAX The maximum height of the board
     */
    public ShipBoard(int X_MAX, int Y_MAX) {
        ShipCard[][] components = new ShipCard[Y_MAX][X_MAX];
        reservedComponents = new ArrayList<ShipCard>();
        lastModifiedX = -1;
        lastModifiedY = -1;
    }



    /**
     * Determines whether the given coordinates are valid within the ship's bounds based on the specific type of ShipBoard
     * This method delegates the validation to the corresponding subclass, ensuring that each type of ShipBoard applies its own coordinate validation logic
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return True if the coordinates are valid, false otherwise
     * @throws IllegalStateException If the ShipBoard type is unknown or if the coordinates are out of the board's bounds
     */
    private boolean booleanCheckCoordinates(int x, int y) {
        return switch (this) {
            case Level1ShipBoard level1ShipBoard -> level1ShipBoard.validateCoordinates(x, y);
            case Level2ShipBoard level2ShipBoard -> level2ShipBoard.validateCoordinates(x, y);
            case Level3ShipBoard level3ShipBoard -> level3ShipBoard.validateCoordinates(x, y);
            default -> throw new IllegalStateException("Unknown ShipBoard type");
        };
    }

    /**
     * Validates whether the given coordinates are within the allowed bounds of the ship
     * This method calls {@code booleanCheckCoordinates} to verify the coordinates and throws an exception if they are out of bounds
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @throws IllegalArgumentException If the coordinates are out of the ship's bounds
     */
    public void checkCoordinates(int x, int y) {
        if (!booleanCheckCoordinates(x, y)) {
            throw new IllegalArgumentException("Coordinates out of the ship's bounds");
        }
    }



    /**
     * Adds a ship card to the specified position on the board
     * This method is intended to be used only during the initial construction of the ship
     *
     * @param shipCard The ship card to be added
     * @param x        The x-coordinate where the card is placed
     * @param y        The y-coordinate where the card is placed
     * @throws IllegalArgumentException if the ship card is null or if coordinates are invalid
     */
    public void addShipCard(ShipCard shipCard, int x, int y) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }

        checkCoordinates(x, y);
        components[y][x] = shipCard;
        lastModifiedX = x;
        lastModifiedY = y;
    }

    /**
     * Removes a ship card from the specified position on the board
     * This method is intended to be used only during the initial construction of the ship
     *
     * @param x The x-coordinate of the card to be removed
     * @param y The y-coordinate of the card to be removed
     * @throws IllegalArgumentException if the card is already null or welded
     */
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

    /**
     * Retrieves the ship card at the specified position
     *
     * @param x The x-coordinate of the card to retrieve
     * @param y The y-coordinate of the card to retrieve
     * @return The ship card at the given position
     * @throws IllegalArgumentException if the coordinates are invalid
     */
    public ShipCard getShipCard(int x, int y) {
        checkCoordinates(x, y);
        return components[y][x];
    }



    /**
     * Reserves a ship card for later use
     * This method is intended to be used only during the initial construction of the ship
     *
     * @param shipCard The ship card to be reserved
     * @throws IllegalArgumentException if the ship card is null
     * @throws IllegalStateException if more than two ship cards are reserved
     */
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

    /**
     * Moves a reserved ship card onto the ship board at the specified coordinates
     * This method ensures that the ship card is valid, has been previously reserved, and is placed within the ship's valid bounds before adding it to the board
     * This method is intended to be used only during the initial construction of the ship
     *
     * @param shipCard The reserved ship card to be placed on the board
     * @param x The x-coordinate where the ship card should be placed
     * @param y The y-coordinate where the ship card should be placed
     * @throws IllegalArgumentException if the ship card is null
     * @throws IllegalStateException if the ship card was not reserved or the reservation list is empty
     * @throws IllegalArgumentException if the coordinates are out of bounds
     */
    public void useReservedShipCard(ShipCard shipCard, int x, int y) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }
        if (reservedComponents.isEmpty()) {
            throw new IllegalStateException("Ship card not previously reserved");
        }
        if (reservedComponents.contains(shipCard)) {
            checkCoordinates(x, y);
            reservedComponents.remove(shipCard);
            components[y][x] = shipCard;
        }
        else {
            throw new IllegalStateException("Ship card not previously reserved");
        }
    }



    /**
     * Checks if two connectors can be connected based on their type
     * Two connectors are compatible if they are identical or if one of them is UNIVERSAL and the other is not NONE
     *
     * @param connector1 The first connector to be checked
     * @param connector2 The second connector to be checked
     * @return True if the connectors can be connected, false otherwise
     */
    private boolean checkConnection(ShipCard.Connector connector1, ShipCard.Connector connector2) {
        if (connector1 == connector2) {
            return true;
        }
        else if (connector1 == ShipCard.Connector.UNIVERSAL && connector2 != ShipCard.Connector.NONE) {
            return true;
        }
        else if (connector2 == ShipCard.Connector.UNIVERSAL && connector1 != ShipCard.Connector.NONE) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Counts the number of exposed connectors of the ship
     *
     * @return The number of exposed connectors
     */
    public int getExposedConnectors(){
        int exposedConnectors = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null) {
                    if (components[i][j].getRightConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkCoordinates(j+1, i);
                            if (components[i][j+1] == null) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                    if (components[i][j].getLeftConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkCoordinates(j-1, i);
                            if (components[i][j-1] == null) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                    if (components[i][j].getTopConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkCoordinates(j, i-1);
                            if (components[i-1][j] == null) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                    if (components[i][j].getBottomConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkCoordinates(j, i+1);
                            if (components[i+1][j] == null) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                }
            }
        }

        return exposedConnectors;
    }

    /**
     * Ensures all ship components are within valid bounds
     *
     * @throws IllegalArgumentException if any component is out of bounds
     */
    private void checkShipBounds(){
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] != null){
                    checkCoordinates(j, i);
                }
            }
        }
    }



    /**
     * Verifies that all ship components are properly connected
     *
     * @return True if all connections are valid, false otherwise
     */
    private boolean checkShipConnections() {
        boolean status = true;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null) {
                    if(components[i][j-1] != null) {
                        if(!checkConnection(components[i][j].getLeftConnector(), components[i][j-1].getRightConnector())){
                            components[i][j].setBadWelded(true);
                            components[i][j-1].setBadWelded(true);
                            status = false;
                        }
                    }
                    if(components[i][j+1] != null) {
                        if(!checkConnection(components[i][j].getRightConnector(), components[i][j+1].getLeftConnector())){
                            components[i][j].setBadWelded(true);
                            components[i][j+1].setBadWelded(true);
                            status = false;
                        }
                    }
                    if(components[i][j-1] != null) {
                        if(!checkConnection(components[i][j].getBottomConnector(), components[i-1][j].getTopConnector())){
                            components[i][j].setBadWelded(true);
                            components[i-1][j].setBadWelded(true);
                            status = false;
                        }
                    }
                    if(components[i][j-1] != null) {
                        if(!checkConnection(components[i][j].getTopConnector(), components[i][j-1].getBottomConnector())){
                            components[i][j].setBadWelded(true);
                            components[i+1][j].setBadWelded(true);
                            status = false;
                        }
                    }
                }

                j++;
                if(j == components[i].length) {
                    j = 0;
                    i++;
                }
            }
        }

        return status;
    }



    /**
     * Checks the structural integrity of the ship
     *
     * @return True if the ship structure is valid, false otherwise
     */
    public boolean checkShipIntegrity(){
        boolean status = true;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null) {

                }
            }
        }

        return status;
    }



    /**
     * Ensures additional ship restrictions are met:
     * - all engines must point at the back and have a free space behind them
     * - all cannons must have only free spaces in front of them
     *
     * @throws IllegalStateException if a restriction is violated
     */
    private void checkOtherRestrictions(){
        int k = 1;
        boolean status = true;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null) {
                    if (components[i][j] instanceof Engine engine) {
                        if (engine.getOrientation() != ShipCard.Orientation.DEG_0){
                            throw new IllegalStateException("Engine must point to the back");
                        }
                        try {
                            checkCoordinates(j, i+1);
                            if (components[i+1][j] != null) {
                                status = false;
                            }
                        } catch (Exception _) {

                        }
                        if (!status) {
                            throw new IllegalStateException("Cannot place a ship card behind an engine");
                        }
                    }

                    if (components[i][j] instanceof Cannon cannon) {
                        k = 1;
                        if (cannon.getOrientation() == ShipCard.Orientation.DEG_0) {
                            while(i >= k){
                                try {
                                    checkCoordinates(j, i-k);
                                    if (components[i-k][j] != null) {
                                        status = false;
                                    }
                                } catch (Exception _) {

                                }
                                k++;
                            }
                        }
                        else if (cannon.getOrientation() == ShipCard.Orientation.DEG_90) {
                            while(i >= k){
                                try {
                                    checkCoordinates(j+k, i);
                                    if (components[i][j+k] != null) {
                                        status = false;
                                    }
                                } catch (Exception _) {

                                }
                                k++;
                            }
                        }
                        else if (cannon.getOrientation() == ShipCard.Orientation.DEG_180) {
                            while(i >= k){
                                try {
                                    checkCoordinates(j, i+k);
                                    if (components[i+k][j] != null) {
                                        status = false;
                                    }
                                } catch (Exception _) {

                                }
                                k++;
                            }
                        }
                        else if (cannon.getOrientation() == ShipCard.Orientation.DEG_270) {
                            while(i >= k){
                                try {
                                    checkCoordinates(j-k, i);
                                    if (components[i][j-k] != null) {
                                        status = false;
                                    }
                                } catch (Exception _) {

                                }
                                k++;
                            }
                        }

                        if (!status) {
                            throw new IllegalStateException("Cannot place a ship card in front of a cannon");
                        }
                    }
                }
            }
        }
    }



    /**
     * Performs a full validation of the ship, checking bounds, connections, integrity, and specific component placement rules
     *
     * @throws IllegalArgumentException if the ship fails any validation check
     */
    public void checkShip() {
        checkShipBounds();
        if(!checkShipConnections()){
            throw new IllegalArgumentException("Illegal ship connections detected");
        }
        if(!checkShipIntegrity()){
            throw new IllegalArgumentException("Illegal ship integrity detected");
        }
        checkOtherRestrictions();
    }



    /**
     * Counts the number of brown aliens present in the ship
     *
     * @return The number of brown aliens
     */
    public int getBrownAliens() {
        int brownAliens = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof AlienUnit alienUnit && !components[i][j].isScrap()) {
                    if(alienUnit.getType() == AlienUnit.Type.BROWN){
                        brownAliens++;
                    }
                }
            }
        }

        return brownAliens;
    }

    /**
     * Counts the number of purple aliens present in the ship
     *
     * @return The number of purple aliens
     */
    public int getPurpleAliens() {
        int purpleAliens = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof AlienUnit alienUnit && !components[i][j].isScrap()) {
                    if(alienUnit.getType() == AlienUnit.Type.PURPLE){
                        purpleAliens++;
                    }
                }
            }
        }

        return purpleAliens;
    }

    /**
     * Counts the total number of crew members in the ship
     *
     * @return The number of crew members
     */
    public int getMembers(){
        int members = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof HousingUnit housingUnit && !components[i][j].isScrap()) {
                    members += housingUnit.getNumMembers();
                }
            }
        }

        return members;
    }

    /**
     * Removes a specified number of crew members from designated housing units
     *
     * @param housingUnitModules The housing units from which members are removed
     * @param killedMembers      The number of members to remove from each unit
     * @throws IllegalArgumentException if input lists are null, mismatched in size, or invalid
     */
    public void killMembers(List<HousingUnit> housingUnitModules, List<Integer> killedMembers) {
        if (housingUnitModules == null || killedMembers == null) {
            throw new IllegalArgumentException("Housing unit modules or killed members is null");
        }
        if (housingUnitModules.size() != killedMembers.size()) {
            throw new IllegalArgumentException("Housing unit modules and killed members do not match");
        }
        for (int i = 0; i < housingUnitModules.size(); i++) {
            HousingUnit housingUnit = housingUnitModules.get(i);
            if (!housingUnit.isScrap()){
                throw new IllegalArgumentException("Scraps cannot be used anymore");
            }
            int numMembers = killedMembers.get(i);
            housingUnit.killMembers(numMembers);
        }
    }



    /**
     * Calculates the total number of available batteries
     *
     * @return The total count of available batteries
     */
    public int getTotalAvailableBatteries(){
        int availableBatteries = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Battery battery && !components[i][j].isScrap()) {
                    availableBatteries += battery.getAvailableBatteries();
                }
            }
        }

        return availableBatteries;
    }

    /**
     * Uses a specified number of batteries from designated battery modules
     *
     * @param batteryModules The battery units to use batteries from
     * @param usedBatteries  The number of batteries to use from each unit
     * @throws IllegalArgumentException if input lists are null, mismatched in size, or invalid
     */
    public void useBatteries(List<Battery> batteryModules, List<Integer> usedBatteries) {
        if (batteryModules == null || usedBatteries == null) {
            throw new IllegalArgumentException("Battery modules or used batteries is null");
        }
        if (batteryModules.size() != usedBatteries.size()) {
            throw new IllegalArgumentException("Battery modules and used batteries do not match");
        }
        for (int i = 0; i < batteryModules.size(); i++) {
            Battery battery = batteryModules.get(i);
            if (!battery.isScrap()){
                throw new IllegalArgumentException("Scraps cannot be used anymore");
            }
            int numBatteries = usedBatteries.get(i);
            battery.useBatteries(numBatteries);
        }
    }



    /**
     * Calculates the total material value stored in the ship
     *
     * @return The total value of materials
     */
    public int getTotalMaterialsValue(){
        int totalMaterialsValue = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Storage storage && !components[i][j].isScrap()) {
                    totalMaterialsValue += storage.getMaterialsValue();
                }
            }
        }

        return totalMaterialsValue;
    }

    /**
     * Removes materials from storage, prioritizing the most valuable
     *
     * @param numMaterials The number of materials to remove
     * @return The number of materials that could not be removed due to shortages
     */
    public int removeMaterials(int numMaterials) {
        int mostValuableMaterial = 0;
        Storage targetStorage = null;
        Material targetMaterial = null;
        Material materialToRemove = null;

        for (int k = 0; k < numMaterials; k++) {
            for (int i = 0; i < components.length; i++) {
                for (int j = 0; j < components[i].length; j++) {
                    if (components[i][j] instanceof Storage storage && !components[i][j].isScrap()) {
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



    /**
     * Counts the number of double engines in the ship
     *
     * @return The number of double engines
     */
    public int getDoubleEnginesNumber(){
        int doubleEngines = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Engine engine && !components[i][j].isScrap()) {
                    if(engine.getType() == Engine.Type.DOUBLE){
                        doubleEngines++;
                    }
                }
            }
        }

        return doubleEngines;
    }

    /**
     * Computes the total engine power based on available engines and batteries
     *
     * @param numBatteries The number of batteries allocated for double engines
     * @return The calculated engine power
     * @throws IllegalArgumentException if numBatteries is negative or exceeds available limits
     */
    public int getEnginesPower (int numBatteries) {
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
                if (components[i][j] instanceof Engine engine && !components[i][j].isScrap()) {
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



    /**
     * Counts the number of double cannons in the ship
     *
     * @return The number of double cannons
     */
    public int getDoubleCannonsNumber(){
        int doubleCannons = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] instanceof Cannon cannon && !components[i][j].isScrap()) {
                    if(cannon.getType() == Cannon.Type.DOUBLE){
                        doubleCannons++;
                    }
                }
            }
        }

        return doubleCannons;
    }

    /**
     * Computes the total cannon power based on available cannons and batteries
     *
     * @param numBatteries The number of batteries allocated for double cannons
     * @return The calculated cannon power
     * @throws IllegalArgumentException if numBatteries is negative or exceeds available limits
     */
    public int getCannonsPower (int numBatteries) {
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
                if (components[i][j] instanceof Cannon cannon && !components[i][j].isScrap()) {
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



    /**
     * Determines whether this ship is being protected from the given direction by any functional shield
     *
     * @param direction The direction from which an attack is coming
     * @return {@code true} if there is at least one active shield protecting from the given direction, {@code false} otherwise
     */
    public boolean isBeingProtected(Hit.Direction direction) {
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof Shield shield && !components[i][j].isScrap()){
                    if(shield.isProtecting(direction)){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

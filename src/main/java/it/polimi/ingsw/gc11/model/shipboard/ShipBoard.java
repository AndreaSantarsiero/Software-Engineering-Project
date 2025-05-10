package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.io.Serializable;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.awt.Point;



/**
 * Represents a ship's board where ship components can be placed and managed
 * <p>
 * The board maintains information about ship cards, reserved components, and the last modified position
 */

public abstract class ShipBoard  implements Serializable {

    private final ShipCard[][] components;
    private final List<ShipCard> reservedComponents;

    private final Map<AlienUnit, Point> alienUnits;
    private final List<Battery> batteries;
    private final Map<Cannon, Point> cannons;
    private final Map<Engine, Point> engines;
    private final Map<HousingUnit, Point> housingUnits;
    private final List<Shield> shields;
    private final List<Storage> storages;

    private int lastModifiedI;
    private int lastModifiedJ;
    private AlienUnit brownActiveUnit;
    private AlienUnit purpleActiveUnit;


    /**
     * Constructs a ShipBoard with the specified dimensions
     *
     * @param X_MAX The maximum width of the board
     * @param Y_MAX The maximum height of the board
     */
    public ShipBoard(int X_MAX, int Y_MAX) {
        this.components = new ShipCard[Y_MAX][X_MAX];
        this.reservedComponents = new ArrayList<>();
        this.alienUnits = new HashMap<>();
        this.batteries = new ArrayList<>();
        this.cannons = new HashMap<>();
        this.engines = new HashMap<>();
        this.housingUnits = new HashMap<>();
        this.shields = new ArrayList<>();
        this.storages = new ArrayList<>();
        this.lastModifiedI = -1;
        this.lastModifiedJ = -1;
        this.brownActiveUnit = null;
        this.purpleActiveUnit = null;
    }


    public void addToList(AlienUnit alienUnit, int x, int y) {
        alienUnits.putIfAbsent(alienUnit, new Point(x, y));
    }

    public void addToList(Battery battery, int x, int y) {
        batteries.add(battery);
    }

    public void addToList(Cannon cannon, int x, int y) {
        cannons.putIfAbsent(cannon, new Point(x, y));
    }

    public void addToList(Engine engine, int x, int y) {
        engines.putIfAbsent(engine, new Point(x, y));
    }

    public void addToList(HousingUnit housingUnit, int x, int y) {
        housingUnits.putIfAbsent(housingUnit, new Point(x, y));
    }

    public void addToList(Shield shield, int x, int y) {
        shields.add(shield);
    }

    public void addToList(Storage storage, int x, int y) {
        storages.add(storage);
    }

    public void addToList(StructuralModule structuralModule, int x, int y) {

    }



    public void removeFromList(AlienUnit alienUnit) {
        alienUnits.remove(alienUnit);
    }

    public void removeFromList(Battery battery) {
        batteries.remove(battery);
    }

    public void removeFromList(Cannon cannon) {
        cannons.remove(cannon);
    }

    public void removeFromList(Engine engine) {
        engines.remove(engine);
    }

    public void removeFromList(HousingUnit housingUnit) {
        housingUnits.remove(housingUnit);
    }

    public void removeFromList(Shield shield) {
        shields.remove(shield);
    }

    public void removeFromList(Storage storage) {
        storages.remove(storage);
    }

    public void removeFromList(StructuralModule structuralModule) {

    }



    /**
     * Adjusts the given X coordinate according to the level of the shipboard
     *
     * @param x The original X coordinate
     * @return The adjusted X coordinate
     */
    public abstract int adaptX (int x);

    /**
     * Adjusts the given Y coordinate according to the level of the shipboard
     *
     * @param y The original Y coordinate
     * @return The adjusted Y coordinate
     */
    public abstract int adaptY (int y);



    /**
     * Returns the width of the shipboard (x coordinate)
     *
     * @return The number of columns in the shipboard
     */
    public int getWidth(){
        return this.components[0].length;
    }

    /**
     * Returns the length of the shipboard (y coordinate)
     *
     * @return The number of rows in the shipboard
     */
    public int getLength(){
        return this.components.length;
    }




    /**
     * Determines whether the given coordinate indexes are valid within the ship's bounds based on the specific type of ShipBoard
     * <p>
     * This method delegates the validation to the corresponding subclass, ensuring that each type of ShipBoard applies its own coordinate validation logic
     *
     * @param j The x-coordinate index to check
     * @param i The y-coordinate index to check
     * @return True if the coordinate indexes are valid, false otherwise
     * @throws IllegalArgumentException If the coordinate indexes are out of the board's bounds
     */
    public abstract boolean validateIndexes(int j, int i);

    /**
     * Validates whether the given coordinate indexes are within the allowed bounds of the ship.
     * This method calls {@code validateIndexes} to verify the coordinate indexes and throws an exception if they are out of bounds
     *
     * @param j The x-coordinate index to check
     * @param i The y-coordinate index to check
     * @throws IllegalArgumentException If the coordinate indexes are out of the ship's bounds
     */
    public void checkIndexes(int j, int i) {
        if (!validateIndexes(j, i)) {
            throw new IllegalArgumentException("Coordinate indexes out of the ship's bounds");
        }
    }



    /**
     * Adds a ship card to the specified position on the board
     * <p>
     * This method is intended to be used only during the initial construction of the ship
     *
     * @param shipCard The ship card to be added
     * @param x The x-coordinate where the card is placed
     * @param y The y-coordinate where the card is placed
     * @throws IllegalArgumentException if the ship card is null or if coordinates are invalid
     */
    public void addShipCard(ShipCard shipCard, int x, int y) {
        if (shipCard == null) {
            throw new IllegalArgumentException("Ship card is null");
        }

        //non manca controllo sulle coordinate x e y?

        int i = adaptY(y);
        int j = adaptX(x);
        checkIndexes(j, i);
        components[i][j] = shipCard;
        lastModifiedI = i;
        lastModifiedJ = j;
        shipCard.place(this, x, y);
    }

    /**
     * Removes a ship card from the specified position on the board
     * <p>
     * This method is intended to be used only during the initial construction of the ship
     *
     * @param x The x-coordinate of the card to be removed
     * @param y The y-coordinate of the card to be removed
     * @throws IllegalArgumentException if the card is already null or welded
     */
    public void removeShipCard(int x, int y) {
//        if (x < 0 || y < 0) {
//            throw new IllegalArgumentException();
//        } non manca questo controllo? seguo chiamata GameContext-->GamePhase-->BuildingPhase-->gameModel.removeShipcard

        int i = adaptY(y);
        int j = adaptX(x);
        checkIndexes(j, i);
        if (components[i][j] == null) {
            throw new IllegalArgumentException("Ship card already null");
        }
        if (j == lastModifiedJ && i == lastModifiedI) {
            components[i][j].unPlace(this);
            components[i][j] = null;
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
        int i = adaptY(y);
        int j = adaptX(x);
        checkIndexes(j, i);
        return components[i][j];
    }



    /**
     * Reserves a ship card for later use
     * <p>
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
     * <p>
     * This method ensures that the ship card is valid, has been previously reserved, and is placed within the ship's valid bounds before adding it to the board.
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
            int i = adaptY(y);
            int j = adaptX(x);
            checkIndexes(j, i);
            reservedComponents.remove(shipCard);
            components[i][j] = shipCard;
        }
        else {
            throw new IllegalStateException("Ship card not previously reserved");
        }
    }

    /**
     * Return the list of the reserved ship cards
     *
     * @return the list of the reserved ship cards
     */
    public List<ShipCard> getReservedComponents() {
        return this.reservedComponents;
    }



    /**
     * Counts the total number of ShipCards used to build the ship that are still not destroyed
     *
     * @return The total number of ShipCards used to build the ship that are still not destroyed
     */
    public int getShipCardsNumber(){
        int numComponents = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                try {
                    checkIndexes(j, i);
                    if (components[i][j] != null && !components[i][j].isScrap()) {
                        numComponents++;
                    }
                }
                catch (Exception _) {

                }
            }
        }

        return numComponents;
    }

    /**
     * Counts the total number of ShipCards used to build the ship that got destroyed or were reserved and remained unused
     *
     * @return The total number of ShipCards used to build the ship that got destroyed or were reserved and remained unused
     */
    public int getScrapedCardsNumber(){
        int numComponents = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                try {
                    checkIndexes(j, i);
                    if (components[i][j] != null && components[i][j].isScrap()) {
                        numComponents++;
                    }
                }
                catch (Exception _) {

                }
            }
        }

        numComponents += reservedComponents.size();
        return numComponents;
    }



    /**
     * Checks if two connectors can be connected based on their type
     * <p>
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
     * Counts the number of exposed connectors of the ship (assuming that the ship doesn't have any illegal connection)
     *
     * @return The number of exposed connectors
     */
    public int getExposedConnectors(){
        int exposedConnectors = 0;

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null && !components[i][j].isScrap()) {
                    if (components[i][j].getRightConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkIndexes(j+1, i);
                            if (components[i][j+1] == null || components[i][j+1].isScrap()) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                    if (components[i][j].getLeftConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkIndexes(j-1, i);
                            if (components[i][j-1] == null || components[i][j-1].isScrap()) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                    if (components[i][j].getTopConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkIndexes(j, i-1);
                            if (components[i-1][j] == null || components[i-1][j].isScrap()) {
                                exposedConnectors++;
                            }
                        }
                        catch (Exception e) {
                            exposedConnectors++;
                        }
                    }
                    if (components[i][j].getBottomConnector() != ShipCard.Connector.NONE) {
                        try {
                            checkIndexes(j, i+1);
                            if (components[i+1][j] == null || components[i+1][j].isScrap()) {
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
     * Ensures every ship component is within valid bounds and reset its illegal status to false
     *
     * @throws IllegalArgumentException if any component is out of ship's bounds
     */
    private void checkShipInitialization(){
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] != null && !components[i][j].isScrap()){
                    checkIndexes(j, i);
                    components[i][j].setIllegal(false);
                }
            }
        }
    }

    /**
     * Initializes the visited state of the components
     * <p>
     * This method iterates through the {@code components} array and sets the visited flag to {@code false}
     * for all non-null, non-scrap components
     */
    private void visitedInitialization(){
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] != null && !components[i][j].isScrap()){
                    components[i][j].setVisited(false);
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
            for (int j = (i % 2); j < components[i].length; j += 2) {
                if (components[i][j] != null && !components[i][j].isScrap()) {
                    try {
                        if(components[i][j-1] != null && !components[i][j-1].isScrap()) {
                            if(!checkConnection(components[i][j].getLeftConnector(), components[i][j-1].getRightConnector())){
                                components[i][j].setIllegal(true);
                                components[i][j-1].setIllegal(true);
                                status = false;
                            }
                        }
                    }
                    catch (Exception _) {

                    }

                    try {
                        if(components[i][j+1] != null && !components[i][j+1].isScrap()) {
                            if(!checkConnection(components[i][j].getRightConnector(), components[i][j+1].getLeftConnector())){
                                components[i][j].setIllegal(true);
                                components[i][j+1].setIllegal(true);
                                status = false;
                            }
                        }
                    }
                    catch (Exception _) {

                    }

                    try {
                        if(components[i-1][j] != null && !components[i-1][j].isScrap()) {
                            if(!checkConnection(components[i][j].getTopConnector(), components[i-1][j].getBottomConnector())){
                                components[i][j].setIllegal(true);
                                components[i-1][j].setIllegal(true);
                                status = false;
                            }
                        }
                    }
                    catch (Exception _) {

                    }

                    try {
                        if(components[i+1][j] != null && !components[i+1][j].isScrap()) {
                            if(!checkConnection(components[i][j].getBottomConnector(), components[i+1][j].getTopConnector())){
                                components[i][j].setIllegal(true);
                                components[i+1][j].setIllegal(true);
                                status = false;
                            }
                        }
                    }
                    catch (Exception _) {

                    }
                }
            }
        }

        return status;
    }



    /**
     * Checks if the ship is structurally valid (all components are connected together)
     *
     * @return True if the entire ship is connected, false otherwise
     * @throws IllegalStateException if the ship has no components
     */
    public boolean checkShipIntegrity(){
        int connectedComponents;
        this.visitedInitialization();
        ShipCard centralUnit = this.getShipCard(7, 7);

        /* trying to start from the central housing unit */
        if (centralUnit != null && !centralUnit.isScrap()) {
            connectedComponents = integrityVerifier(adaptX(7), adaptY(7));
            if (connectedComponents == this.getShipCardsNumber()) {
                return true;
            }
            else {
                return false;
            }
        }

        /* trying to start from any other valid ship card */
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null && !components[i][j].isScrap()) {
                    connectedComponents = integrityVerifier(j, i);
                    if (connectedComponents == this.getShipCardsNumber()) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        throw new IllegalStateException("Ship does not contain any component (or they're all scraped)");
    }



    /**
     * Recursively verifies the ship's connectivity
     *
     * @param x The x-coordinate of the current card
     * @param y The y-coordinate of the current card
     * @return The total number of connected cards, including itself
     */
    private int integrityVerifier(int x, int y){
        try{
            ShipCard shipCard = this.getShipCard(x - adaptX(0), y - adaptY(0));
            shipCard.setVisited(true);
            boolean finalComponent = true;
            int connectedComponents = 1;

            if (shipCard.getTopConnector() != ShipCard.Connector.NONE) {
                try {
                    checkIndexes(x, y-1);
                    if (components[y-1][x] != null && !components[y-1][x].isVisited() && !components[y-1][x].isScrap()) {
                        finalComponent = false;
                        connectedComponents += integrityVerifier(x, y-1);   /* inductive step */
                    }
                }
                catch (Exception _) {

                }
            }
            if (shipCard.getRightConnector() != ShipCard.Connector.NONE) {
                try {
                    checkIndexes(x+1, y);
                    if (components[y][x+1] != null && !components[y][x+1].isVisited() && !components[y][x+1].isScrap()) {
                        finalComponent = false;
                        connectedComponents += integrityVerifier(x+1, y);   /* inductive step */
                    }
                }
                    catch (Exception _) {

                }
            }
            if (shipCard.getBottomConnector() != ShipCard.Connector.NONE) {
                try {
                    checkIndexes(x, y+1);
                    if (components[y+1][x] != null && !components[y+1][x].isVisited() && !components[y+1][x].isScrap()) {
                        finalComponent = false;
                        connectedComponents += integrityVerifier(x, y+1);   /* inductive step */
                    }
                }
                catch (Exception _) {

                }
            }
            if (shipCard.getLeftConnector() != ShipCard.Connector.NONE) {
                try {
                    checkIndexes(x-1, y);
                    if (components[y][x-1] != null && !components[y][x-1].isVisited() && !components[y][x-1].isScrap()) {
                        finalComponent = false;
                        connectedComponents += integrityVerifier(x-1, y);   /* inductive step */
                    }
                }
                catch (Exception _) {

                }
            }

            if (finalComponent) {
                return 1;   /* base case */
            }

            return connectedComponents;
        }
        catch (Exception _) {
            return 0;
        }
    }



    /**
     * Ensures that additional ship restrictions are met:
     * <ul>
     *     <li>All engines must be facing the back and have a free space behind them</li>
     *     <li>All cannons must have a free space in front of the direction they are aiming</li>
     * </ul>
     *
     * @return {@code false} if any restriction is violated, {@code true} otherwise
     */
    private boolean checkOtherRestrictions(){
        boolean status = true;

        for (Map.Entry<Cannon, Point> entry : cannons.entrySet()) {
            Cannon cannon = entry.getKey();
            int i = adaptY((int )entry.getValue().getY());
            int j = adaptX((int )entry.getValue().getX());

            if(!cannon.isScrap()){
                if (cannon.getOrientation() == ShipCard.Orientation.DEG_0) {
                    try {
                        checkIndexes(j, i-1);
                        if (components[i-1][j] != null) {
                            cannon.setIllegal(true);
                            components[i-1][j].setIllegal(true);
                            status = false;
                        }
                    } catch (Exception _) {

                    }
                }
                else if (cannon.getOrientation() == ShipCard.Orientation.DEG_90) {
                    try {
                        checkIndexes(j+1, i);
                        if (components[i][j+1] != null) {
                            cannon.setIllegal(true);
                            components[i][j+1].setIllegal(true);
                            status = false;
                        }
                    } catch (Exception _) {

                    }
                }
                else if (cannon.getOrientation() == ShipCard.Orientation.DEG_180) {
                    try {
                        checkIndexes(j, i+1);
                        if (components[i+1][j] != null) {
                            cannon.setIllegal(true);
                            components[i+1][j].setIllegal(true);
                            status = false;
                        }
                    } catch (Exception _) {

                    }
                }
                else if (cannon.getOrientation() == ShipCard.Orientation.DEG_270) {
                    try {
                        checkIndexes(j-1, i);
                        if (components[i][j-1] != null) {
                            cannon.setIllegal(true);
                            components[i][j-1].setIllegal(true);
                            status = false;
                        }
                    } catch (Exception _) {

                    }
                }
            }
        }

        for (Map.Entry<Engine, Point> entry : engines.entrySet()) {
            Engine engine = entry.getKey();
            int i = adaptY((int )entry.getValue().getY());
            int j = adaptX((int )entry.getValue().getX());


            if(!engine.isScrap()){
                if (engine.getOrientation() != ShipCard.Orientation.DEG_0){
                    engine.setIllegal(true);
                    status = false;
                }
                try {
                    checkIndexes(j, i+1);
                    if (components[i+1][j] != null) {
                        engine.setIllegal(true);
                        components[i+1][j].setIllegal(true);
                        status = false;
                    }
                } catch (Exception _) {

                }
            }
        }

        return status;
    }



    /**
     * Performs a full validation of the ship, checking bounds, connections, integrity, and specific component placement rules
     *
     * @return false if the ship fails any validation check, true otherwise
     */
    public boolean checkShip() {
        checkShipInitialization();

        return checkShipConnections() && checkShipIntegrity() && checkOtherRestrictions();
    }



    /**
     * Connects an AlienUnit to a HousingUnit based on their coordinates and connector compatibility.
     * If all conditions are met, the AlienUnit is linked to the HousingUnit
     *
     * @param alienUnit the AlienUnit to connect
     * @param housingUnit the HousingUnit to connect
     * @throws IllegalArgumentException if not all conditions are met
     */
    public void connectAlienUnit(AlienUnit alienUnit, HousingUnit housingUnit) {
        if(alienUnit.getType() == AlienUnit.Type.BROWN && brownActiveUnit != null) {
            throw new IllegalArgumentException("This ship has already activate a brown AlienUnit");
        }
        if(alienUnit.getType() == AlienUnit.Type.PURPLE && purpleActiveUnit != null) {
            throw new IllegalArgumentException("This ship has already activate a purple AlienUnit");
        }
        if(housingUnit.isCentral()){
            throw new IllegalArgumentException("Cannot connect an AlienUnit to a central HousingUnit");
        }

        int alienX = alienUnits.get(alienUnit).x;
        int alienY = alienUnits.get(alienUnit).y;
        int housingX = housingUnits.get(housingUnit).x;
        int housingY = housingUnits.get(housingUnit).y;

        if(alienX == housingX && alienY == housingY+1 && alienUnit.getTopConnector() != ShipCard.Connector.NONE && checkConnection(alienUnit.getTopConnector(), housingUnit.getBottomConnector())){
            housingUnit.setAlienUnit(alienUnit);
        }
        else if(alienX == housingX-1 && alienY == housingY && alienUnit.getRightConnector() != ShipCard.Connector.NONE && checkConnection(alienUnit.getRightConnector(), housingUnit.getLeftConnector())){
            housingUnit.setAlienUnit(alienUnit);
        }
        else if(alienX == housingX && alienY == housingY-1 && alienUnit.getBottomConnector() != ShipCard.Connector.NONE && checkConnection(alienUnit.getBottomConnector(), housingUnit.getTopConnector())){
            housingUnit.setAlienUnit(alienUnit);
        }
        else if(alienX == housingX+1 && alienY == housingY && alienUnit.getLeftConnector() != ShipCard.Connector.NONE && checkConnection(alienUnit.getLeftConnector(), housingUnit.getRightConnector())) {
            housingUnit.setAlienUnit(alienUnit);
        }
        else{
            throw new IllegalArgumentException("AlienUnit and CentralUnit are not directly connected");
        }

        if(alienUnit.getType() == AlienUnit.Type.BROWN){
            brownActiveUnit = alienUnit;
        }
        else{
            purpleActiveUnit = alienUnit;
        }
    }



    /**
     * Counts the number of brown aliens present in the ship
     *
     * @return The number of brown aliens
     * @throws IllegalStateException if {@code brownActiveUnit} is pointing at a purple AlienUnit
     */
    public int getBrownAliens() {
        if(brownActiveUnit == null){
            return 0;
        }
        if (brownActiveUnit.getType() == AlienUnit.Type.PURPLE) {
            throw new IllegalStateException("Purple AlienUnit are not supposed to be pointed by this variable");
        }
        if (!brownActiveUnit.isScrap() && brownActiveUnit.isPresent()) {
            return 1;
        }
        else{
            return 0;
        }
    }

    /**
     * Counts the number of purple aliens present in the ship
     *
     * @return The number of purple aliens
     * @throws IllegalStateException if {@code brownActiveUnit} is pointing at a brown AlienUnit
     */
    public int getPurpleAliens() {
        if(purpleActiveUnit == null){
            return 0;
        }
        if (purpleActiveUnit.getType() == AlienUnit.Type.BROWN) {
            throw new IllegalStateException("Brown AlienUnit are not supposed to be pointed by this variable");
        }
        if (!purpleActiveUnit.isScrap() && purpleActiveUnit.isPresent()) {
            return 1;
        }
        else{
            return 0;
        }
    }

    /**
     * Counts the total number of crew members in the ship
     *
     * @return The number of crew members
     */
    public int getMembers(){
        int members = 0;

        for(HousingUnit housingUnit : housingUnits.keySet()){
            if(!housingUnit.isScrap()){
                members += housingUnit.getNumMembers();
            }
        }

        return members;
    }

    /**
     * Removes a specified number of crew members from designated housing units
     *
     * @param housingUsage A map associating each housing unit with the number of members to remove
     * @throws IllegalArgumentException if the map is null, contains invalid entries, references scrap housing units, or has null values
     */
    public void killMembers(Map<HousingUnit, Integer> housingUsage) {
        if (housingUsage == null) {
            throw new IllegalArgumentException("Housing usage map cannot be null");
        }

        for (Map.Entry<HousingUnit, Integer> entry : housingUsage.entrySet()) {
            HousingUnit housingUnit = entry.getKey();
            Integer numMembers = entry.getValue();

            if (housingUnit == null) {
                throw new IllegalArgumentException("Housing unit module cannot be null");
            }
            if (numMembers == null) {
                throw new IllegalArgumentException("Number of members to remove cannot be null");
            }
            if (housingUnit.isScrap()) {
                throw new IllegalArgumentException("Scrap housing units cannot be used");
            }

            housingUnit.killMembers(numMembers);
        }
    }


    /**
     * Applies the effects of the epidemic AdventureCard to the ship
     */
    public void epidemic(){
        this.visitedInitialization();

        for (Map.Entry<HousingUnit, Point> entry : housingUnits.entrySet()) {
            HousingUnit housingUnit = entry.getKey();

            if (!housingUnit.isScrap()) {
                int x = (int) entry.getValue().getX();
                int y = (int) entry.getValue().getY();

                if(housingUnit.getTopConnector() != ShipCard.Connector.NONE){
                    Point topPoint = new Point(x, y-1);

                    for (Map.Entry<HousingUnit, Point> entry2 : housingUnits.entrySet()) {
                        if (entry2.getValue().equals(topPoint)) {
                            HousingUnit housingUnitTop = entry2.getKey();
                            if(!housingUnitTop.isScrap() && checkConnection(housingUnit.getTopConnector(), housingUnitTop.getBottomConnector())){
                                if(!housingUnitTop.isVisited()){
                                    housingUnitTop.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                            break;
                        }
                    }
                }

                if(housingUnit.getRightConnector() != ShipCard.Connector.NONE){
                    Point rightPoint = new Point(x+1, y);

                    for (Map.Entry<HousingUnit, Point> entry2 : housingUnits.entrySet()) {
                        if (entry2.getValue().equals(rightPoint)) {
                            HousingUnit housingUnitRight = entry2.getKey();
                            if(!housingUnitRight.isScrap() && checkConnection(housingUnit.getRightConnector(), housingUnitRight.getLeftConnector())){
                                if(!housingUnitRight.isVisited()){
                                    housingUnitRight.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                            break;
                        }
                    }
                }

                if(housingUnit.getBottomConnector() != ShipCard.Connector.NONE){
                    Point bottomPoint = new Point(x, y+1);

                    for (Map.Entry<HousingUnit, Point> entry2 : housingUnits.entrySet()) {
                        if (entry2.getValue().equals(bottomPoint)) {
                            HousingUnit housingUnitBottom = entry2.getKey();
                            if(!housingUnitBottom.isScrap() && checkConnection(housingUnit.getBottomConnector(), housingUnitBottom.getTopConnector())){
                                if(!housingUnitBottom.isVisited()){
                                    housingUnitBottom.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                            break;
                        }
                    }
                }

                if(housingUnit.getLeftConnector() != ShipCard.Connector.NONE){
                    Point leftPoint = new Point(x-1, y);

                    for (Map.Entry<HousingUnit, Point> entry2 : housingUnits.entrySet()) {
                        if (entry2.getValue().equals(leftPoint)) {
                            HousingUnit housingUnitLeft = entry2.getKey();
                            if(!housingUnitLeft.isScrap() && checkConnection(housingUnit.getLeftConnector(), housingUnitLeft.getRightConnector())){
                                if(!housingUnitLeft.isVisited()){
                                    housingUnitLeft.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                            break;
                        }
                    }
                }

                housingUnit.setVisited(true);
            }
        }
    }



    /**
     * Calculates the total number of available batteries
     *
     * @return The total count of available batteries
     */
    public int getTotalAvailableBatteries(){
        int availableBatteries = 0;

        for(Battery battery : batteries){
            if(!battery.isScrap()){
                availableBatteries += battery.getAvailableBatteries();
            }
        }

        return availableBatteries;
    }

    /**
     * Uses a specified number of batteries from designated battery modules
     *
     * @param batteryUsage A map associating each battery module with the number of batteries to use
     * @throws IllegalArgumentException if the map is null, contains invalid entries, references scrap batteries, or has null values
     */
    public void useBatteries(Map<Battery, Integer> batteryUsage) {
        if (batteryUsage == null) {
            throw new IllegalArgumentException("Battery usage map cannot be null");
        }

        for (Map.Entry<Battery, Integer> entry : batteryUsage.entrySet()) {
            Battery battery = entry.getKey();
            Integer numBatteries = entry.getValue();

            if (battery == null) {
                throw new IllegalArgumentException("Battery module cannot be null");
            }
            if (numBatteries == null) {
                throw new IllegalArgumentException("Number of batteries to use cannot be null");
            }
            if (battery.isScrap()) {
                throw new IllegalArgumentException("Scrap batteries cannot be used");
            }

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

        for (Storage storage : storages) {
            if(!storage.isScrap()){
                totalMaterialsValue += storage.getMaterialsValue();
            }
        }

        return totalMaterialsValue;
    }

    /**
     * Adds or replaces materials in a list of storage units based on the provided new and old material lists
     * <p>
     * This method ensures that the provided map is non-null and contains valid entries.
     * Each storage unit is mapped to a pair of lists: one for new materials to add and one for old materials to replace
     *
     * @param storageMaterials A map associating each {@link Storage} unit with a pair of lists:
     * <ul>
     *     <li>The first list contains the new {@link Material} objects to be added</li>
     *     <li>The second list contains the old {@link Material} objects to be replaced</li>
     * </ul>
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>the map is {@code null}</li>
     *     <li>it contains {@code null} keys or values</li>
     *     <li>any inner material list is {@code null}</li>
     *     <li>or the sizes of the new and old material lists do not match for a given storage unit</li>
     * </ul>
     */
    public void addMaterials(Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials) {
        if (storageMaterials == null) {
            throw new IllegalArgumentException("Storage materials map cannot be null");
        }

        for (Map.Entry<Storage, SimpleEntry<List<Material>, List<Material>>> entry : storageMaterials.entrySet()) {
            Storage storage = entry.getKey();
            SimpleEntry<List<Material>, List<Material>> materialsPair = entry.getValue();

            if (storage == null) {
                throw new IllegalArgumentException("Storage unit cannot be null");
            }
            if (materialsPair == null || materialsPair.getKey() == null || materialsPair.getValue() == null) {
                throw new IllegalArgumentException("Material lists cannot be null for storage: " + storage);
            }

            List<Material> newMaterials = materialsPair.getKey();
            List<Material> oldMaterials = materialsPair.getValue();

            if (newMaterials.size() != oldMaterials.size()) {
                throw new IllegalArgumentException("New and old materials lists do not match in size for storage: " + storage);
            }

            for (int i = 0; i < newMaterials.size(); i++) {
                if (oldMaterials.get(i) == null) {
                    storage.addMaterial(newMaterials.get(i));
                } else {
                    storage.replaceMaterial(newMaterials.get(i), oldMaterials.get(i));
                }
            }
        }
    }


    /**
     * Removes materials from storage, prioritizing the most valuable
     *
     * @param numMaterials The number of materials to remove
     * @return The number of materials that could not be removed due to shortages
     */
    public int removeMaterials(int numMaterials) {
        int mostValuableMaterial;
        Storage targetStorage;
        Material targetMaterial;
        Material materialToRemove;

        for (int k = 0; k < numMaterials; k++) {
            mostValuableMaterial = 0;
            targetStorage = null;
            targetMaterial = null;

            for (Storage storage : storages) {
                if (!storage.isScrap()) {
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

        for(Engine engine : engines.keySet()){
            if(!engine.isScrap() && engine.getType() == Engine.Type.DOUBLE){
                doubleEngines++;
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
        for(Engine engine : engines.keySet()){
            if(!engine.isScrap() && engine.getType() == Engine.Type.SINGLE){
                enginePower++;
            }
        }

        enginePower += 2*numBatteries;
        if(enginePower > 0){
            enginePower += 2*getBrownAliens();
        }

        return enginePower;
    }



    /**
     * Counts the number of double cannons in the ship
     *
     * @return The number of double cannons
     */
    public int getDoubleCannonsNumber(){
        int doubleCannons = 0;

        for (Cannon cannon : cannons.keySet()) {
            if (!cannon.isScrap() && cannon.getType() == Cannon.Type.DOUBLE) {
                doubleCannons++;
            }
        }

        return doubleCannons;
    }

    /**
     * Computes the total cannon power based on the selected double cannons and the available batteries
     *
     * @param doubleCannons The list of double cannons that the user wants to activate. All cannons in this list must be of type {@code Cannon.Type.DOUBLE} and not destroyed
     * @return The calculated total cannon power
     * @throws IllegalArgumentException if the list contains non-double or destroyed cannons, or if the number of selected cannons exceeds the available batteries
     */
    public double getCannonsPower (List<Cannon> doubleCannons) {
        double cannonPower = 0;

        if(doubleCannons != null) {
            for (Cannon cannon : doubleCannons) {
                if (cannon.getType() != Cannon.Type.DOUBLE) {
                    throw new IllegalArgumentException("Cannot activate single cannons with batteries");
                }
                if (cannon.isScrap()) {
                    throw new IllegalArgumentException("Cannot activate a cannon that was previously destroyed");
                }
                if (cannon.getType() == Cannon.Type.SINGLE){
                    throw new IllegalArgumentException("Cannot use batteries on a single cannon");
                }
            }
            if(doubleCannons.size() > getTotalAvailableBatteries()){
                throw new IllegalArgumentException("numBatteries cannot be greater than the number of available batteries");
            }
            if(doubleCannons.size() > getDoubleCannonsNumber()){
                throw new IllegalArgumentException("Double cannons number cannot be greater than the number of double cannons on this ship");
            }

            for(Cannon cannon : doubleCannons){
                if(cannon.getOrientation() == ShipCard.Orientation.DEG_0){
                    cannonPower += 2;
                }
                else{
                    cannonPower++;
                }
            }
        }

        for (Cannon cannon : cannons.keySet()) {
            if (!cannon.isScrap() && cannon.getType() == Cannon.Type.SINGLE) {
                if(cannon.getOrientation() == ShipCard.Orientation.DEG_0){
                    cannonPower++;
                }
                else{
                    cannonPower += 0.5;
                }
            }
        }

        if(cannonPower > 0){
            cannonPower += 2*getPurpleAliens();
        }

        return cannonPower;
    }



    /**
     * Determines whether this ship is being protected from the given direction by any functional shield
     *
     * @param direction The direction from which an attack is coming
     * @return {@code true} if there is at least one active shield protecting from the given direction, {@code false} otherwise
     */
    public boolean isBeingProtected(Hit.Direction direction) {
        for(Shield shield : shields){
            if(!shield.isScrap() && shield.isProtecting(direction)){
                return true;
            }
        }

        return false;
    }

    /**
     * Determines which cannons can be used to destroy a big meteor coming from the given direction and coordinate
     *
     * @param direction The direction from which a big meteor is coming
     * @param coordinate The coordinate from which a big meteor is coming
     * @return A list of {@code Cannon} objects that can be used to destroy the meteor. If no cannons are available, returns an empty list
     */
    public List<Cannon> canDestroy(Hit.Direction direction, int coordinate) {
        List<Cannon> availableCannons = new ArrayList<>();

        if (direction == Hit.Direction.LEFT) {
            for (int offset = -1; offset < 2; offset++) {
                for (int j = 0; j < components[0].length; j++) {
                    Point point = new Point(j - adaptX(0), coordinate + offset);

                    for (Map.Entry<Cannon, Point> entry : cannons.entrySet()) {
                        if (entry.getValue().equals(point)) {
                            Cannon cannon = entry.getKey();
                            if (!cannon.isScrap() && cannon.getOrientation() == ShipCard.Orientation.DEG_270) {
                                availableCannons.add(cannon);
                            }
                        }
                    }
                }
            }
        }
        else if (direction == Hit.Direction.RIGHT) {

            for (int offset = -1; offset < 2; offset++) {
                for (int j = 0; j < components[0].length; j++) {
                    Point point = new Point(components[0].length - j - adaptX(0), coordinate + offset);

                    for (Map.Entry<Cannon, Point> entry : cannons.entrySet()) {
                        if (entry.getValue().equals(point)) {
                            Cannon cannon = entry.getKey();
                            if (!cannon.isScrap() && cannon.getOrientation() == ShipCard.Orientation.DEG_90) {
                                availableCannons.add(cannon);
                            }
                        }
                    }
                }
            }
        }
        else if (direction == Hit.Direction.TOP) {

            for (int i = 0; i < components.length; i++) {
                Point point = new Point(coordinate, i - adaptY(0));

                for (Map.Entry<Cannon, Point> entry : cannons.entrySet()) {
                    if (entry.getValue().equals(point)) {
                        Cannon cannon = entry.getKey();
                        if (!cannon.isScrap() && cannon.getOrientation() == ShipCard.Orientation.DEG_0) {
                            availableCannons.add(cannon);
                        }
                    }
                }
            }
        }
        else if (direction == Hit.Direction.BOTTOM) {

            for (int offset = -1; offset < 2; offset++) {
                for (int i = 0; i < components.length; i++) {
                    Point point = new Point(coordinate + offset, components.length - i - adaptY(0));

                    for (Map.Entry<Cannon, Point> entry : cannons.entrySet()) {
                        if (entry.getValue().equals(point)) {
                            Cannon cannon = entry.getKey();
                            if (!cannon.isScrap() && cannon.getOrientation() == ShipCard.Orientation.DEG_180) {
                                availableCannons.add(cannon);
                            }
                        }
                    }
                }
            }
        }

        return availableCannons;
    }

    /**
     * Determines whether this ship has an exposed connector in the given direction and coordinate
     *
     * @param direction The direction from which a little meteor is coming
     * @param coordinate The coordinate from which a little meteor is coming
     * @return {@code true} if the ship has an exposed connector in the given direction and coordinate, {@code false} otherwise
     */
    public boolean hasAnExposedConnector(Hit.Direction direction, int coordinate) {
        if (direction == Hit.Direction.LEFT) {
            for (int i = 0; i < components[0].length; i++) {
                try{
                    ShipCard shipCard = getShipCard(i - adaptX(0), coordinate);
                    if (shipCard != null && !shipCard.isScrap()) {
                        if(shipCard.getLeftConnector() != ShipCard.Connector.NONE) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.RIGHT) {
            for (int i = 0; i < components[0].length; i++) {
                try{
                    ShipCard shipCard = getShipCard(components[0].length - i - adaptX(0), coordinate);
                    if (shipCard != null && !shipCard.isScrap()) {
                        if(shipCard.getRightConnector() != ShipCard.Connector.NONE) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.TOP) {
            for (int i = 0; i < components.length; i++) {
                try{
                    ShipCard shipCard = getShipCard(coordinate, i - adaptY(0));
                    if (shipCard != null && !shipCard.isScrap()) {
                        if(shipCard.getTopConnector() != ShipCard.Connector.NONE) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.BOTTOM) {
            for (int i = 0; i < components.length; i++) {
                try{
                    ShipCard shipCard = getShipCard(coordinate, components.length - i - adaptY(0));
                    if (shipCard != null && !shipCard.isScrap()) {
                        if(shipCard.getBottomConnector() != ShipCard.Connector.NONE) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                catch(Exception _){

                }
            }
        }

        return false;    /* didn't find any ShipCard on the given direction and coordinate => ShipBoard is not affected by the little meteor */
    }

    /**
     * Determines which component is going to be destroyed by a hit
     *
     * @param direction The direction from which a hit is coming
     * @param coordinate The coordinate from which a hit is coming
     * @return {@code true} if a ShipCard was destroyed by the hit, {@code false} otherwise
     */
    public boolean destroyHitComponent(Hit.Direction direction, int coordinate) {
        if (direction == Hit.Direction.LEFT) {
            for (int i = 0; i < components[0].length; i++) {
                try{
                    ShipCard shipCard = getShipCard(i - adaptX(0), coordinate);
                    if (shipCard != null && !shipCard.isScrap()) {
                        shipCard.destroy();
                        return true;
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.RIGHT) {
            for (int i = 0; i < components[0].length; i++) {
                try{
                    ShipCard shipCard = getShipCard(components[0].length - i - adaptX(0), coordinate);
                    if (shipCard != null && !shipCard.isScrap()) {
                        shipCard.destroy();
                        return true;
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.TOP) {
            for (int i = 0; i < components.length; i++) {
                try{
                    ShipCard shipCard = getShipCard(coordinate, i - adaptY(0));
                    if (shipCard != null && !shipCard.isScrap()) {
                        shipCard.destroy();
                        return true;
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.BOTTOM) {
            for (int i = 0; i < components.length; i++) {
                try{
                    ShipCard shipCard = getShipCard(coordinate, components.length - i - adaptY(0));
                    if (shipCard != null && !shipCard.isScrap()) {
                        shipCard.destroy();
                        return true;
                    }
                }
                catch(Exception _){

                }
            }
        }

        return false;    /* didn't find any ShipCard on the given direction and coordinate => ShipBoard is not affected by the hit */
    }
}

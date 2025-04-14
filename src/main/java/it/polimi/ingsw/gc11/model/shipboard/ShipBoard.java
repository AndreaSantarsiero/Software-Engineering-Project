package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.*;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Represents a ship's board where ship components can be placed and managed
 * <p>
 * The board maintains information about ship cards, reserved components, and the last modified position
 */
public abstract class ShipBoard implements ShipCardVisitor {

    private final ShipCard[][] components;
    private final List<ShipCard> reservedComponents;

    private final List<AlienUnit> alienUnits;
    private final List<Battery> batteries;
    private final List<Cannon> cannons;
    private final List<Engine> engines;
    private final List<HousingUnit> housingUnits;
    private final List<Shield> shields;
    private final List<Storage> storages;

    private boolean addMode;
    private int lastModifiedX;
    private int lastModifiedY;
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
        this.alienUnits = new ArrayList<>();
        this.batteries = new ArrayList<>();
        this.cannons = new ArrayList<>();
        this.engines = new ArrayList<>();
        this.housingUnits = new ArrayList<>();
        this.shields = new ArrayList<>();
        this.storages = new ArrayList<>();
        this.lastModifiedX = -1;
        this.lastModifiedY = -1;
        this.brownActiveUnit = null;
        this.purpleActiveUnit = null;
    }



    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link AlienUnit} instance to the alienUnits list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(AlienUnit alienUnit) {
        if(addMode) {
            if(!alienUnits.contains(alienUnit)) {
                alienUnits.add(alienUnit);
            }
        }
        else {
            alienUnits.remove(alienUnit);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link Battery} instance to the batteries list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(Battery battery) {
        if(addMode) {
            if(!batteries.contains(battery)) {
                batteries.add(battery);
            }
        }
        else {
            batteries.remove(battery);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link Cannon} instance to the cannons list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(Cannon cannon) {
        if(addMode) {
            if(!cannons.contains(cannon)) {
                cannons.add(cannon);
            }
        }
        else {
            cannons.remove(cannon);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link Engine} instance to the engines list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(Engine engine) {
        if(addMode) {
            if(!engines.contains(engine)) {
                engines.add(engine);
            }
        }
        else {
            engines.remove(engine);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link HousingUnit} instance to the housingUnits list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(HousingUnit housingUnit) {
        if(addMode) {
            if(!housingUnits.contains(housingUnit)) {
                housingUnits.add(housingUnit);
            }
        }
        else {
            housingUnits.remove(housingUnit);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link Shield} instance to the shields list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(Shield shield) {
        if(addMode) {
            if(!shields.contains(shield)) {
                shields.add(shield);
            }
        }
        else {
            shields.remove(shield);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>addMode</code> is <code>true</code>, adds the {@link Storage} instance to the storages list if it is not already present.
     * Otherwise, removes it from the list if present
     */
    @Override
    public void visit(Storage storage) {
        if(addMode) {
            if(!storages.contains(storage)) {
                storages.add(storage);
            }
        }
        else {
            storages.remove(storage);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * No operation is performed on {@link StructuralModule}
     */
    @Override
    public void visit(StructuralModule structuralModule) {

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
     * Determines whether the given coordinates are valid within the ship's bounds based on the specific type of ShipBoard
     * <p>
     * This method delegates the validation to the corresponding subclass, ensuring that each type of ShipBoard applies its own coordinate validation logic
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return True if the coordinates are valid, false otherwise
     * @throws IllegalStateException If the coordinates are out of the board's bounds
     */
    public abstract boolean validateCoordinates(int x, int y);

    /**
     * Validates whether the given coordinates are within the allowed bounds of the ship.
     * This method calls {@code validateCoordinates} to verify the coordinates and throws an exception if they are out of bounds
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @throws IllegalArgumentException If the coordinates are out of the ship's bounds
     */
    public void checkCoordinates(int x, int y) {
        if (!validateCoordinates(x, y)) {
            throw new IllegalArgumentException("Coordinates out of the ship's bounds");
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

        x = adaptX(x);
        y = adaptY(y);
        checkCoordinates(x, y);
        components[y][x] = shipCard;
        lastModifiedX = x;
        lastModifiedY = y;
        addMode = true;
        shipCard.accept(this);
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
        x = adaptX(x);
        y = adaptY(y);
        checkCoordinates(x, y);
        if (components[y][x] == null) {
            throw new IllegalArgumentException("Ship card already null");
        }
        if (x == lastModifiedX && y == lastModifiedY) {
            addMode = false;
            components[y][x].accept(this);
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
        x = adaptX(x);
        y = adaptY(y);
        checkCoordinates(x, y);
        return components[y][x];
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
            x = adaptX(x);
            y = adaptY(y);
            checkCoordinates(x, y);
            reservedComponents.remove(shipCard);
            components[y][x] = shipCard;
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
                    checkCoordinates(j, i);
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
                    checkCoordinates(j, i);
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
                            checkCoordinates(j+1, i);
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
                            checkCoordinates(j-1, i);
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
                            checkCoordinates(j, i-1);
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
                            checkCoordinates(j, i+1);
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
                    checkCoordinates(j, i);
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
                    checkCoordinates(x, y-1);
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
                    checkCoordinates(x+1, y);
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
                    checkCoordinates(x, y+1);
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
                    checkCoordinates(x-1, y);
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

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j] != null) {
                    if (components[i][j] instanceof Engine engine && !components[i][j].isScrap()) {
                        if (engine.getOrientation() != ShipCard.Orientation.DEG_0){
                            engine.setIllegal(true);
                            status = false;
                        }
                        try {
                            checkCoordinates(j, i+1);
                            if (components[i+1][j] != null) {
                                engine.setIllegal(true);
                                components[i+1][j].setIllegal(true);
                                status = false;
                            }
                        } catch (Exception _) {

                        }
                    }

                    if (components[i][j] instanceof Cannon cannon && !components[i][j].isScrap()) {
                        if (cannon.getOrientation() == ShipCard.Orientation.DEG_0) {
                            try {
                                checkCoordinates(j, i-1);
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
                                checkCoordinates(j+1, i);
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
                                checkCoordinates(j, i+1);
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
                                checkCoordinates(j-1, i);
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
     * @param alienX the x-coordinate of the AlienUnit
     * @param alienY the y-coordinate of the AlienUnit
     * @param housingX the x-coordinate of the HousingUnit
     * @param housingY the y-coordinate of the HousingUnit
     * @throws IllegalArgumentException if not all conditions are met
     */
    public void connectAlienUnit(int alienX, int alienY, int housingX, int housingY) {
        ShipCard shipCard1 = this.getShipCard(alienX, alienY);
        ShipCard shipCard2 = this.getShipCard(housingX, housingY);

        if (!(shipCard1 instanceof AlienUnit alienUnit)){
            throw new IllegalArgumentException("AlienUnit coordinates do not point to an AlienUnit");
        }
        if (!(shipCard2 instanceof HousingUnit housingUnit)){
            throw new IllegalArgumentException("HousingUnit coordinates do not point to a HousingUnit");
        }
        if(alienUnit.getType() == AlienUnit.Type.BROWN && brownActiveUnit != null) {
            throw new IllegalArgumentException("This ship has already activate a brown AlienUnit");
        }
        if(alienUnit.getType() == AlienUnit.Type.PURPLE && purpleActiveUnit != null) {
            throw new IllegalArgumentException("This ship has already activate a purple AlienUnit");
        }
        if(housingUnit.isCentral()){
            throw new IllegalArgumentException("Cannot connect an AlienUnit to a central HousingUnit");
        }

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

        for(HousingUnit housingUnit : housingUnits){
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

        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if(components[i][j] instanceof HousingUnit housingUnit && !housingUnit.isScrap()) {
                    if(housingUnit.getTopConnector() != ShipCard.Connector.NONE){
                        if(components[i-1][j] instanceof HousingUnit housingUnitTop && !housingUnitTop.isScrap()) {
                            if(checkConnection(housingUnit.getTopConnector(), housingUnitTop.getBottomConnector())){
                                if(!housingUnitTop.isVisited()){
                                    housingUnitTop.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                        }
                    }

                    if(housingUnit.getRightConnector() != ShipCard.Connector.NONE){
                        if(components[i][j+1] instanceof HousingUnit housingUnitRight && !housingUnitRight.isScrap()) {
                            if(checkConnection(housingUnit.getRightConnector(), housingUnitRight.getLeftConnector())){
                                if(!housingUnitRight.isVisited()){
                                    housingUnitRight.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                        }
                    }

                    if(housingUnit.getBottomConnector() != ShipCard.Connector.NONE){
                        if(components[i+1][j] instanceof HousingUnit housingUnitBottom && !housingUnitBottom.isScrap()) {
                            if(checkConnection(housingUnit.getBottomConnector(), housingUnitBottom.getTopConnector())){
                                if(!housingUnitBottom.isVisited()){
                                    housingUnitBottom.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                        }
                    }

                    if(housingUnit.getLeftConnector() != ShipCard.Connector.NONE){
                        if(components[i][j-1] instanceof HousingUnit housingUnitLeft && !housingUnitLeft.isScrap()) {
                            if(checkConnection(housingUnit.getLeftConnector(), housingUnitLeft.getRightConnector())){
                                if(!housingUnitLeft.isVisited()){
                                    housingUnitLeft.epidemic();
                                }
                                if(!housingUnit.isVisited()){
                                    housingUnit.epidemic();
                                }
                            }
                        }
                    }

                    housingUnit.setVisited(true);
                }
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

        for(Engine engine : engines){
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
        for(Engine engine : engines){
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

        for (Cannon cannon : cannons) {
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
            }
            if(doubleCannons.size() > getTotalAvailableBatteries()){
                throw new IllegalArgumentException("numBatteries cannot be greater than the number of available batteries");
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

        for (Cannon cannon : cannons) {
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
            for (int j = -1; j < 2; j++) {
                for (int i = 0; i < components[0].length; i++) {
                    try{
                        ShipCard shipCard = this.getShipCard(i - adaptX(0), coordinate + j);
                        if(shipCard != null){
                            if (shipCard instanceof Cannon && !shipCard.isScrap()) {
                                if(shipCard.getOrientation() == ShipCard.Orientation.DEG_270) {
                                    availableCannons.add((Cannon) shipCard);
                                }
                            }
                        }
                    }
                    catch(Exception _){

                    }
                }
            }
        }
        else if (direction == Hit.Direction.RIGHT) {

            for (int j = -1; j < 2; j++) {
                for (int i = 0; i < components[0].length; i++) {
                    try{
                        ShipCard shipCard = this.getShipCard(components[0].length - i - adaptX(0), coordinate + j);
                        if(shipCard != null){
                            if (shipCard instanceof Cannon && !shipCard.isScrap()) {
                                if(shipCard.getOrientation() == ShipCard.Orientation.DEG_90) {
                                    availableCannons.add((Cannon) shipCard);
                                }
                            }
                        }
                    }
                    catch(Exception _){

                    }
                }
            }
        }
        else if (direction == Hit.Direction.TOP) {
            for (int i = 0; i < components.length; i++) {
                try{
                    ShipCard shipCard = this.getShipCard(coordinate, i - adaptY(0));
                    if(shipCard != null){
                        if (shipCard instanceof Cannon && !shipCard.isScrap()) {
                            if(shipCard.getOrientation() == ShipCard.Orientation.DEG_0) {
                                availableCannons.add((Cannon) shipCard);
                            }
                        }
                    }
                }
                catch(Exception _){

                }
            }
        }
        else if (direction == Hit.Direction.BOTTOM) {

            for (int j = -1; j < 2; j++) {
                for (int i = 0; i < components.length; i++) {
                    try{
                        ShipCard shipCard = this.getShipCard(coordinate + j, components.length - i - adaptY(0));
                        if(shipCard != null){
                            if (shipCard instanceof Cannon && !shipCard.isScrap()) {
                                if(shipCard.getOrientation() == ShipCard.Orientation.DEG_180) {
                                    availableCannons.add((Cannon) shipCard);
                                }
                            }
                        }
                    }
                    catch(Exception _){

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

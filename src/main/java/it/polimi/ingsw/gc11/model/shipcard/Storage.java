package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



/**
 * Represents a Storage, a specialized type of ShipCard
 * <p>
 * Different storage types allow different amounts of material and specific material types
 */
public class Storage extends ShipCard {

    /**
     * Defines the possible types of storage
     */
    public enum Type {
        DOUBLE_BLUE, TRIPLE_BLUE, SINGLE_RED, DOUBLE_RED;

        public int getCapacity() {
            return switch (this) {
                case SINGLE_RED -> 1;
                case DOUBLE_BLUE, DOUBLE_RED -> 2;
                case TRIPLE_BLUE -> 3;
            };
        }
    }


    private final Type type;
    private final List<Material> materials;


    /**
     * Constructs a Storage with  specified connectors and type
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param type The type of the storage (defines its capacity and allowed materials)
     */
    public Storage(String id, Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(id, topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
        this.materials = new ArrayList<>();
    }



    /**
     * Gets the type of this storage unit
     *
     * @return The type of the storage
     */
    public Type getType() {
        return type;
    }


    /**
     * Returns the list of materials currently stored
     *
     * @return The list of materials currently stored
     */
    public List<Material> getMaterials() {
        return materials;
    }


    public void checkChooseMaterialsRestrictions(List<Material> newMaterials, List<Material> oldMaterials) {
        int blueCount = 0, greenCount = 0, yellowCount = 0, redCount = 0, maximumCount = 0;

        if (newMaterials.size() != oldMaterials.size()) {
            throw new IllegalArgumentException("New material list size does not match old material list size");
        }

        if (type == Type.DOUBLE_BLUE || type == Type.TRIPLE_BLUE) {
            for (int i = 0; i < newMaterials.size(); i++) {
                Material newMaterial = newMaterials.get(i);
                Material oldMaterial = oldMaterials.get(i);

                if((newMaterial != null && newMaterial.getType() == Material.Type.RED) || (oldMaterial != null && oldMaterial.getType() == Material.Type.RED)) {
                    throw new IllegalArgumentException("Cannot add or remove red materials in a blue storage");
                }
            }
        }

        switch (type) {
            case SINGLE_RED -> maximumCount = 1;
            case DOUBLE_RED, DOUBLE_BLUE -> maximumCount = 2;
            case TRIPLE_BLUE -> maximumCount = 3;
        }

        for (Material material : materials) {
            if(material != null){
                switch (material.getType()) {
                    case BLUE -> blueCount++;
                    case GREEN -> greenCount++;
                    case YELLOW -> yellowCount++;
                    case RED -> redCount++;
                }
            }
        }

        for (int i = 0; i < newMaterials.size(); i++) {
            Material newMaterial = newMaterials.get(i);
            Material oldMaterial = oldMaterials.get(i);

            if(newMaterial != null){
                switch (newMaterial.getType()) {
                    case BLUE -> blueCount++;
                    case GREEN -> greenCount++;
                    case YELLOW -> yellowCount++;
                    case RED -> redCount++;
                }
            }

            if(oldMaterial != null){
                switch (oldMaterial.getType()) {
                    case BLUE -> blueCount--;
                    case GREEN -> greenCount--;
                    case YELLOW -> yellowCount--;
                    case RED -> redCount--;
                }
            }

            if(blueCount < 0 || greenCount < 0 || yellowCount < 0 || redCount < 0){
                throw new IllegalArgumentException("Trying to remove a material that is not present on this storage");
            }
            if((blueCount + greenCount + yellowCount + redCount) > maximumCount){
                throw new IllegalArgumentException("Too many materials on this storage at the same time");
            }
        }
    }


    /**
     * Adds a new material to the storage.
     * Throws an exception if the storage is full or the material type is incompatible with the storage type
     *
     * @param newMaterial The material to add
     * @throws IllegalArgumentException If the material is null or the material type is incompatible
     * @throws IllegalStateException If the storage is full and cannot accept more materials
     */
    public void addMaterial(Material newMaterial) {
        if (newMaterial == null) {
            throw new IllegalArgumentException("Material is null");
        }
        if (type.equals(Type.SINGLE_RED) && !materials.isEmpty()) {
            throw new IllegalStateException("Storage is already full. Use replaceMaterial method");
        }
        if ((type.equals(Type.DOUBLE_RED) || type.equals(Type.DOUBLE_BLUE)) && materials.size() > 1) {
            throw new IllegalStateException("Storage is already full. Use replaceMaterial method");
        }
        if (type.equals(Type.TRIPLE_BLUE) && materials.size() > 2) {
            throw new IllegalStateException("Storage is already full. Use replaceMaterial method");
        }
        if (newMaterial.getType() == Material.Type.RED) {
            if (type.equals(Type.DOUBLE_RED) || type.equals(Type.SINGLE_RED)) {
                materials.add(newMaterial);
            } else {
                throw new IllegalArgumentException("Can't add special material to a non-special storage");
            }
        } else {
            materials.add(newMaterial);
        }
    }


    /**
     * Removes a specified material from the storage.
     * Throws an exception if the material is not found or the storage is empty
     *
     * @param material The material to remove
     * @throws IllegalArgumentException If the material is null or not found in the storage
     * @throws IllegalStateException If the storage is empty
     */
    public void removeMaterial(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material is null");
        }
        if (materials.isEmpty()) {
            throw new IllegalStateException("Storage already empty");
        }
        if (materials.contains(material)) {
            materials.remove(material);
        } else {
            throw new IllegalArgumentException("Material not found in this storage");
        }
    }


    /**
     * Replaces an old material with a new one in the storage.
     * Throws an exception if the storage is empty or the old material is not found
     *
     * @param newMaterial The material to replace the old one with
     * @param oldMaterial The material to remove and replace
     * @throws IllegalArgumentException If any of the materials is null or the old material is not found
     * @throws IllegalStateException If the storage is empty
     */
    public void replaceMaterial(Material newMaterial, Material oldMaterial) {
        if (newMaterial == null || oldMaterial == null) {
            throw new IllegalArgumentException("Material is null");
        }
        if (materials.isEmpty()) {
            throw new IllegalStateException("Storage empty, nothing to replace. Use addMaterial method");
        }
        if (materials.contains(oldMaterial)) {
            materials.remove(oldMaterial);
            materials.add(newMaterial);
        } else {
            throw new IllegalArgumentException("Old material not found in this storage");
        }
    }


    /**
     * Calculates and returns the total value of the materials in the storage
     * <p>
     * The value is determined by the type of material:
     * <ul>
     *     <li>RED = 4 coins</li>
     *     <li>YELLOW = 3 coins</li>
     *     <li>GREEN = 2 coins</li>
     *     <li>BLUE = 1 coin</li>
     * </ul>
     *
     * @return The total value of the materials in the storage
     */
    public int getMaterialsValue() {
        if (materials.isEmpty()) {
            return 0;
        }

        int materialsValue = 0;

        for (Material material : materials) {
            materialsValue += material.getValue();
        }

        return materialsValue;
    }


    /**
     * Retrieves the most valued material from the storage.
     * If no materials are found, an {@code IllegalArgumentException} is thrown
     *
     * @return the most valued {@code Material}
     * @throws IllegalArgumentException if no material is available in the storage
     */
    public Material getMostValuedMaterial() {
        for (Material material : materials) {
            if (material.getType() == Material.Type.RED) {
                return material;
            }
        }
        for (Material material : materials) {
            if (material.getType() == Material.Type.YELLOW) {
                return material;
            }
        }
        for (Material material : materials) {
            if (material.getType() == Material.Type.GREEN) {
                return material;
            }
        }
        for (Material material : materials) {
            if (material.getType() == Material.Type.BLUE) {
                return material;
            }
        }

        throw new IllegalArgumentException("Storage empty, no material found");
    }



    /**
     * Compares this Storage to another object for equality
     * <p>
     * Two Storages are considered equal if they are of the same class, pass the equality check of the superclass, and have the same type
     *
     * @param obj The object to compare with this Storage
     * @return {@code true} if the given object is a Storage with the same attributes, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Storage other = (Storage) obj;
        return super.equals(obj) && this.type == other.getType() && Objects.equals(this.materials, other.materials);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     * <p>
     * The hash includes the base {@code ShipCard} attributes, the storage type, and the current list of materials.
     *
     * @return the hash code of this storage
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, materials);
    }


    /**
     * Places this storage module on the specified {@link ShipBoard} at the given coordinates.
     * <p>
     * Registers the storage card into the board’s internal layout and tracking systems.
     *
     * @param shipBoard the ship board where this storage is placed
     * @param x the x-coordinate on the ship board grid
     * @param y the y-coordinate on the ship board grid
     */
    @Override
    public void place(ShipBoard shipBoard, int x, int y){
        shipBoard.addToList(this, x, y);
    }

    /**
     * Removes this storage module from the specified {@link ShipBoard}.
     * <p>
     * Deregisters the component from the board, effectively freeing its position.
     *
     * @param shipBoard the ship board from which this storage is removed
     */
    @Override
    public void unPlace(ShipBoard shipBoard){
        shipBoard.removeFromList(this);
    }

    /**
     * Renders this storage card in the CLI interface using the provided renderer.
     *
     * @param shipCardCLI the command-line interface drawing utility
     * @param i the row index for layout alignment
     * @param selected {@code true} if the card is currently selected in the UI; {@code false} otherwise
     */
    @Override
    public void print(ShipCardCLI shipCardCLI, int i, boolean selected){
        shipCardCLI.draw(this, i, selected);
    }
}

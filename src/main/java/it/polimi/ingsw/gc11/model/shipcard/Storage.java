package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Material;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a Storage, a specialized type of ShipCard
 * Different storage types allow different amounts of material and specific material types
 */
public class Storage extends ShipCard {

    /**
     * Defines the possible types of storage
     */
    public enum Type {
        DOUBLE_BLUE, TRIPLE_BLUE, SINGLE_RED, DOUBLE_RED;
    }


    private final Type type;
    private List<Material> materials;


    /**
     * Constructs a Storage with  specified connectors and type
     *
     * @param topConnector The connector on the top side
     * @param rightConnector The connector on the right side
     * @param bottomConnector The connector on the bottom side
     * @param leftConnector The connector on the left side
     * @param type The type of the storage (defines its capacity and allowed materials)
     */
    public Storage(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
        this.materials = new ArrayList<Material>();
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
     * Adds a new material to the storage
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
     * Removes a specified material from the storage
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
     * Replaces an old material with a new one in the storage
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
     * The value is determined by the type of material:
     * - RED = 4 coins
     * - YELLOW = 3 coins
     * - GREEN = 2 coins
     * - BLUE = 1 coin
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
     * Retrieves the most valued material from the storage
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
}

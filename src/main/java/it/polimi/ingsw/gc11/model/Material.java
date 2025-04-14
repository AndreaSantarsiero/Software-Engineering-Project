package it.polimi.ingsw.gc11.model;



/**
 * Represents a material in the game. Each material has a specific type
 * that can be BLUE, GREEN, YELLOW, or RED
 */
public class Material {

    /**
     * Defines the possible types of materials
     */
    public enum Type {
        BLUE, GREEN, YELLOW, RED
    }


    private final Type type;


    /**
     * Constructs a new Material with the specified type
     *
     * @param type The type of the material to be created
     */
    public Material(Type type) {
        this.type = type;
    }


    /**
     * Gets the type of this material
     *
     * @return The type of the material
     */
    public Type getType() {
        return type;
    }


    /**
     * Gets the value of this material
     *
     * @return The value of the material
     */
    public int getValue() {
        return switch (type) {
            case BLUE -> 1;
            case GREEN -> 2;
            case YELLOW -> 3;
            case RED -> 4;
            default -> throw new IllegalArgumentException("Unknown material type: " + type);
        };
    }


    /**
     * Compares this Material to another object for equality
     * <p>
     * Two Materials are considered equal if they have the same type
     *
     * @param obj The object to compare with this Material
     * @return {@code true} if the given object is a Material with the same type, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Material material = (Material) obj;
        return this.type.equals(material.getType());
    }
}

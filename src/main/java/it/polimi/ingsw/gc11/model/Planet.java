package it.polimi.ingsw.gc11.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Represents a planet in the game, which can contain materials of various types
 * and can be visited by a player.
 *
 * <p>Each planet maintains a list of {@link Material} objects initialized during construction,
 * and can be marked as "visited" by a {@link Player}. The class implements {@link Serializable}
 * to support serialization of its state.</p>
 */
public class Planet implements Serializable {

    private boolean visited;
    private final ArrayList<Material> materials;
    private Player player;

    /**
     * Constructs a new planet containing the specified number of materials
     * of each color (blue, green, yellow, red).
     *
     * @param numBlue   the number of blue materials to add
     * @param numGreen  the number of green materials to add
     * @param numYellow the number of yellow materials to add
     * @param numRed    the number of red materials to add
     */
    public Planet(int numBlue, int numGreen, int numYellow, int numRed) {
        materials = new ArrayList<>();
        visited = false;
        player = null;
        //add materials
        for (int i = 0; i < numBlue; i++)
            materials.add(new Material(Material.Type.BLUE));
        for (int i = 0; i < numGreen; i++)
            materials.add(new Material(Material.Type.GREEN));
        for (int i = 0; i < numYellow; i++)
            materials.add(new Material(Material.Type.YELLOW));
        for (int i = 0; i < numRed; i++)
            materials.add(new Material(Material.Type.RED));
    }

    /**
     * Checks whether the planet has already been visited by a player.
     *
     * @return {@code true} if the planet has been visited, {@code false} otherwise
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Marks the planet as visited and records the player who visited it.
     *
     * @param player the player who visits the planet
     */
    public void setVisited(Player player) {
        this.visited = true;
        this.player = player;
    }

    /**
     * Returns the player who has visited the planet.
     *
     * @return the associated player, or {@code null} if not yet visited
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the list of materials present on the planet.
     *
     * @return a list of {@link Material} objects
     */
    public ArrayList<Material> getMaterials() { return materials;}
}

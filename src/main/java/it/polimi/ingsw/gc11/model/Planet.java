package it.polimi.ingsw.gc11.model;

import java.util.Vector;



public class Planet {
    private boolean visited;
    private Vector<Material> materials;


    public Planet(int numBlue, int numGreen, int numYellow, int numRed) {
        materials = new Vector<>();
        visited = false;
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


    public boolean isVisited() {
        return visited;
    }
    public void setVisited() { this.visited = true; }

    public Vector<Material> getMaterials() {
        Vector<Material> materialsCopy = new Vector<>(materials);
        return materialsCopy;
    }
}

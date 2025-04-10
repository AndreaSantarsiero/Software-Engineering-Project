package it.polimi.ingsw.gc11.model;

import java.util.ArrayList;

public class Planet {
    private boolean visited;
    private ArrayList<Material> materials;
    private Player player;


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


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(Player player) {
        this.visited = true;
        this.player = player;
    }

    public ArrayList<Material> getMaterials() { return materials;}
}

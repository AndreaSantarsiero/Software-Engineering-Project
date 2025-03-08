package it.polimi.ingsw.gc11.model;

import java.util.ArrayList;
import java.util.Vector;

public class Planet {
    private Boolean visited;
    private Vector<Material> materials;

    public Boolean isVisited() {
        return visited;
    }
    public void setVisited() { this.visited = true; }

    public void setMaterials(Vector<Material> materials){ this.materials = materials;}
    public Vector<Material> getMaterials() {
        Vector<Material> materialsCopy = new Vector<>(materials);
        return materialsCopy;
    }
}

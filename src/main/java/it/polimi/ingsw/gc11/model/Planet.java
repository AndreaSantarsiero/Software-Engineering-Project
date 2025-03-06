package it.polimi.ingsw.gc11.model;

import java.util.Vector;

public class Planet {
    private Boolean visited;


    public Boolean isVisited() {
        return visited;
    }

    public Vector<Material> getMaterials() {
        return new Vector<Material>();
    }
}

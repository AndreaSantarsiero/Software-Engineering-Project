package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Material;
import java.util.ArrayList;

public class AbandonedStation extends AdventureCard {

    private int lostDays;
    private int membersRequired;
    private ArrayList<Material> materials;
    private boolean resolved;

    public AbandonedStation(AdventureCard.Type type, int lostDays, int membersRequired, int numBlue, int numGreen, int numYellow, int numRed) {
        super(type);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (membersRequired < 0) {
            throw new IllegalArgumentException("negative members required.");
        }

        this.lostDays = lostDays;
        this.membersRequired = membersRequired;
        this.materials = new ArrayList<>();
        //add materials
        for (int i = 0; i < numBlue; i++)
            materials.add(new Material(Material.Type.BLUE));
        for (int i = 0; i < numGreen; i++)
            materials.add(new Material(Material.Type.GREEN));
        for (int i = 0; i < numYellow; i++)
            materials.add(new Material(Material.Type.YELLOW));
        for (int i = 0; i < numRed; i++)
            materials.add(new Material(Material.Type.RED));
        this.resolved = false;
    }

    public void handler(){
        //to implement
    }
}

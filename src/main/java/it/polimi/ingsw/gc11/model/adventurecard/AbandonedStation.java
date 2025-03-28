package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStation extends AdventureCard {

    private final int lostDays;
    private final int membersRequired;
    private final ArrayList<Material> materials;

    public AbandonedStation(AdventureCard.Type type, int lostDays, int membersRequired, int numBlue, int numGreen, int numYellow, int numRed)throws IllegalArgumentException {
        super(type);

        if (lostDays < 0) {
            throw new IllegalArgumentException("negative lost days.");
        }
        if (membersRequired < 0) {
            throw new IllegalArgumentException("negative members required.");
        }
        if (numBlue < 0 || numGreen < 0 || numYellow < 0 || numRed < 0) {
            throw new IllegalArgumentException("negative materials required.");
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
    }

    public int getLostDays() {return lostDays;}

    public int getMembersRequired() {return membersRequired;}

    public ArrayList<Material> getMaterials() {return materials;}

    //The check if the player has sufficient humans is done by controller
    public void handler(GameModel model, String username, List<List<Material>> materialsUserAccepted, List<Storage> storagesUserAccepted) {
        //User lose days of flight
        model.move(username, lostDays);

        //Add materials to user shipboard, DISCUSS THE PLAYER CAN REARRANGE MATERIALS, need method in shipboard or change
        for(int i = 0; i < storagesUserAccepted.size(); i++) {
            for(int j = 0; j < materialsUserAccepted.get(i).size(); j++) {
                storagesUserAccepted.get(i).addMaterial(materialsUserAccepted.get(i).get(j));
            }
        }
    }

}

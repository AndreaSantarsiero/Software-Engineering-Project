package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AbandonedStationStates.AbandonedStationState;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;



public class AbandonedStation extends AdventureCard {

    private final int lostDays;
    private final int membersRequired;
    private final ArrayList<Material> materials;
    private boolean resolved;



    public AbandonedStation(String id, AdventureCard.Type type, int lostDays, int membersRequired, int numBlue, int numGreen, int numYellow, int numRed)throws IllegalArgumentException {
        super(id, type);

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
        this.resolved = false;

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

    public boolean isResolved() {return resolved;}

    public void resolveCard() {this.resolved = true;}



    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new AbandonedStationState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    @Override
    public void getStates(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setStates(this);
    }
}

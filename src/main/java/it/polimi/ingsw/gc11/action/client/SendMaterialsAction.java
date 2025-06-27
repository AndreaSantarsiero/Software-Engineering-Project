package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.view.*;
import java.util.List;



public class SendMaterialsAction extends ServerAction {

    private final List<Material> materialsBuffer;



    public SendMaterialsAction(List<Material> materialsBuffer){
        this.materialsBuffer = materialsBuffer;
    }



    @Override
    public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override
    public void loadData(CheckPhaseData checkPhaseData) {}

    @Override
    public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.setMaterialsBuffer(materialsBuffer);
    }

    @Override
    public void loadData(EndPhaseData endPhaseData) {}
}
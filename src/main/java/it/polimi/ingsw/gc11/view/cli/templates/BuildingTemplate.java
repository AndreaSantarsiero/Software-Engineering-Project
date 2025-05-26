package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.InputHandler;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;


public class BuildingTemplate extends CLITemplate {

    private final ShipCardCLI shipCardCLI;
    private final ShipBoardCLI shipBoardCLI;
    private String serverMessage;



    public BuildingTemplate(MainCLI mainCLI, InputHandler inputHandler) {
        super(mainCLI, inputHandler);
         shipCardCLI = new ShipCardCLI();
         shipBoardCLI = new ShipBoardCLI(shipCardCLI);
    }



    @Override
    public void update (BuildingPhaseData buildingPhaseData) {
        render(buildingPhaseData);
    }

    @Override
    public void change(){
        mainCLI.changeTemplate(this);
    }



    public void render(BuildingPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
        Thread.dumpStack();
        shipBoardCLI.print(data.getShipBoard());
    }
}

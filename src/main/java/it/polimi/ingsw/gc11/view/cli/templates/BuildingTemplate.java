package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.InputHandler;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.List;


public class BuildingTemplate extends CLITemplate {

    private final ShipCardCLI shipCardCLI;
    private final ShipBoardCLI shipBoardCLI;
    private static final List<String> mainMenu = List.of("Take a free Ship Card", "See adventure card decks", "See enemies ships");



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
        shipBoardCLI.print(data.getShipBoard());
        renderMenu("Select an option (Use W/S to navigate, Enter to select): ", mainMenu, data.getMainMenu());

//        try{
            inputHandler.interactiveMenu(data, mainMenu, data.getMainMenu());
//        } catch (NetworkException e) {
//            System.out.println("Connection error: " + e.getMessage());
//        }
    }
}

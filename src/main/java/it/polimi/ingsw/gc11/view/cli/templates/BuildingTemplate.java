package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.MenuInput;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.List;


public class BuildingTemplate extends CLITemplate {

    private final ShipCardCLI shipCardCLI;
    private final ShipBoardCLI shipBoardCLI;
    private static final List<String> mainMenu = List.of("Take a free Ship Card", "See adventure card decks", "See enemies ships");



    public BuildingTemplate(MainCLI mainCLI) {
        super(mainCLI);
        shipCardCLI = new ShipCardCLI();
        shipBoardCLI = new ShipBoardCLI(shipCardCLI);
    }



    @Override
    public void update (BuildingPhaseData buildingPhaseData) {
        if (active && buildingPhaseData.equals(mainCLI.getContext().getCurrentPhase())) {
            render(buildingPhaseData);
        }
    }

    @Override
    public void change(){
        active = false;
        mainCLI.changeTemplate(this);
    }



    public void render(BuildingPhaseData data) {
        clearView();
        System.out.println("\n\nBuilding Phase");
        ShipBoard shipBoard = data.getShipBoard();



        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                shipBoardCLI.printInvalidSquare();
            }
            else if(x == (shipBoard.getWidth() - 1)){
                System.out.println("    Reserved components:");
            }
        }
        System.out.print("   |");
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                shipBoardCLI.printInvalidSquare();
            }
            else {
                System.out.print("       " + (x + 3 - shipBoard.getWidth()) + "       ");
            }
        }
        for (int i = 0; i < ShipCardCLI.cardLength; i++) {
            shipBoardCLI.printReservedCards(shipBoard, i);
            System.out.println();
        }
        System.out.println();
        shipBoardCLI.printHorizontalCoordinates(shipBoard);
        System.out.println();
        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < ShipCardCLI.cardLength; i++) {
                shipBoardCLI.print(shipBoard, y, i);
                System.out.println();
            }
        }
        shipBoardCLI.printHorizontalCoordinates(shipBoard);
        System.out.println();

        renderMenu("Select an option (Use W/S to navigate, Enter to select): ", mainMenu, data.getMainMenu());
        mainCLI.addInputRequest(new MenuInput(data, mainMenu.size(), data.getMainMenu()));
    }
}

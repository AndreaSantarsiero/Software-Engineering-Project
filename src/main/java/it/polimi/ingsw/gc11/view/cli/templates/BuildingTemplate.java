package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
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
    private static final int rowCount = 10;



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
        List<ShipCard> freeShipCards = data.getFreeShipCards();


        for(int y = 0; y < rowCount; y++){
            for (int i = 0; i < ShipCardCLI.cardLength; i++) {

                //printing user shipBoard (reserved components)
                if(y == 0){
                    if(i <= 2) {
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("      |");
                    }
                    if(i == 3){
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else if(x == (shipBoard.getWidth() - 1)){
                                System.out.println("    Reserved components:               |");
                            }
                        }
                    }
                    if(i == 4){
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else {
                                System.out.print("       " + (x + 3 - shipBoard.getWidth()) + "       ");
                            }
                        }
                        System.out.print("      |");
                    }
                    else {
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else {
                                shipBoardCLI.printReservedCards(shipBoard, i-5);
                                System.out.print("   |");
                            }
                        }
                    }
                }

                else if (y == 1){
                    if(i < 5){
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else {
                                shipBoardCLI.printReservedCards(shipBoard, i+2);
                                System.out.print("   |");
                            }
                        }
                    }
                    else if (i == 5){
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("      |");
                    }
                    else if (i == 6){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                    }
                }

                //printing user shipBoard (main board)
                else if (y < shipBoard.getLength() + 2){
                    shipBoardCLI.print(shipBoard, y-2, i);
                    System.out.print("   |");
                }
                else if (y == shipBoard.getLength() + 2 && i == 0){
                    shipBoardCLI.printHorizontalCoordinates(shipBoard);
                    System.out.println();
                }
                else {
                    System.out.print("   ");
                    for (int x = 0; x < shipBoard.getWidth(); x++) {
                        if(x < shipBoard.getWidth()){
                            shipBoardCLI.printInvalidSquare();
                        }
                    }
                    System.out.print("      |");
                }


                //printing free ship cards
                for(int x = 0; x < freeShipCards.size()/ rowCount; x++){
                    ShipCard shipCard = freeShipCards.get(x* rowCount);
                    shipCard.print(shipCardCLI, i);
                }

                System.out.println();
            }
        }


        renderMenu("\nSelect an option (Use W/S to navigate, Enter to select): ", mainMenu, data.getMainMenu());
        mainCLI.addInputRequest(new MenuInput(data, mainMenu.size(), data.getMainMenu()));
    }
}

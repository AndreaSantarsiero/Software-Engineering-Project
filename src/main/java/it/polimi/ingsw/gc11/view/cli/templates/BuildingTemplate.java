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
    private static final int colCount = 14;
    private static final List<List<String>> mainMenu = List.of(
            List.of("┌┬┐┌─┐┬┌─┌─┐  ┌─┐┬─┐┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    " │ ├─┤├┴┐├┤   ├┤ ├┬┘├┤ ├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    " ┴ ┴ ┴┴ ┴└─┘  └  ┴└─└─┘└─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┌─┐┌─┐┌─┐  ┌─┐┌┐┌┌─┐┌┬┐┬┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐",
                    "└─┐├┤ ├┤   ├┤ │││├┤ ││││├┤ └─┐  └─┐├─┤│├─┘",
                    "└─┘└─┘└─┘  └─┘┘└┘└─┘┴ ┴┴└─┘└─┘  └─┘┴ ┴┴┴  "),
            List.of("┌─┐┌─┐┌─┐  ┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌┬┐┬ ┬┬─┐┌─┐  ┌┬┐┌─┐┌─┐┬┌─┌─┐",
                    "└─┐├┤ ├┤   ├─┤ ││└┐┌┘├┤ │││ │ │ │├┬┘├┤    ││├┤ │  ├┴┐└─┐",
                    "└─┘└─┘└─┘  ┴ ┴─┴┘ └┘ └─┘┘└┘ ┴ └─┘┴└─└─┘  ─┴┘└─┘└─┘┴ ┴└─┘")
    );
    //"Take a free Ship Card", "See adventure card decks", "See enemies ships"
    //https://manytools.org/hacker-tools/ascii-banner/
    //style Calvin S



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
        System.out.println("\n╔╗ ╦ ╦╦╦  ╔╦╗╦╔╗╔╔═╗  ╔═╗╦ ╦╔═╗╔═╗╔═╗\n" +
                             "╠╩╗║ ║║║   ║║║║║║║ ╦  ╠═╝╠═╣╠═╣╚═╗║╣ \n" +
                             "╚═╝╚═╝╩╩═╝═╩╝╩╝╚╝╚═╝  ╩  ╩ ╩╩ ╩╚═╝╚═╝");
        ShipBoard shipBoard = data.getShipBoard();
        List<ShipCard> freeShipCards = data.getFreeShipCards();
        int menuIndex = 0;


        for(int y = 0; y < (freeShipCards.size()/colCount + 1); y++){
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
                        System.out.print("         ");
                    }
                    else if(i == 3){
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else if(x == (shipBoard.getWidth() - 1)){
                                System.out.print("    Reserved components:                  ");
                            }
                        }
                    }
                    else if(i == 4){
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else {
                                System.out.print("       " + (x + 3 - shipBoard.getWidth()) + "       ");
                            }
                        }
                        System.out.print("         ");
                    }
                    else {
                        shipBoardCLI.printReservedCards(shipBoard, i-5);
                        System.out.print("      ");
                    }
                }

                else if (y == 1){
                    if(i < 5){
                        shipBoardCLI.printReservedCards(shipBoard, i+2);
                        System.out.print("      ");
                    }
                    else if (i == 5){
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("         ");
                    }
                    else if (i == 6){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                        System.out.print("      ");
                    }
                }

                //printing user shipBoard (main board)
                else if (y < shipBoard.getLength() + 2){
                    shipBoardCLI.print(shipBoard, y-2, i);
                    System.out.print("      ");
                }
                else if (y == shipBoard.getLength() + 2){
                    if (i == 0){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                        System.out.print("      ");
                    }
                    else {
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("         ");
                    }
                }

                //printing menu
                else {
                    if(menuIndex < mainMenu.size()*mainMenu.getFirst().size()) {
                        int spacesUsed = renderMultiLevelMenu(mainMenu, menuIndex/mainMenu.size(), menuIndex%mainMenu.size(), data.getMainMenu());
                        int singleSpacesLeft = 15 - ((spacesUsed - 3) % 15);
                        int invalidCardsLeft = shipBoard.getWidth() - (spacesUsed - 3)/15 + 1;
                        for (int x = 0; x < singleSpacesLeft; x++) {
                            System.out.print(" ");
                        }
                        for (int x = 0; x < invalidCardsLeft; x++) {
                            shipBoardCLI.printInvalidSquare();
                        }
                        System.out.print("         ");
                        menuIndex++;

                    }
                    else {
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("         ");
                    }
                }


                //printing free ship cards
                for(int x = 0; x < colCount; x++){
                    int index = y * colCount + x;
                    if(index < freeShipCards.size()){
                        ShipCard shipCard = freeShipCards.get(y * colCount + x);
                        shipCard.print(shipCardCLI, i);
                    }
                }

                System.out.println();
            }
        }

        mainCLI.addInputRequest(new MenuInput(data, mainMenu.size(), data.getMainMenu()));
    }
}

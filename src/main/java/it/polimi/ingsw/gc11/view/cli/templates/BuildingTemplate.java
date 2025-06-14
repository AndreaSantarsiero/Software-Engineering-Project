package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.input.*;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import it.polimi.ingsw.gc11.view.cli.controllers.BuildingController;
import org.fusesource.jansi.Ansi;
import java.util.List;
import java.util.Map;



public class BuildingTemplate extends CLITemplate {

    private final BuildingController controller;
    private final ShipCardCLI shipCardCLI;
    private final ShipBoardCLI shipBoardCLI;
    private final AdventureCardCLI adventureCardCLI;
    private static final int rowCount = 11;
    private static final int colCount = 12;
    private static final List<List<String>> mainMenu = List.of(
            List.of("┌┬┐┌─┐┬┌─┌─┐  ┌─┐┬─┐┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    " │ ├─┤├┴┐├┤   ├┤ ├┬┘├┤ ├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    " ┴ ┴ ┴┴ ┴└─┘  └  ┴└─└─┘└─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┬ ┬┌─┐┌─┐  ┬─┐┌─┐┌─┐┌─┐┬─┐┬  ┬┌─┐┌┬┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "│ │└─┐├┤   ├┬┘├┤ └─┐├┤ ├┬┘└┐┌┘├┤  ││  └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "└─┘└─┘└─┘  ┴└─└─┘└─┘└─┘┴└─ └┘ └─┘─┴┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┬─┐┌─┐┌┬┐┌─┐┬  ┬┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "├┬┘├┤ ││││ │└┐┌┘├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "┴└─└─┘┴ ┴└─┘ └┘ └─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌─┐┌─┐┌┬┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├─┤ ││└┐┌┘├─┤││││  ├┤  ││  │││├┤ ││││ │",
                    "┴ ┴─┴┘ └┘ ┴ ┴┘└┘└─┘└─┘─┴┘  ┴ ┴└─┘┘└┘└─┘")
    );
    //"Take a free Ship Card", "See adventure card decks", "See enemies ships"
    //https://manytools.org/hacker-tools/ascii-banner/
    //style Calvin S
    private static final List<List<String>> advancedMenu = List.of(
            List.of("┌─┐┌─┐┌─┐  ┌─┐┌┐┌┌─┐┌┬┐┬┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐",
                    "└─┐├┤ ├┤   ├┤ │││├┤ ││││├┤ └─┐  └─┐├─┤│├─┘",
                    "└─┘└─┘└─┘  └─┘┘└┘└─┘┴ ┴┴└─┘└─┘  └─┘┴ ┴┴┴  "),
            List.of("┌─┐┌─┐┌─┐  ┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌┬┐┬ ┬┬─┐┌─┐  ┌┬┐┌─┐┌─┐┬┌─┌─┐",
                    "└─┐├┤ ├┤   ├─┤ ││└┐┌┘├┤ │││ │ │ │├┬┘├┤    ││├┤ │  ├┴┐└─┐",
                    "└─┘└─┘└─┘  ┴ ┴─┴┘ └┘ └─┘┘└┘ ┴ └─┘┴└─└─┘  ─┴┘└─┘└─┘┴ ┴└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┌┬┐┬┌┬┐┌─┐┬─┐",
                    "├┬┘├┤ └─┐├┤  │    │ ││││├┤ ├┬┘",
                    "┴└─└─┘└─┘└─┘ ┴    ┴ ┴┴ ┴└─┘┴└─"),
            List.of("┌─┐┌┐┌┌┬┐  ┌┐ ┬ ┬┬┬  ┌┬┐┬┌┐┌┌─┐",
                    "├┤ │││ ││  ├┴┐│ │││   │││││││ ┬",
                    "└─┘┘└┘─┴┘  └─┘└─┘┴┴─┘─┴┘┴┘└┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> shipCardMenu = List.of(
            List.of("┌─┐┬  ┌─┐┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "├─┘│  ├─┤│  ├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "┴  ┴─┘┴ ┴└─┘└─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┬─┐┬  ┬┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "├┬┘├┤ └─┐├┤ ├┬┘└┐┌┘├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "┴└─└─┘└─┘└─┘┴└─ └┘ └─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┬─┐┌─┐┬  ┌─┐┌─┐┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "├┬┘├┤ │  ├┤ ├─┤└─┐├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "┴└─└─┘┴─┘└─┘┴ ┴└─┘└─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘")
    );
    private static final List<List<String>> shipCardActionMenu = List.of(
            List.of("┌─┐┬ ┬┌─┐┌─┐┌─┐┌─┐  ┌─┐┌─┐┌─┐┬┌┬┐┬┌─┐┌┐┌",
                    "│  ├─┤│ ││ │└─┐├┤   ├─┘│ │└─┐│ │ ││ ││││",
                    "└─┘┴ ┴└─┘└─┘└─┘└─┘  ┴  └─┘└─┘┴ ┴ ┴└─┘┘└┘"),
            List.of("┬─┐┌─┐┌┬┐┌─┐┌┬┐┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "├┬┘│ │ │ ├─┤ │ ├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "┴└─└─┘ ┴ ┴ ┴ ┴ └─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┌─┐┌─┐┌┐┌┌─┐┬┬─┐┌┬┐",
                    "│  │ ││││├┤ │├┬┘│││",
                    "└─┘└─┘┘└┘└  ┴┴└─┴ ┴"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> shipCardOrientationMenu = List.of(
            List.of("┌┐┌┌─┐┬─┐┌┬┐┌─┐┬  ",
                    "││││ │├┬┘│││├─┤│  ",
                    "┘└┘└─┘┴└─┴ ┴┴ ┴┴─┘"),
            List.of("┬─┐┌─┐┌┬┐┌─┐┌┬┐┌─┐  ┌┬┐┌─┐  ┌┬┐┬ ┬┌─┐  ┬─┐┬┌─┐┬ ┬┌┬┐",
                    "├┬┘│ │ │ ├─┤ │ ├┤    │ │ │   │ ├─┤├┤   ├┬┘││ ┬├─┤ │ ",
                    "┴└─└─┘ ┴ ┴ ┴ ┴ └─┘   ┴ └─┘   ┴ ┴ ┴└─┘  ┴└─┴└─┘┴ ┴ ┴ "),
            List.of("┬─┐┌─┐┌┬┐┌─┐┌┬┐┌─┐  ┬ ┬┌─┐┌─┐┬┌┬┐┌─┐  ┌┬┐┌─┐┬ ┬┌┐┌",
                    "├┬┘│ │ │ ├─┤ │ ├┤   │ │├─┘└─┐│ ││├┤    │││ │││││││",
                    "┴└─└─┘ ┴ ┴ ┴ ┴ └─┘  └─┘┴  └─┘┴─┴┘└─┘  ─┴┘└─┘└┴┘┘└┘ "),
            List.of("┬─┐┌─┐┌┬┐┌─┐┌┬┐┌─┐  ┌┬┐┌─┐  ┌┬┐┬ ┬┌─┐  ┬  ┌─┐┌─┐┌┬┐",
                    "├┬┘│ │ │ ├─┤ │ ├┤    │ │ │   │ ├─┤├┤   │  ├┤ ├┤  │ ",
                    "┴└─└─┘ ┴ ┴ ┴ ┴ └─┘   ┴ └─┘   ┴ ┴ ┴└─┘  ┴─┘└─┘└   ┴ ")
    );
    private static final List<List<String>> adventureDecksMenu = List.of(
            List.of("┌─┐┬┬─┐┌─┐┌┬┐",
                    "├┤ │├┬┘└─┐ │ ",
                    "└  ┴┴└─└─┘ ┴ "),
            List.of("┌─┐┌─┐┌─┐┌─┐┌┐┌┌┬┐",
                    "└─┐├┤ │  │ ││││ ││",
                    "└─┘└─┘└─┘└─┘┘└┘─┴┘"),
            List.of("┌┬┐┬ ┬┬┬─┐┌┬┐",
                    " │ ├─┤│├┬┘ ││",
                    " ┴ ┴ ┴┴┴└──┴┘")
    );
    private static final List<List<String>> endBuildingMenu = List.of(
            List.of("┌─┐┬┬─┐┌─┐┌┬┐",
                    "├┤ │├┬┘└─┐ │ ",
                    "└  ┴┴└─└─┘ ┴ "),
            List.of("┌─┐┌─┐┌─┐┌─┐┌┐┌┌┬┐",
                    "└─┐├┤ │  │ ││││ ││",
                    "└─┘└─┘└─┘└─┘┘└┘─┴┘"),
            List.of("┌┬┐┬ ┬┬┬─┐┌┬┐",
                    " │ ├─┤│├┬┘ ││",
                    " ┴ ┴ ┴┴┴└──┴┘"),
            List.of("┌─┐┌─┐┬ ┬┬─┐┌┬┐┬ ┬",
                    "├┤ │ ││ │├┬┘ │ ├─┤",
                    "└  └─┘└─┘┴└─ ┴ ┴ ┴")
    );
    private static final List<String> pressEnterToContinue = List.of("┌─┐┬─┐┌─┐┌─┐┌─┐  ┌─┐┌┐┌┌┬┐┌─┐┬─┐  ┌┬┐┌─┐  ┌─┐┌─┐┌┐┌┌┬┐┬┌┐┌┬ ┬┌─┐         ",
                                                                     "├─┘├┬┘├┤ └─┐└─┐  ├┤ │││ │ ├┤ ├┬┘   │ │ │  │  │ ││││ │ │││││ │├┤          ",
                                                                     "┴  ┴└─└─┘└─┘└─┘  └─┘┘└┘ ┴ └─┘┴└─   ┴ └─┘  └─┘└─┘┘└┘ ┴ ┴┘└┘└─┘└─┘  o  o  o");



    public BuildingTemplate(BuildingController controller) {
        this.controller = controller;
        shipCardCLI = new ShipCardCLI();
        shipBoardCLI = new ShipBoardCLI(shipCardCLI);
        adventureCardCLI = new AdventureCardCLI();
    }



    public void render() {
        BuildingPhaseData data = controller.getPhaseData();

        clearView();
        System.out.println("╔╗ ╦ ╦╦╦  ╔╦╗╦╔╗╔╔═╗  ╔═╗╦ ╦╔═╗╔═╗╔═╗\n" +
                           "╠╩╗║ ║║║   ║║║║║║║ ╦  ╠═╝╠═╣╠═╣╚═╗║╣ \n" +
                           "╚═╝╚═╝╩╩═╝═╩╝╩╝╚╝╚═╝  ╩  ╩ ╩╩ ╩╚═╝╚═╝");
        ShipBoard shipBoard = data.getShipBoard();
        List<ShipCard> freeShipCards = data.getFreeShipCards();
        int menuIndex = 0;
        int offset = 0;
        if(controller.getShipCardIndex() >= colCount*rowCount){
            offset = ((controller.getShipCardIndex() + 1)/colCount - rowCount) + 1;
        }


        if(data.getState() == BuildingPhaseData.BuildingState.SHOW_ENEMIES_SHIP){
            printEnemiesShipBoard(data.getEnemiesShipBoard());
            for (int i = 0; i < pressEnterToContinue.size(); i++) {
                System.out.println(pressEnterToContinue.get(i));
            }
            return;
        }
        else if(data.getState() == BuildingPhaseData.BuildingState.SHOW_ADVENTURE_DECK){
            System.out.println();
            for (int i = 0; i < AdventureCardCLI.cardLength; i++) {
                for (AdventureCard adventureCard : data.getMiniDeck()) {
                    adventureCardCLI.print(adventureCard, i);
                }
                System.out.println();
            }
            for (int i = 0; i < pressEnterToContinue.size(); i++) {
                System.out.println(pressEnterToContinue.get(i));
            }
            return;
        }



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
                        shipBoardCLI.printReservedCards(shipBoard, i-5, controller.getReservedShipCardIndex());
                        System.out.print("      ");
                    }
                }

                else if (y == 1){
                    if(i < 5){
                        shipBoardCLI.printReservedCards(shipBoard, i+2, controller.getReservedShipCardIndex());
                        System.out.print("      ");
                    }
                    else if (i == 5){
                        printEmptyShipLine(shipBoard);
                    }
                    else if (i == 6){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                        System.out.print("      ");
                    }
                }

                //printing user shipBoard (main board)
                else if (y < shipBoard.getLength() + 2){
                    shipBoardCLI.print(shipBoard, y-2, i, controller.getSelectedJ(), controller.getSelectedI());
                    System.out.print("      ");
                }

                //printing held shipCard
                else if (y == shipBoard.getLength() + 2){
                    if (i == 0){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                        System.out.print("      ");
                    }
                    else if (i == 1){
                        printEmptyShipLine(shipBoard);
                    }
                    else {
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth()/2; x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU || data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION || data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION || data.getState() == BuildingPhaseData.BuildingState.PLACE_SHIPCARD){
                            if(data.getHeldShipCard() != null){
                                data.getHeldShipCard().print(shipCardCLI, i-2, false);
                            }
                            else if(data.getReservedShipCard() != null){
                                data.getReservedShipCard().print(shipCardCLI, i-2, false);
                            }
                            else {
                                shipCardCLI.printEmptyShipCard(i-2);
                            }
                        }
                        else {
                            shipCardCLI.printEmptyShipCard(i-2);
                        }
                        for (int x = 0; x < shipBoard.getWidth()/2; x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("         ");
                    }
                }
                else if ((y == shipBoard.getLength() + 3) && i < 3){
                    if (i < 2){
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth()/2; x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU || data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION || data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION || data.getState() == BuildingPhaseData.BuildingState.PLACE_SHIPCARD){
                            if(data.getHeldShipCard() != null) {
                                data.getHeldShipCard().print(shipCardCLI, i+5, false);
                            }
                            else if(data.getReservedShipCard() != null){
                                data.getReservedShipCard().print(shipCardCLI, i+5, false);
                            }
                            else {
                                shipCardCLI.printEmptyShipCard(i+5);
                            }
                        }
                        else {
                            shipCardCLI.printEmptyShipCard(i+5);
                        }
                        for (int x = 0; x < shipBoard.getWidth()/2; x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("         ");
                    }
                    else {
                        printEmptyShipLine(shipBoard);
                    }
                }


                //printing menu
                else {
                    if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU || data.getState() == BuildingPhaseData.BuildingState.RESERVE_SHIPCARD || data.getState() == BuildingPhaseData.BuildingState.RELEASE_SHIPCARD || (data.getState() == BuildingPhaseData.BuildingState.SHIPCARD_SETUP && controller.getShipCardMenu() != 0)){
                        printMenu(data, shipBoard, menuIndex, shipCardMenu, controller.getShipCardMenu());
                    }
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION || data.getState() == BuildingPhaseData.BuildingState.PLACE_SHIPCARD || (data.getState() == BuildingPhaseData.BuildingState.SHIPCARD_SETUP && controller.getShipCardMenu() == 0)){
                        printMenu(data, shipBoard, menuIndex, shipCardActionMenu, controller.getShipCardActionMenu());
                    }
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION){
                        printMenu(data, shipBoard, menuIndex, shipCardOrientationMenu, controller.getShipCardOrientationMenu());
                    }
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_ADVENTURE_DECK || data.getState() == BuildingPhaseData.BuildingState.WAIT_ADVENTURE_DECK){
                        printMenu(data, shipBoard, menuIndex, adventureDecksMenu, controller.getAdventureCardMenu());
                    }
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_POSITION){
                        printMenu(data, shipBoard, menuIndex, endBuildingMenu, controller.getEndBuildingMenu());
                    }
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_ADVANCED_MENU){
                        printMenu(data, shipBoard, menuIndex, advancedMenu, controller.getAdvancedMenu());
                    }
                    else {
                        printMenu(data, shipBoard, menuIndex, mainMenu, controller.getMainMenu());
                    }
                    menuIndex++;
                }


                //printing free ship cards
                if (y < rowCount){
                    for(int x = offset * colCount; x < (colCount * (1 + offset)); x++){
                        int index = y * colCount + x;
                        if(index < freeShipCards.size()){
                            ShipCard shipCard = freeShipCards.get(y * colCount + x);
                            shipCard.print(shipCardCLI, i, index == controller.getShipCardIndex());
                        }
                        else {
                            shipBoardCLI.printInvalidSquare();
                        }
                    }
                }


                //printing lateral bar
                if (y == 0){
                    if(i == 0){
                        if(offset < 1){
                            System.out.print(" ╔╗");
                        }
                        else {
                            System.out.print(" ┌┐");
                        }
                    }
                    else if (i < 6) {
                        if (offset < 1) {
                            System.out.print(" ║║");
                        } else {
                            System.out.print(" ││");
                        }
                    }
                    else {
                        if(offset < 2){
                            System.out.print(" ║║");
                        }
                        else {
                            System.out.print(" ││");
                        }
                    }
                }
                else if (y == 1){
                    if (i < 5) {
                        if (offset < 2) {
                            System.out.print(" ║║");
                        } else {
                            System.out.print(" ││");
                        }
                    }
                    else {
                        System.out.print(" ║║");
                    }
                }
                else if (y == (rowCount - 2)){
                    if (i < 2) {
                        System.out.print(" ║║");
                    }
                    else {
                        if (offset > 0) {
                            System.out.print(" ║║");
                        } else {
                            System.out.print(" ││");
                        }
                    }
                }
                else if (y == (rowCount - 1)){
                    if(i == 0) {
                        if(offset > 0){
                            System.out.print(" ║║");
                        }
                        else {
                            System.out.print(" ││");
                        }
                    }
                    else if (i < 6) {
                        if (offset < 2) {
                            System.out.print(" ││");
                        } else {
                            System.out.print(" ║║");
                        }
                    }
                    else {
                        if(offset < 2){
                            System.out.print(" └┘");
                        }
                        else {
                            System.out.print(" ╚╝");
                        }
                    }
                }
                else if (y < rowCount) {
                    System.out.print(" ║║");
                }


                System.out.println(Ansi.ansi().reset());
            }
        }
        //System.out.println("(x, y): (" + data.getSelectedX() + "," + data.getSelectedY() + ")  --  (j, i): (" + data.getSelectedJ() + "," + data.getSelectedI() + ")");
        //System.out.println("selected reserved card: " + data.getReservedShipCardIndex() + ", offset: " + offset);


        //printing error messages
        String serverMessage = data.getServerMessage();
        if(serverMessage != null && !serverMessage.isEmpty()) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.RED) + serverMessage.toUpperCase() + Ansi.ansi().reset());
            data.resetServerMessage();
        }
    }



    public void printEmptyShipLine(ShipBoard shipBoard){
        System.out.print("   ");
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < shipBoard.getWidth()){
                shipBoardCLI.printInvalidSquare();
            }
        }
        System.out.print("         ");
    }



    public void printMenu(BuildingPhaseData data, ShipBoard shipBoard, int menuIndex, List<List<String>> options, int selected){
        if(menuIndex < options.size()*options.getFirst().size()) {
            int spacesUsed = renderMultiLevelMenu(options, menuIndex / options.getFirst().size(), menuIndex % options.getFirst().size(), selected);
            printMenuLeftSpaces(shipBoard, spacesUsed);

        }
        else {
            printEmptyShipLine(shipBoard);
        }
    }

    public void printMenuLeftSpaces(ShipBoard shipBoard, int spacesUsed){
        int singleSpacesLeft = 15 - ((spacesUsed - 3) % 15);
        int invalidCardsLeft = shipBoard.getWidth() - (spacesUsed - 3)/15 - 1;
        for (int x = 0; x < singleSpacesLeft; x++) {
            System.out.print(" ");
        }
        for (int x = 0; x < invalidCardsLeft; x++) {
            shipBoardCLI.printInvalidSquare();
        }
        System.out.print("         ");
    }



    public void printEnemiesShipBoard(Map<String, ShipBoard> enemiesShipBoard){
        for (Map.Entry<String, ShipBoard> entry : enemiesShipBoard.entrySet()) {
            System.out.println(entry.getKey() + "'S SHIP:");
            shipBoardCLI.printFullShip(entry.getValue());
        }
    }



    public int getRowCount(){
        return rowCount;
    }

    public int getColCount(){
        return colCount;
    }

    public int getMainMenuSize(){
        return mainMenu.size();
    }

    public int getAdvancedMenuSize(){
        return advancedMenu.size();
    }

    public int getShipCardMenuSize(){
        return shipCardMenu.size();
    }

    public int getShipCardActionMenuSize(){
        return shipCardActionMenu.size();
    }

    public int getShipCardOrientationMenuSize(){
        return shipCardOrientationMenu.size();
    }

    public int getAdventureDecksMenuSize(){
        return adventureDecksMenu.size();
    }

    public int endBuildingMenuSize(){
        return endBuildingMenu.size();
    }
}

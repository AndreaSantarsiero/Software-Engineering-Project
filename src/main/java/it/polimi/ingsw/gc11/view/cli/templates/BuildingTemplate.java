package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.ListIndexInput;
import it.polimi.ingsw.gc11.view.cli.input.MenuInput;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class BuildingTemplate extends CLITemplate {

    private final ShipCardCLI shipCardCLI;
    private final ShipBoardCLI shipBoardCLI;
    private static final int colCount = 14;
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
                    "└─┘┘└┘─┴┘  └─┘└─┘┴┴─┘─┴┘┴┘└┘└─┘")
    );
    //"Take a free Ship Card", "See adventure card decks", "See enemies ships"
    //https://manytools.org/hacker-tools/ascii-banner/
    //style Calvin S
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
        System.out.println("╔╗ ╦ ╦╦╦  ╔╦╗╦╔╗╔╔═╗  ╔═╗╦ ╦╔═╗╔═╗╔═╗\n" +
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
                        printEmptyShipLine(shipBoard);
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
                        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU || data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION || data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION){
                            if(data.getHeldShipCard() != null){
                                data.getHeldShipCard().print(shipCardCLI, i-2, false);
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
                        if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU){
                            data.getHeldShipCard().print(shipCardCLI, i+5, false);
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

                    //ship card menu
                    if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU || data.getState() == BuildingPhaseData.BuildingState.RESERVE_SHIPCARD || data.getState() == BuildingPhaseData.BuildingState.RELEASE_SHIPCARD || (data.getState() == BuildingPhaseData.BuildingState.SHIPCARD_SETUP && data.getShipCardMenu() != 0)){
                        printMenu(data, shipBoard, menuIndex, shipCardMenu, data.getShipCardMenu());
                    }

                    //ship card action menu
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION || data.getState() == BuildingPhaseData.BuildingState.PLACE_SHIPCARD || (data.getState() == BuildingPhaseData.BuildingState.SHIPCARD_SETUP && data.getShipCardMenu() == 0)){
                        printMenu(data, shipBoard, menuIndex, shipCardActionMenu, data.getShipCardActionMenu());
                    }

                    //ship card orientation menu
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION){
                        printMenu(data, shipBoard, menuIndex, shipCardOrientationMenu, data.getShipCardOrientationMenu());
                    }

                    //adventure card menu
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_ADVENTURE_DECK || data.getState() == BuildingPhaseData.BuildingState.WAIT_ADVENTURE_DECK || data.getState() == BuildingPhaseData.BuildingState.SHOW_ADVENTURE_DECK){
                        printMenu(data, shipBoard, menuIndex, adventureDecksMenu, data.getAdventureCardMenu());
                    }

                    //end building menu
                    else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_POSITION){
                        printMenu(data, shipBoard, menuIndex, endBuildingMenu, data.getEndBuildingMenu());
                    }

                    //main menu
                    else {
                        printMenu(data, shipBoard, menuIndex, mainMenu, data.getMainMenu());
                    }
                    menuIndex++;
                }


                //printing free ship cards
                for(int x = 0; x < colCount; x++){
                    int index = y * colCount + x;
                    if(index < freeShipCards.size()){
                        ShipCard shipCard = freeShipCards.get(y * colCount + x);
                        shipCard.print(shipCardCLI, i, index == data.getShipCardIndex());
                    }
                }

                System.out.println(Ansi.ansi().reset());
            }
        }


        //printing error messages
        String serverMessage = data.getServerMessage();
        if(serverMessage != null && !serverMessage.isEmpty()) {
            System.out.println(serverMessage);
            data.resetServerMessage();
        }



        //input and commands
        if(data.isStateNew()){
            addInputRequest(data);
        }
    }


    public void addInputRequest(BuildingPhaseData data) {
        try{
            if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_MAIN_MENU){
                mainCLI.addInputRequest(new MenuInput(data, mainMenu.size(), data.getMainMenu()));
            }
            else if (data.getState() == BuildingPhaseData.BuildingState.CHOOSE_FREE_SHIPCARD){
                mainCLI.addInputRequest(new ListIndexInput(data, data.getFreeShipCards().size(), colCount, data.getShipCardIndex()));
            }
            else if (data.getState() == BuildingPhaseData.BuildingState.WAIT_SHIPCARD){
                mainCLI.getVirtualServer().getFreeShipCard(data.getShipCardIndex());
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_MENU){
                mainCLI.addInputRequest(new MenuInput(data, shipCardMenu.size(), data.getShipCardMenu()));
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ACTION){
                mainCLI.addInputRequest(new MenuInput(data, shipCardActionMenu.size(), data.getShipCardActionMenu()));
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.PLACE_SHIPCARD){
                //scelgo dove metterla
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_SHIPCARD_ORIENTATION){
                mainCLI.addInputRequest(new MenuInput(data, shipCardOrientationMenu.size(), data.getShipCardOrientationMenu()));
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RESERVE_SHIPCARD){
                mainCLI.getVirtualServer().reserveShipCard(data.getHeldShipCard());
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RELEASE_SHIPCARD){
                mainCLI.getVirtualServer().releaseShipCard(data.getHeldShipCard());
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.SHIPCARD_SETUP){
                //invio richiesta placeShipCard
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.WAIT_ENEMIES_SHIP){
                //manca azione per richiederle
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.SHOW_ENEMIES_SHIP){
                //aspetto invio utente
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.WAIT_ADVENTURE_DECK){
                mainCLI.getVirtualServer().observeMiniDeck(data.getAdventureCardMenu());
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.SHOW_ADVENTURE_DECK){
                //aspetto invio utente
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.RESET_TIMER){
                //manca azione per reset timer
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.CHOOSE_POSITION){
                mainCLI.addInputRequest(new MenuInput(data, endBuildingMenu.size(), data.getEndBuildingMenu()));
            }
            else if(data.getState() == BuildingPhaseData.BuildingState.END_BUILDING_SETUP){
                mainCLI.getVirtualServer().endBuilding(data.getEndBuildingMenu());
            }
        } catch (NetworkException e) {
            System.out.println("Connection error: " + e.getMessage());
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
}

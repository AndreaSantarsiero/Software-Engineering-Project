package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.CheckPhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.CheckController;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class CheckTemplate extends CLITemplate {

    private final CheckController controller;
    private static final int rowCount = 10;
    private static final List<List<String>> mainMenu = List.of(
            List.of("┬─┐┌─┐┌┬┐┌─┐┬  ┬┌─┐  ┌─┐┬ ┬┬┌─┐┌─┐┌─┐┬─┐┌┬┐",
                    "├┬┘├┤ ││││ │└┐┌┘├┤   └─┐├─┤│├─┘│  ├─┤├┬┘ ││",
                    "┴└─└─┘┴ ┴└─┘ └┘ └─┘  └─┘┴ ┴┴┴  └─┘┴ ┴┴└──┴┘"),
            List.of("┌─┐┌─┐┌─┐  ┌─┐┌┐┌┌─┐┌┬┐┬┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐",
                    "└─┐├┤ ├┤   ├┤ │││├┤ ││││├┤ └─┐  └─┐├─┤│├─┘",
                    "└─┘└─┘└─┘  └─┘┘└┘└─┘┴ ┴┴└─┘└─┘  └─┘┴ ┴┴┴  "),
            List.of("┌─┐┬ ┬┌─┐┌─┐┬┌─  ┌─┐┬ ┬┬┌─┐",
                    "│  ├─┤├┤ │  ├┴┐  └─┐├─┤│├─┘",
                    "└─┘┴ ┴└─┘└─┘┴ ┴  └─┘┴ ┴┴┴  "),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┌─┐┬ ┬┬┌─┐",
                    "├┬┘├┤ └─┐├┤  │   └─┐├─┤│├─┘",
                    "┴└─└─┘└─┘└─┘ ┴   └─┘┴ ┴┴┴  ")
    );



    public CheckTemplate(CheckController controller) {
        this.controller = controller;
    }



    public void render() {
        CheckPhaseData data = controller.getPhaseData();

        clearView();
        System.out.println("╔═╗╦ ╦╔═╗╔═╗╦╔═  ╔═╗╦ ╦╔═╗╔═╗╔═╗\n" +
                           "║  ╠═╣║╣ ║  ╠╩╗  ╠═╝╠═╣╠═╣╚═╗║╣ \n" +
                           "╚═╝╩ ╩╚═╝╚═╝╩ ╩  ╩  ╩ ╩╩ ╩╚═╝╚═╝");
        ShipBoard shipBoard = data.getShipBoard();
        int menuIndex = 0;


//        if(data.getState() == CheckPhaseData.CheckState.SHOW_ENEMIES_SHIP){
//            printEnemiesShipBoard(data.getEnemiesShipBoard());
//            for (int i = 0; i < pressEnterToContinue.size(); i++) {
//                System.out.println(pressEnterToContinue.get(i));
//            }
//            return;
//        }



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
                        shipBoardCLI.printReservedCards(shipBoard, i-5, -1);
                        System.out.print("      ");
                    }
                }

                else if (y == 1){
                    if(i < 5){
                        shipBoardCLI.printReservedCards(shipBoard, i+2, -1);
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


                //printing menu
                else {
                    printMenu(shipBoard, menuIndex, mainMenu, controller.getMainMenu());
                    menuIndex++;
                }


                System.out.println(Ansi.ansi().reset());
            }
        }


        //printing error messages
        String serverMessage = data.getServerMessage();
        if(serverMessage != null && !serverMessage.isEmpty()) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.RED) + serverMessage.toUpperCase() + Ansi.ansi().reset());
            data.resetServerMessage();
        }
    }
}

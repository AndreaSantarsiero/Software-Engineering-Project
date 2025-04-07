package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;



public class mainCLI {
    public static void main(String[] args) {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard6.json");
        ShipBoard shipBoard = shipBoardLoader.getShipBoard();
        ShipBoardCLI.print(shipBoard);
        System.out.println("Exposed connectors: " + shipBoard.getExposedConnectors());

        if(shipBoard.checkShip()){
            System.out.println("Shipboard respects every rule");
        }
        else {
            System.out.println("Shipboard DOES NOT respect the rules");
        }
        

        System.out.println("\n\nExample of a covered ship card:");
        ShipCardCLI.printCovered();


        System.out.println("\n\nExample of an adventure card:");
        AdventureCardCLI.string(null);


        System.out.println("\n\nExample of a level 1 flight board with one player on it:");
        FlightBoardCLI.printLevel1();


        System.out.println("\n\nExample of a level 2 flight board with two players on it:");
        FlightBoardCLI.printLevel2();


        System.out.println("\n\nExample of a level 3 flight board with one player on it:");
        FlightBoardCLI.printLevel3();
    }
}

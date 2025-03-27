package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;



public class mainCLI {
    public static void main(String[] args) {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard4.json");
        ShipBoard shipBoard = shipBoardLoader.getShipBoard();
        CLI.printShipBoard(shipBoard);
        System.out.println("Exposed connectors: " + shipBoard.getExposedConnectors());

        if(shipBoard.checkShip()){
            System.out.println("Shipboard respects every rule");
        }
        else {
            System.out.println("Shipboard DOES NOT respect the rules");
        }
    }
}

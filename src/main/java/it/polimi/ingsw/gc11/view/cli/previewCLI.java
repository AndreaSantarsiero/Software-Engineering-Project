package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.Planet;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.adventurecard.PlanetsCard;
import it.polimi.ingsw.gc11.model.adventurecard.StarDust;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.FlightBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import java.util.ArrayList;
import java.util.List;



public class previewCLI {
    public static void main(String[] args) {
        ShipBoardLoader shipBoardLoader = new ShipBoardLoader("src/test/resources/it/polimi/ingsw/gc11/shipBoards/shipBoard7.json");
        ShipBoard shipBoard = shipBoardLoader.getShipBoard();
        ShipCardCLI shipCardCLI = new ShipCardCLI();
        ShipBoardCLI shipBoardCLI = new ShipBoardCLI(shipCardCLI);
        AdventureCardCLI adventureCardCLI = new AdventureCardCLI();

        //printing user shipBoard (reserved components)
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                shipBoardCLI.printInvalidSquare();
            }
            else if(x == (shipBoard.getWidth() - 1)){
                System.out.println("    Reserved components:               |");
            }
        }
        System.out.print("   ");
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                shipBoardCLI.printInvalidSquare();
            }
            else {
                System.out.print("       " + (x + 3 - shipBoard.getWidth()) + "       ");
            }
        }
        System.out.println("      |");
        for (int i = 0; i < ShipCardCLI.cardLength + 1; i++) {
            shipBoardCLI.printReservedCards(shipBoard, i);
            System.out.println("   |");
        }

        //printing user shipBoard (main board)
        shipBoardCLI.printHorizontalCoordinates(shipBoard);
        System.out.println();
        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < ShipCardCLI.cardLength; i++) {
                shipBoardCLI.print(shipBoard, y, i);
                System.out.println("   |");
            }
        }
        shipBoardCLI.printHorizontalCoordinates(shipBoard);
        System.out.println();
        System.out.println("\nExposed connectors: " + shipBoard.getExposedConnectors());

        if(shipBoard.checkShip()){
            System.out.println("Shipboard respects every rule");
        }
        else {
            System.out.println("Shipboard DOES NOT respect the rules");
        }
        

        System.out.println("\n\n\nExample of a covered ship card:");
        for (int i = 0; i < ShipCardCLI.cardLength; i++){
            shipCardCLI.printCovered(i);
            System.out.println();
        }


        System.out.println("\n\n\nExample of some adventure cards:");
        List<AdventureCard> adventureCards = new ArrayList<>();
        adventureCards.add(new StarDust(AdventureCard.Type.TRIAL));
        adventureCards.add(new StarDust(AdventureCard.Type.LEVEL2));
        StarDust starDust = new StarDust(AdventureCard.Type.TRIAL);
        starDust.useCard();
        adventureCards.add(starDust);
        starDust = new StarDust(AdventureCard.Type.LEVEL2);
        starDust.useCard();
        adventureCards.add(starDust);

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(new Planet(1, 1, 1, 1));
        planets.add(new Planet(0, 0, 3, 2));
        planets.add(new Planet(5, 0, 0, 0));
        PlanetsCard planetsCard = new PlanetsCard(AdventureCard.Type.LEVEL2, 5, planets);
        planetsCard.useCard();
        adventureCards.add(planetsCard);
        adventureCards.add(null);
        for (int i = 0; i < AdventureCardCLI.cardLength; i++) {
            for (AdventureCard adventureCard : adventureCards) {
                adventureCardCLI.print(adventureCard, i);
            }
            System.out.println();
        }



        System.out.println("\n\n\nExample of a level 1 flight board with one player on it:");
        FlightBoardCLI.printLevel1();


        System.out.println("\n\n\nExample of a level 2 flight board with two players on it:");
        FlightBoardCLI.printLevel2();


        System.out.println("\n\n\nExample of a level 3 flight board with one player on it:");
        FlightBoardCLI.printLevel3();
    }
}

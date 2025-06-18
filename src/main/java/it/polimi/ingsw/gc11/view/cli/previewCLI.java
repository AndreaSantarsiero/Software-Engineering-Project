package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.loaders.ShipBoardLoader;
import it.polimi.ingsw.gc11.model.*;
import it.polimi.ingsw.gc11.model.adventurecard.*;
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

        shipBoardCLI.printFullShip(shipBoard);
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
        adventureCards.add(new StarDust("id", AdventureCard.Type.TRIAL));
        adventureCards.add(new StarDust("id", AdventureCard.Type.LEVEL2));
        Pirates pirates = new Pirates("id", AdventureCard.Type.TRIAL, 1, 1, 1, new ArrayList<>(List.of(new Shot(Hit.Type.SMALL, Hit.Direction.TOP))));
        OpenSpace openSpace = new OpenSpace("id", AdventureCard.Type.TRIAL);
        pirates.useCard();
        openSpace.useCard();
        adventureCards.add(pirates);
        adventureCards.add(openSpace);
        StarDust starDust = new StarDust("id", AdventureCard.Type.LEVEL2);
        starDust.useCard();
        adventureCards.add(starDust);

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(new Planet(1, 1, 1, 1));
        planets.add(new Planet(0, 0, 3, 2));
        planets.add(new Planet(5, 0, 0, 0));
        PlanetsCard planetsCard = new PlanetsCard("id", AdventureCard.Type.LEVEL2, 5, planets);
        planetsCard.useCard();
        adventureCards.add(planetsCard);
        MeteorSwarm meteorSwarm = new MeteorSwarm("id", AdventureCard.Type.TRIAL, new ArrayList<>(List.of(new Meteor(Hit.Type.SMALL, Hit.Direction.TOP))));
        meteorSwarm.useCard();
        adventureCards.add(meteorSwarm);
        for (int i = 0; i < AdventureCardCLI.cardLength; i++) {
            for (AdventureCard adventureCard : adventureCards) {
                adventureCardCLI.print(adventureCard, i);
            }
            System.out.println();
        }

        adventureCards.clear();
        Epidemic epidemic = new Epidemic("id");
        epidemic.useCard();
        adventureCards.add(epidemic);
        AbandonedShip abandonedShip = new AbandonedShip("id", AdventureCard.Type.TRIAL, 3, 2, 10);
        abandonedShip.useCard();
        adventureCards.add(abandonedShip);
        AbandonedStation abandonedStation = new AbandonedStation("id", AdventureCard.Type.TRIAL, 3, 2, 1, 1, 1, 1);
        abandonedStation.useCard();
        adventureCards.add(abandonedStation);
        CombatZoneLv1 combatZoneLv1 = new CombatZoneLv1("id", AdventureCard.Type.TRIAL, 3, 2, new ArrayList<>(List.of(new Shot(Hit.Type.SMALL, Hit.Direction.TOP))));
        combatZoneLv1.useCard();
        adventureCards.add(combatZoneLv1);
        CombatZoneLv2 combatZoneLv2 = new CombatZoneLv2("id", AdventureCard.Type.TRIAL, 3, 2, new ArrayList<>(List.of(new Shot(Hit.Type.SMALL, Hit.Direction.TOP))));
        combatZoneLv2.useCard();
        adventureCards.add(combatZoneLv2);
        Slavers slavers = new Slavers("id", AdventureCard.Type.LEVEL2, 3, 7, 4, 8);
        slavers.useCard();
        adventureCards.add(slavers);
        Smugglers smugglers = new Smugglers("id", AdventureCard.Type.LEVEL2, 4, 8, 3,new ArrayList<>(List.of(new Material(Material.Type.RED))));
        smugglers.useCard();
        adventureCards.add(smugglers);

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

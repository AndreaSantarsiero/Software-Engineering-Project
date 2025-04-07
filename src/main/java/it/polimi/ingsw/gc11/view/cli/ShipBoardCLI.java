package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import java.util.List;



public class ShipBoardCLI {

    public static void print(ShipBoard shipBoard) {
//        AnsiConsole.systemInstall();
//        System.out.println(Ansi.ansi()
//                .bg(Ansi.Color.BLUE)
//                .fg(Ansi.Color.BLUE)
//                .a(" ".repeat(50))
//                .reset());


        printReservedCards(shipBoard);
        System.out.println();


        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < ShipCardCLI.cardLength; i++) {
                System.out.print("  ");
                for (int x = 0; x < shipBoard.getWidth(); x++) {
                    if (shipBoard.validateCoordinates(x, y)) {
                        ShipCard shipCard = shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                        ShipCardCLI.print(shipCard, i);
                    }
                    else {
                        printInvalidSquare(i);
                    }
                }
                System.out.println("  ");
            }
        }
    }



    public static void printReservedCards(ShipBoard shipBoard) {
        List<ShipCard> reservedCards = shipBoard.getReservedComponents();
        while (reservedCards.size() < 2) {
            reservedCards.add(null);
        }

        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                printInvalidSquare(0);
            }
            else if(x == (shipBoard.getWidth() - 1)){
                System.out.println("   Reserved components:");
            }
        }

        for (int i = 0; i < ShipCardCLI.cardLength; i++) {
            System.out.print("  ");
            for (int x = 0; x < shipBoard.getWidth(); x++) {
                if(x < (shipBoard.getWidth() - 2)){
                    printInvalidSquare(i);
                }
                else if(x == (shipBoard.getWidth() - 1)){
                    for (ShipCard shipCard : reservedCards) {
                        ShipCardCLI.print(shipCard, i);
                    }
                }
            }
            System.out.println("  ");
        }
    }



    public static void printInvalidSquare(int i){
        System.out.print("               ");
    }
}

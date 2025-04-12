package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class ShipBoardCLI {

    private static ShipCardCLI shipCardCLI;



    public ShipBoardCLI() {
        shipCardCLI = new ShipCardCLI();
    }

    public void print(ShipBoard shipBoard) {
//        System.out.println(Ansi.ansi()
//                .bg(Ansi.Color.BLUE)
//                .fg(Ansi.Color.BLUE)
//                .a(" ".repeat(50))
//                .reset());


        printReservedCards(shipBoard);
        System.out.println();
        printHorizontalCoordinates(shipBoard);

        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < ShipCardCLI.cardLength; i++) {
                printVerticalCoordinates(shipBoard, y, i);

                for (int x = 0; x < shipBoard.getWidth(); x++) {
                    if (shipBoard.validateCoordinates(x, y)) {
                        ShipCard shipCard = shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                        if(shipCard != null) {
                            shipCardCLI.setIndex(i);
                            shipCard.accept(shipCardCLI);
                        }
                        else{
                            printInvalidSquare();
                        }
                    }
                    else {
                        printInvalidSquare();
                    }
                }

                printVerticalCoordinates(shipBoard, y, i);
                System.out.println();
            }
        }

        printHorizontalCoordinates(shipBoard);
    }



    public void printReservedCards(ShipBoard shipBoard) {
        List<ShipCard> reservedCards = shipBoard.getReservedComponents();
        while (reservedCards.size() < 2) {
            reservedCards.add(null);
        }

        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                printInvalidSquare();
            }
            else if(x == (shipBoard.getWidth() - 1)){
                System.out.println("    Reserved components:");
            }
        }

        System.out.print("   ");
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                printInvalidSquare();
            }
            else {
                System.out.print("       " + (x + 3 - shipBoard.getWidth()) + "       ");
            }
        }
        System.out.println();

        for (int i = 0; i < ShipCardCLI.cardLength; i++) {
            System.out.print("   ");
            for (int x = 0; x < shipBoard.getWidth(); x++) {
                if(x < (shipBoard.getWidth() - 2)){
                    printInvalidSquare();
                }
                else if(x == (shipBoard.getWidth() - 1)){
                    for (ShipCard shipCard : reservedCards) {
                        if (shipCard != null) {
                            shipCardCLI.setIndex(i);
                            shipCard.accept(shipCardCLI);
                        }
                        else {
                            shipCardCLI.printEmptyShipCard();
                        }
                    }
                }
            }
            System.out.println("   ");
        }
    }



    public void printInvalidSquare(){
        System.out.print("               ");
    }



    public void printHorizontalCoordinates(ShipBoard shipBoard){
        System.out.print(Ansi.ansi().reset() + "   ");

        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x - shipBoard.adaptX(0) < 10){
                System.out.print("       " + (x - shipBoard.adaptX(0)) + "       ");
            }
            else {
                System.out.print("      " + (x - shipBoard.adaptX(0)) + "       ");
            }
        }

        System.out.println("   ");
    }



    public void printVerticalCoordinates(ShipBoard shipBoard, int y, int i){
        if (i == (ShipCardCLI.cardLength/2)){
            System.out.print(Ansi.ansi().reset() + " " + (y - shipBoard.adaptY(0)) + " ");
        }
        else {
            System.out.print(Ansi.ansi().reset() + "   ");
        }
    }
}

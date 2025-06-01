package it.polimi.ingsw.gc11.view.cli.utils;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;
import java.util.List;



/**
 * Handles the CLI (Command Line Interface) rendering of the ShipBoard
 * <p>
 * This class contains methods that can be used to print the entire ship including components, reserved cards, coordinates, and invalid spaces
 */
public class ShipBoardCLI {

    private final ShipCardCLI shipCardCLI;



    /**
     * Constructs a ShipBoardCLI with the given ShipCardCLI renderer
     *
     * @param shipCardCLI the ShipCardCLI used to render individual ship cards
     */
    public ShipBoardCLI(ShipCardCLI shipCardCLI) {
        this.shipCardCLI = shipCardCLI;
    }



    /**
     * Prints the entire ship board, including ship cards, coordinates, and reserved components
     *
     * @param shipBoard the ShipBoard to print
     */
    public void print(ShipBoard shipBoard, int y, int i) {
//        System.out.println(Ansi.ansi()
//                .bg(Ansi.Color.BLUE)
//                .fg(Ansi.Color.BLUE)
//                .a(" ".repeat(50))
//                .reset());


        printVerticalCoordinates(shipBoard, y, i);

        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if (shipBoard.validateIndexes(x, y)) {
                ShipCard shipCard = shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                if(shipCard != null) {
                    shipCard.print(shipCardCLI, i, false);
                }
                else if (shipBoard.validateIndexes(x, y)) {
                    shipCardCLI.printEmptyShipCard(i);
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
    }



    /**
     * Prints the reserved ship cards, typically shown above the ship on the right side
     *
     * @param shipBoard the ShipBoard containing the reserved cards
     */
    public void printReservedCards(ShipBoard shipBoard, int i) {
        List<ShipCard> reservedCards = shipBoard.getReservedComponents();
        if (i == 0){
            while (reservedCards.size() < 2) {
                reservedCards.add(null);
            }
        }

        System.out.print("   ");
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < (shipBoard.getWidth() - 2)){
                printInvalidSquare();
            }
            else if(x == (shipBoard.getWidth() - 1)){
                for (ShipCard shipCard : reservedCards) {
                    if (i < ShipCardCLI.cardLength){
                        if (shipCard != null) {
                            shipCard.print(shipCardCLI, i, false);
                        }
                        else {
                            shipCardCLI.printEmptyShipCard(i);
                        }
                    }
                    else {
                        printInvalidSquare();
                    }
                }
            }
        }
        System.out.print("   ");
    }



    /**
     * Prints a blank space representing an invalid square on the ship board
     */
    public void printInvalidSquare(){
        System.out.print("               ");
    }



    /**
     * Prints the horizontal coordinates (x-axis) of the ship board layout
     *
     * @param shipBoard the ShipBoard to print coordinates for
     */
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

        System.out.print("   ");
    }



    /**
     * Prints the vertical coordinates (y-axis) of the ship board, aligned with ship cards
     *
     * @param shipBoard the ShipBoard to print coordinates for
     * @param y the current row
     * @param i the current print index within the ship card height
     */
    public void printVerticalCoordinates(ShipBoard shipBoard, int y, int i){
        if (i == (ShipCardCLI.cardLength/2)){
            System.out.print(Ansi.ansi().reset() + " " + (y - shipBoard.adaptY(0)) + " ");
        }
        else {
            System.out.print(Ansi.ansi().reset() + "   ");
        }
    }
}

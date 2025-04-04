package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import java.util.List;



public class CLI {

    public static int cardWidth = 15;
    public static int cardLength = 7;



    public static void printShipBoard(ShipBoard shipBoard) {
        AnsiConsole.systemInstall();
        System.out.println(Ansi.ansi()
                .bg(Ansi.Color.BLUE)
                .fg(Ansi.Color.BLUE)
                .a(" ".repeat(50))
                .reset());


        printReservedCards(shipBoard);
        System.out.println();


        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < cardLength; i++) {
                System.out.print("  ");
                for (int x = 0; x < shipBoard.getWidth(); x++) {
                    if (shipBoard.validateCoordinates(x, y)) {
                        ShipCard shipCard = shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                        printShipCard(shipCard, i);
                    }
                    else {
                        printInvalidSquare(i);
                    }
                }
                System.out.println("  ");
            }
        }


        System.out.println(Ansi.ansi()
                .bg(Ansi.Color.BLUE)
                .fg(Ansi.Color.BLUE)
                .a(" ".repeat(50))
                .reset());
        AnsiConsole.systemUninstall();
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

        for (int i = 0; i < cardLength; i++) {
            System.out.print("  ");
            for (int x = 0; x < shipBoard.getWidth(); x++) {
                if(x < (shipBoard.getWidth() - 2)){
                    printInvalidSquare(i);
                }
                else if(x == (shipBoard.getWidth() - 1)){
                    for (ShipCard shipCard : reservedCards) {
                        printShipCard(shipCard, i);
                    }
                }
            }
            System.out.println("  ");
        }
    }



    public static String topConnectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "       ";
            case ShipCard.Connector.SINGLE -> "   ╩   ";
            case ShipCard.Connector.DOUBLE -> "╚═════╝";
            case ShipCard.Connector.UNIVERSAL -> "╚══╩══╝";
        };
    }



    public static String leftConnectorToString(ShipCard.Connector connector, int i) {
        if(i == (cardLength/2 - 1)){
            return switch (connector) {
                case ShipCard.Connector.NONE, ShipCard.Connector.SINGLE -> "  ";
                case ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL -> "═╗";
            };
        }
        else if(i == cardLength/2){
            return switch (connector) {
                case ShipCard.Connector.NONE -> "  ";
                case ShipCard.Connector.SINGLE -> "══";
                case ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL -> "═╣";
            };
        }
        else if(i == (cardLength/2 + 1)){
            return switch (connector) {
                case ShipCard.Connector.NONE, ShipCard.Connector.SINGLE -> "  ";
                case ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL -> "═╝";
            };
        }
        else{
            return "   ";
        }
    }



    public static String bottomConnectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "       ";
            case ShipCard.Connector.SINGLE -> "   ╦   ";
            case ShipCard.Connector.DOUBLE -> "╔═════╗";
            case ShipCard.Connector.UNIVERSAL -> "╔══╦══╗";
        };
    }



    public static String rightConnectorToString(ShipCard.Connector connector, int i) {
        if(i == (cardLength/2 - 1)){
            return switch (connector) {
                case ShipCard.Connector.NONE, ShipCard.Connector.SINGLE -> "  ";
                case ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL -> "╔═";
            };
        }
        else if(i == cardLength/2){
            return switch (connector) {
                case ShipCard.Connector.NONE -> "  ";
                case ShipCard.Connector.SINGLE -> "══";
                case ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL -> "╠═";
            };
        }
        else if(i == (cardLength/2 + 1)){
            return switch (connector) {
                case ShipCard.Connector.NONE, ShipCard.Connector.SINGLE -> "  ";
                case ShipCard.Connector.DOUBLE, ShipCard.Connector.UNIVERSAL -> "╚═";
            };
        }
        else{
            return "   ";
        }
    }



    public static String shipCardToEmoji(ShipCard shipCard) {
        return switch (shipCard) {
            case AlienUnit alienUnit -> "A";
            case Battery battery -> "B";
            case Cannon cannon -> "C";
            case Engine engine -> "E";
            case HousingUnit housingUnit -> "H";
            case Shield shield -> "S";
            case Storage storage -> "s";
            case StructuralModule structuralModule -> "T";
            case null, default -> "?";
        };
    }



    public static void printShipCard(ShipCard shipCard, int i) {
        StringBuilder currentLine = new StringBuilder();

        if (i == 0) {
            System.out.print("┌─────────────┐");
        }
        else if (i == (cardLength - 1)){
            System.out.print("└─────────────┘");
        }
        else {
            currentLine.append("│");
            if (shipCard != null) {
                if (i == 1) {
                    currentLine.append("   ").append(topConnectorToString(shipCard.getTopConnector())).append("   ");
                }
                else if (i == (cardLength/2 - 1)){
                    currentLine.append(leftConnectorToString(shipCard.getLeftConnector(), i)).append("         ").append(rightConnectorToString(shipCard.getRightConnector(), i));
                }
                else if (i == cardLength/2){
                    for (int j = 1; j < (cardWidth - 1); j++) {
                        if (j == 1){
                            currentLine.append(leftConnectorToString(shipCard.getLeftConnector(), i));
                        }
                        else if (j == (cardWidth/2 + 1)){
                            currentLine.append(shipCardToEmoji(shipCard));
                        }
                        else if (j == (cardWidth - 2)){
                            currentLine.append(rightConnectorToString(shipCard.getRightConnector(), i));
                        }
                        else if (!(j == 2|| j == cardWidth/2)){
                            currentLine.append(" ");
                        }
                    }
                }
                else if (i == (cardLength/2 + 1)){
                    currentLine.append(leftConnectorToString(shipCard.getLeftConnector(), i)).append("         ").append(rightConnectorToString(shipCard.getRightConnector(), i));
                }
                else if (i == (cardLength - 2)) {
                    currentLine.append("   ").append(bottomConnectorToString(shipCard.getBottomConnector())).append("   ");
                }
                else {
                    currentLine.append("             ");
                }
            }
            else {
                currentLine.append("             ");
            }
            currentLine.append("│");
        }

        System.out.print(currentLine.toString());
    }



    public static void printInvalidSquare(int i){
        System.out.print("               ");
    }
}

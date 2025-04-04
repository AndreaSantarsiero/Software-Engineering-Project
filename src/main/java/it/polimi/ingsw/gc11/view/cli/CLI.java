package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;



public class CLI {

    public static int cardWidth = 17;
    public static int cardLength = 7;



    public static void printShipBoard(ShipBoard shipBoard) {
        AnsiConsole.systemInstall();
        System.out.println(Ansi.ansi()
                .bg(Ansi.Color.BLUE)
                .fg(Ansi.Color.BLUE)
                .a(" ".repeat(50))
                .reset());

        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < cardLength; i++) {
                System.out.print("  ");
                for (int x = 0; x < shipBoard.getWidth(); x++) {
                    if (shipBoard.validateCoordinates(x, y)) {
                        ShipCard shipCard = shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                        System.out.print(printShipCard(shipCard, i));
                    }
                    else {
                        System.out.print(printInvalidSquare(i));
                    }
                }
                System.out.println("  ");
            }
        }

        System.out.println(Ansi.ansi()
                .bg(Ansi.Color.BLUE)
                .reset());
        AnsiConsole.systemUninstall();
    }



    public static String connectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "0";
            case ShipCard.Connector.SINGLE -> "1";
            case ShipCard.Connector.DOUBLE -> "2";
            case ShipCard.Connector.UNIVERSAL -> "3";
        };
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



    public static String printShipCard(ShipCard shipCard, int i) {
        StringBuilder currentLine = new StringBuilder();

        if (i == 0 || i == (cardLength - 1)) {
            currentLine.append("+---------------+");
        }
        else {
            currentLine.append("|");
            if (shipCard != null) {
                for (int j = 1; j < (cardWidth - 1); j++) {
                    if (i == 1 && j == 8) {
                        currentLine.append(connectorToString(shipCard.getTopConnector()));
                    }
                    else if (i == 3) {
                        if (j == 1){
                            currentLine.append(connectorToString(shipCard.getLeftConnector()));
                        }
                        else if (j == 15){
                            currentLine.append(connectorToString(shipCard.getRightConnector()));
                        }
                        else if (j == 8){
                            currentLine.append(shipCardToEmoji(shipCard));
                        }
                        else {
                            currentLine.append(" ");
                        }
                    }
                    else if (i == 5 && j == 8) {
                        currentLine.append(connectorToString(shipCard.getBottomConnector()));
                    }
                    else {
                        currentLine.append(" ");
                    }
                }
            }
            else {
                currentLine.append("               ");
            }
            currentLine.append("|");
        }

        return currentLine.toString();
    }



    public static String printInvalidSquare(int i){
        return "                 ";
    }
}

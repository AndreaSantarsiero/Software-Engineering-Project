package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.*;



public class CLI {
    public static void printShipBoard(ShipBoard shipBoard) {
        for (int y = 0; y < shipBoard.getLength(); y++) {
            for (int i = 0; i < 4; i++) {
                for (int x = 0; x < shipBoard.getWidth(); x++) {
                    if (i == 0){
                        System.out.print("+---------");
                    }
                    else{
                        System.out.print("|");
                        try{
                            if (shipBoard.validateCoordinates(x, y)) {
                                ShipCard shipCard = shipBoard.getShipCard(x - shipBoard.adaptX(0), y - shipBoard.adaptY(0));
                                if (shipCard != null) {
                                    for (int j = 1; j < 10; j++) {
                                        if (i == 1 && j == 5) {
                                            System.out.print(connectorToString(shipCard.getTopConnector()));
                                        }
                                        else if (i == 2) {
                                            if (j == 1){
                                                System.out.print(connectorToString(shipCard.getLeftConnector()));
                                            }
                                            else if (j == 9){
                                                System.out.print(connectorToString(shipCard.getRightConnector()));
                                            }
                                            else if (j == 5){
                                                System.out.print(shipCardToEmoji(shipCard));
                                            }
                                            else {
                                                System.out.print(" ");
                                            }
                                        }
                                        else if (i == 3 && j == 5) {
                                            System.out.print(connectorToString(shipCard.getBottomConnector()));
                                        }
                                        else {
                                            System.out.print(" ");
                                        }
                                    }
                                }
                                else {
                                    System.out.print("         ");
                                }
                            }
                            else {
                                System.out.print("#########");
                            }
                        } catch (Exception _) {
                            System.out.print("         ");
                        }
                    }
                }
                if(i == 0){
                    System.out.println("+");
                }
                else {
                    System.out.println("|");
                }
            }
        }

        for (int x = 0; x < shipBoard.getWidth(); x++) {
            System.out.print("+---------");
        }
        System.out.println("+");
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
}

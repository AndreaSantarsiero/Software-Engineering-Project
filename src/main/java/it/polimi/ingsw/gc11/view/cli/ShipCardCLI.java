package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.shipcard.*;



public class ShipCardCLI {

    public static int cardWidth = 15;
    public static int cardLength = 7;



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



    public static void print(ShipCard shipCard, int i) {
        if (i == 0) {
            System.out.print("┌─────────────┐");
        }
        else if (i == (cardLength - 1)){
            System.out.print("└─────────────┘");
        }
        else {
            StringBuilder currentLine = new StringBuilder();
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
            System.out.print(currentLine.toString());
        }
    }



    public static void printCovered() {
        for(int i = 0; i < cardLength; i++){
            if (i == 0) {
                System.out.print("┌─────────────┐");
            }
            else if (i == 1 || i == (cardLength - 2)) {
                System.out.print("│ *         * │");
            }
            else if (i == cardLength/2){
                System.out.print("│   COVERED   │");
            }
            else if (i == (cardLength - 1)){
                System.out.print("└─────────────┘");
            }
            else {
                System.out.print("│             │");
            }

            System.out.println();
        }
    }
}

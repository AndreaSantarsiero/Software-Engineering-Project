package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;



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



    public static String shipCardToString(ShipCard shipCard, int i) {
        if(i == (cardLength/2 - 1)){
            switch (shipCard) {
                case HousingUnit housingUnit -> {
                    if (housingUnit.isCentral()) {
                        return " CENTRAL ";
                    } else {
                        return "         ";
                    }
                }
                case Shield shield -> {
                    StringBuilder result = new StringBuilder();

                    if (shield.isProtecting(Hit.Direction.LEFT)) {
                        result.append("< ");
                    } else {
                        result.append("  ");
                    }
                    if (shield.isProtecting(Hit.Direction.TOP)) {
                        result.append("∧ ∧ ∧");
                    } else {
                        result.append("     ");
                    }
                    if (shield.isProtecting(Hit.Direction.RIGHT)) {
                        result.append(" >");
                    } else {
                        result.append("  ");
                    }

                    return result.toString();
                }
                case Storage storage -> {
                    if (storage.getType().equals(Storage.Type.DOUBLE_RED) || storage.getType().equals(Storage.Type.SINGLE_RED)) {
                        return " SPECIAL ";
                    } else {
                        return "         ";
                    }
                }
                case null, default -> {
                    return "         ";
                }
            }
        }


        else if(i == (cardLength/2)){
            return switch (shipCard) {
                case AlienUnit alienUnit -> "  ALIEN  ";
                case Battery battery -> " BATTERY ";
                case Cannon cannon -> " CANNON  ";
                case Engine engine -> " ENGINE  ";
                case HousingUnit housingUnit -> "  CABIN  ";
                case Shield shield -> " SHIELD  ";
                case Storage storage -> "  CARGO  ";
                case StructuralModule structuralModule -> "STRUCTURE";
                case null, default -> " UNKNOWN ";
            };
        }


        else if(i == (cardLength/2 + 1)){
            switch (shipCard) {
                case AlienUnit alienUnit -> {
                    if (alienUnit.getType().equals(AlienUnit.Type.BROWN)) {
                        return "  BROWN  ";
                    } else if (alienUnit.getType().equals(AlienUnit.Type.PURPLE)) {
                        return "  PURPLE ";
                    } else {
                        return "   ???   ";
                    }
                }
                case Battery battery -> {
                    StringBuilder result = new StringBuilder();

                    result.append("   ").append(battery.getAvailableBatteries()).append("/");
                    if (battery.getType().equals(Battery.Type.DOUBLE)) {
                        result.append("2   ");
                    } else if (battery.getType().equals(Battery.Type.TRIPLE)) {
                        result.append("3   ");
                    } else {
                        result.append("?   ");
                    }

                    return result.toString();
                }
                case HousingUnit housingUnit -> {
                    StringBuilder result = new StringBuilder();

                    result.append("   ").append(housingUnit.getNumMembers()).append("/");
                    if (housingUnit.getAlienUnit() != null) {
                        result.append("1   ");
                    } else {
                        result.append("2   ");
                    }

                    return result.toString();
                }
                case Shield shield -> {
                    StringBuilder result = new StringBuilder();

                    if (shield.isProtecting(Hit.Direction.LEFT)) {
                        result.append("< ");
                    } else {
                        result.append("  ");
                    }
                    if (shield.isProtecting(Hit.Direction.BOTTOM)) {
                        result.append("v v v");
                    } else {
                        result.append("     ");
                    }
                    if (shield.isProtecting(Hit.Direction.RIGHT)) {
                        result.append(" >");
                    } else {
                        result.append("  ");
                    }

                    return result.toString();
                }
                case StructuralModule structuralModule -> {
                    return " MODULE  ";
                }
                case null, default -> {
                    return "         ";
                }
            }
        }
        else {
            return "         ";
        }
    }



    public static void setColor(ShipCard shipCard){
        switch (shipCard) {
            case AlienUnit alienUnit -> {
                if (alienUnit.getType().equals(AlienUnit.Type.BROWN)) {
                    System.out.print(Ansi.ansi().reset().fg(Ansi.Color.YELLOW));
                }
                else if (alienUnit.getType().equals(AlienUnit.Type.PURPLE)) {
                    System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA));
                }
            }
            case Battery battery -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.GREEN));
            case Cannon cannon -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA));
            case Engine engine -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.YELLOW));
            case HousingUnit housingUnit -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE));
            case Shield shield -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.GREEN));
            case Storage storage -> {
                if (storage.getType().equals(Storage.Type.DOUBLE_RED) || storage.getType().equals(Storage.Type.SINGLE_RED)) {
                    System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED));
                }
                else if (storage.getType().equals(Storage.Type.TRIPLE_BLUE) || storage.getType().equals(Storage.Type.DOUBLE_BLUE)) {
                    System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE));
                }
            }
            case null, default -> System.out.print(Ansi.ansi().reset());
        }
    }



    public static void print(ShipCard shipCard, int i) {
        setColor(shipCard);

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
                else if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
                    currentLine.append(leftConnectorToString(shipCard.getLeftConnector(), i)).append(shipCardToString(shipCard, i)).append(rightConnectorToString(shipCard.getRightConnector(), i));
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

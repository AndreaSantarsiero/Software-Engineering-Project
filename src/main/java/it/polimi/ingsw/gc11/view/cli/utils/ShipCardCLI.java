package it.polimi.ingsw.gc11.view.cli.utils;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;



/**
 * CLI (Command Line Interface) renderer for ShipCard elements
 */
public class ShipCardCLI {

    public static int cardWidth = 15;
    public static int cardLength = 7;



    public ShipCardCLI(){}



    /**
     * Converts a top connector type to its string representation
     * @param connector connector type
     * @return formatted string for top connector
     */
    public String topConnectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "       ";
            case ShipCard.Connector.SINGLE -> "   ╩   ";
            case ShipCard.Connector.DOUBLE -> "╚═════╝";
            case ShipCard.Connector.UNIVERSAL -> "╚══╩══╝";
        };
    }



    /**
     * Converts a left connector type to its string representation based on current line index
     * @param connector connector type
     * @return formatted string for left connector
     */
    public String leftConnectorToString(ShipCard.Connector connector, int i) {
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
                case ShipCard.Connector.DOUBLE -> " ║";
                case ShipCard.Connector.UNIVERSAL -> "═╣";
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



    /**
     * Converts a bottom connector type to its string representation
     * @param connector connector type
     * @return formatted string for bottom connector
     */
    public String bottomConnectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "       ";
            case ShipCard.Connector.SINGLE -> "   ╦   ";
            case ShipCard.Connector.DOUBLE -> "╔═════╗";
            case ShipCard.Connector.UNIVERSAL -> "╔══╦══╗";
        };
    }



    /**
     * Converts a right connector type to its string representation based on current line index
     * @param connector connector type
     * @return formatted string for right connector
     */
    public String rightConnectorToString(ShipCard.Connector connector, int i) {
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
                case ShipCard.Connector.DOUBLE -> "║ ";
                case ShipCard.Connector.UNIVERSAL -> "╠═";
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



    /**
     * Sets the printing color for an AlienUnit card based on its type
     * @param alienUnit the AlienUnit instance
     */
    public void setColor(AlienUnit alienUnit) {
        if (alienUnit.getType().equals(AlienUnit.Type.BROWN)) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW));
        }
        else if (alienUnit.getType().equals(AlienUnit.Type.PURPLE)) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.MAGENTA));
        }
    }


    /**
     * Sets the printing color for a Battery card
     * @param battery the Battery instance
     */
    public void setColor(Battery battery) {
        System.out.print(Ansi.ansi().fg(Ansi.Color.GREEN));
    }


    /**
     * Sets the printing color for a Cannon card
     * @param cannon the Cannon instance
     */
    public void setColor(Cannon cannon) {
        System.out.print(Ansi.ansi().fg(Ansi.Color.MAGENTA));
    }


    /**
     * Sets the printing color for an Engine card
     * @param engine the Engine instance
     */
    public void setColor(Engine engine) {
        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW));
    }


    /**
     * Sets the printing color for a HousingUnit card
     * @param housingUnit the HousingUnit instance
     */
    public void setColor(HousingUnit housingUnit) {
        if(housingUnit.isCentral()){
            switch (housingUnit.getId()){
                case "BlueCentralUnit" -> System.out.print(Ansi.ansi().fg(Ansi.Color.BLUE));
                case "GreenCentralUnit" -> System.out.print(Ansi.ansi().fg(Ansi.Color.GREEN));
                case "RedCentralUnit" -> System.out.print(Ansi.ansi().fg(Ansi.Color.RED));
                case "YellowCentralUnit" -> System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW));
            }
        }
        else{
            System.out.print(Ansi.ansi().fg(Ansi.Color.BLUE));
        }
    }


    /**
     * Sets the printing color for a Shield card
     * @param shield the Shield instance
     */
    public void setColor(Shield shield) {
        System.out.print(Ansi.ansi().fg(Ansi.Color.GREEN));
    }


    /**
     * Sets the printing color for a Storage card based on its type
     * @param storage the Storage instance
     */
    public void setColor(Storage storage) {
        if (storage.getType().equals(Storage.Type.DOUBLE_RED) || storage.getType().equals(Storage.Type.SINGLE_RED)) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.RED));
        }
        else if (storage.getType().equals(Storage.Type.TRIPLE_BLUE) || storage.getType().equals(Storage.Type.DOUBLE_BLUE)) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.BLUE));
        }
    }


    /**
     * Sets the printing color for a StructuralModule card
     * @param structuralModule the StructuralModule instance
     */
    public void setColor(StructuralModule structuralModule) {}


    public void setSelectedBackground(boolean selected) {
        System.out.print(Ansi.ansi().reset());
        if(selected){
            System.out.print("\033[48;5;235m");
        }
    }



    /**
     * Prints a generic ship card depending on the current line index
     * @param shipCard the ShipCard to render
     */
    public void printShipCard(ShipCard shipCard, int i) {
        if (i == 0) {
            System.out.print("┌─────────────┐");
        }
        else if (i == (cardLength - 1)){
            System.out.print("└─────────────┘");
        }
        else {
            System.out.print("│");

            if (i == 1) {
                System.out.print("   " + topConnectorToString(shipCard.getTopConnector()) + "   ");
            }
            else if (i == (cardLength - 2)) {
                System.out.print("   " + bottomConnectorToString(shipCard.getBottomConnector()) + "   ");
            }
            else {
                System.out.print("             ");
            }

            System.out.print("│");
        }
    }



    /**
     * Renders an AlienUnit card based on the current line index
     * @param alienUnit the AlienUnit instance
     */
    public void draw(AlienUnit alienUnit, int i, boolean selected) {
        setSelectedBackground(selected);

        if(alienUnit.isCovered()) {
            printCovered(i);
        }
        else {
            setColor(alienUnit);

            if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
                System.out.print("│" + leftConnectorToString(alienUnit.getLeftConnector(), i));
                printAlienUnitCenter(alienUnit, i);
                System.out.print(rightConnectorToString(alienUnit.getRightConnector(), i) + "│");
            }
            else {
                printShipCard(alienUnit, i);
            }
        }
    }

    /**
     * Prints the central section of an AlienUnit card
     * @param alienUnit the AlienUnit instance
     */
    public void printAlienUnitCenter(AlienUnit alienUnit, int i) {
        if(i == (cardLength/2 - 1)) {
            System.out.print("         ");
        }
        else if (i == cardLength/2) {
            System.out.print("  ALIEN  ");
        }
        else if (i == (cardLength/2 + 1)) {
            if (alienUnit.getType().equals(AlienUnit.Type.BROWN)) {
                System.out.print("  BROWN  ");
            } else if (alienUnit.getType().equals(AlienUnit.Type.PURPLE)) {
                System.out.print("  PURPLE ");
            } else {
                System.out.print("   ???   ");
            }
        }
    }



    /**
     * Renders a Battery card based on the current line index
     * @param battery the Battery instance
     */
    public void draw(Battery battery, int i, boolean selected) {
        setSelectedBackground(selected);

        if(battery.isCovered()){
            printCovered(i);
        }
        else {
            setColor(battery);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(battery.getLeftConnector(), i));
                printBatteryCenter(battery, i);
                System.out.print(rightConnectorToString(battery.getRightConnector(), i) + "│");
            } else {
                printShipCard(battery, i);
            }
        }
    }

    /**
     * Prints the central section of a Battery card
     * @param battery the Battery instance
     */
    public void printBatteryCenter(Battery battery, int i) {
        if(i == (cardLength/2 - 1)) {
            System.out.print("         ");
        }
        else if (i == cardLength/2) {
            System.out.print(" BATTERY ");
        }
        else if (i == (cardLength/2 + 1)) {
            StringBuilder result = new StringBuilder();

            result.append("   ").append(battery.getAvailableBatteries()).append("/");
            if (battery.getType().equals(Battery.Type.DOUBLE)) {
                result.append("2   ");
            } else if (battery.getType().equals(Battery.Type.TRIPLE)) {
                result.append("3   ");
            } else {
                result.append("?   ");
            }

            System.out.print(result);
        }
    }



    /**
     * Renders a Cannon card based on the current line index
     * @param cannon the Cannon instance
     */
    public void draw(Cannon cannon, int i, boolean selected) {
        setSelectedBackground(selected);

        if(cannon.isCovered()){
            printCovered(i);
        }
        else {
            setColor(cannon);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(cannon.getLeftConnector(), i));
                printCannonCenter(cannon, i);
                System.out.print(rightConnectorToString(cannon.getRightConnector(), i) + "│");
            } else {
                printShipCard(cannon, i);
            }
        }
    }

    /**
     * Prints the central section of a Cannon card
     * @param cannon the Cannon instance
     */
    public void printCannonCenter(Cannon cannon, int i) {
        if(i == (cardLength/2 - 1)) {
            if(cannon.getOrientation().equals(ShipCard.Orientation.DEG_0) && cannon.getType().equals(Cannon.Type.SINGLE)){
                System.out.print("    ∧    ");
            }
            else if (cannon.getOrientation().equals(ShipCard.Orientation.DEG_0) && cannon.getType().equals(Cannon.Type.DOUBLE)){
                System.out.print("   ∧ ∧   ");
            }
            else if (cannon.getType().equals(Cannon.Type.DOUBLE) && cannon.getOrientation().equals(Cannon.Orientation.DEG_90)) {
                System.out.print("        >");
            }
            else if (cannon.getType().equals(Cannon.Type.DOUBLE) && cannon.getOrientation().equals(Cannon.Orientation.DEG_270)){
                System.out.print("<        ");
            }
            else {
                System.out.print("         ");
            }
        }
        else if (i == cardLength/2) {
            if (cannon.getOrientation().equals(ShipCard.Orientation.DEG_90) && cannon.getType().equals(Cannon.Type.SINGLE)){
                System.out.print(" CANNON >");
            }
            else if (cannon.getOrientation().equals(ShipCard.Orientation.DEG_270) && cannon.getType().equals(Cannon.Type.SINGLE)){
                System.out.print("< CANNON ");
            }
            else{
                System.out.print("  CANNON ");
            }
        }
        else if (i == (cardLength/2 + 1)) {
            if(cannon.getOrientation().equals(ShipCard.Orientation.DEG_180) && cannon.getType().equals(Cannon.Type.SINGLE)){
                System.out.print("    v    ");
            }
            else if (cannon.getOrientation().equals(ShipCard.Orientation.DEG_180) && cannon.getType().equals(Cannon.Type.DOUBLE)){
                System.out.print("   v v   ");
            }
            else if (cannon.getType().equals(Cannon.Type.DOUBLE) && cannon.getOrientation().equals(Cannon.Orientation.DEG_90)) {
                System.out.print("        >");
            }
            else if (cannon.getType().equals(Cannon.Type.DOUBLE) && cannon.getOrientation().equals(Cannon.Orientation.DEG_270)){
                System.out.print("<        ");
            }
            else {
                System.out.print("         ");
            }
        }
    }



    /**
     * Renders an Engine card based on the current line index
     * @param engine the Engine instance
     */
    public void draw(Engine engine, int i, boolean selected) {
        setSelectedBackground(selected);

        if(engine.isCovered()){
            printCovered(i);
        }
        else {
            setColor(engine);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(engine.getLeftConnector(), i));
                printEngineCenter(engine, i);
                System.out.print(rightConnectorToString(engine.getRightConnector(), i) + "│");
            } else {
                printShipCard(engine, i);
            }
        }
    }

    /**
     * Prints the central section of an Engine card
     * @param engine the Engine instance
     */
    public void printEngineCenter(Engine engine, int i) {
        if(i == (cardLength/2 - 1)) {
            if(engine.getOrientation().equals(ShipCard.Orientation.DEG_180) && engine.getType().equals(Engine.Type.SINGLE)){
                System.out.print("    ∧    ");
            }
            else if (engine.getOrientation().equals(ShipCard.Orientation.DEG_180) && engine.getType().equals(Engine.Type.DOUBLE)){
                System.out.print("   ∧ ∧   ");
            }
            else if(engine.getType().equals(Engine.Type.DOUBLE) && engine.getOrientation().equals(Engine.Orientation.DEG_90)){
                System.out.print("<        ");
            }
            else if (engine.getType().equals(Engine.Type.DOUBLE) && engine.getOrientation().equals(Engine.Orientation.DEG_270)){
                System.out.print("        >");
            }
            else {
                System.out.print("         ");
            }
        }
        else if (i == cardLength/2) {
            if (engine.getOrientation().equals(ShipCard.Orientation.DEG_90) && engine.getType().equals(Engine.Type.SINGLE)){
                System.out.print("< ENGINE ");
            }
            else if (engine.getOrientation().equals(ShipCard.Orientation.DEG_270) && engine.getType().equals(Engine.Type.SINGLE)){
                System.out.print(" ENGINE >");
            }
            else{
                System.out.print("  ENGINE ");
            }
        }
        else if (i == (cardLength/2 + 1)) {
            if(engine.getOrientation().equals(ShipCard.Orientation.DEG_0) && engine.getType().equals(Engine.Type.SINGLE)){
                System.out.print("    v    ");
            }
            else if (engine.getOrientation().equals(ShipCard.Orientation.DEG_0) && engine.getType().equals(Engine.Type.DOUBLE)){
                System.out.print("   v v   ");
            }
            else if(engine.getType().equals(Engine.Type.DOUBLE) && engine.getOrientation().equals(Engine.Orientation.DEG_90)){
                System.out.print("<        ");
            }
            else if (engine.getType().equals(Engine.Type.DOUBLE) && engine.getOrientation().equals(Engine.Orientation.DEG_270)){
                System.out.print("        >");
            }
            else {
                System.out.print("         ");
            }
        }
    }



    /**
     * Renders a HousingUnit card based on the current line index
     * @param housingUnit the HousingUnit instance
     */
    public void draw(HousingUnit housingUnit, int i, boolean selected) {
        setSelectedBackground(selected);

        if(housingUnit.isCovered()){
            printCovered(i);
        }
        else {
            setColor(housingUnit);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(housingUnit.getLeftConnector(), i));
                printHousingUnitCenter(housingUnit, i);
                System.out.print(rightConnectorToString(housingUnit.getRightConnector(), i) + "│");
            } else {
                printShipCard(housingUnit, i);
            }
        }
    }

    /**
     * Prints the central section of a HousingUnit card
     * @param housingUnit the HousingUnit instance
     */
    public void printHousingUnitCenter(HousingUnit housingUnit, int i) {
        if(i == (cardLength/2 - 1)) {
            if (housingUnit.isCentral()) {
                System.out.print(" CENTRAL ");
            } else {
                System.out.print("         ");
            }
        }
        else if (i == cardLength/2) {
            System.out.print("  CABIN  ");
        }
        else if (i == (cardLength/2 + 1)) {
            StringBuilder result = new StringBuilder();

            result.append("   ").append(housingUnit.getNumMembers()).append("/");
            if (housingUnit.getAlienUnit() != null) {
                result.append("1   ");
            } else {
                result.append("2   ");
            }

            System.out.print(result);
        }
    }



    /**
     * Renders a Shield card based on the current line index
     * @param shield the Shield instance
     */
    public void draw(Shield shield, int i, boolean selected) {
        setSelectedBackground(selected);

        if(shield.isCovered()){
            printCovered(i);
        }
        else {
            setColor(shield);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(shield.getLeftConnector(), i));
                printShieldCenter(shield, i);
                System.out.print(rightConnectorToString(shield.getRightConnector(), i) + "│");
            } else {
                printShipCard(shield, i);
            }
        }
    }

    /**
     * Prints the central section of a Shield card
     * @param shield the Shield instance
     */
    public void printShieldCenter(Shield shield, int i) {
        if(i == (cardLength/2 - 1)) {
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

            System.out.print(result);
        }
        else if (i == cardLength/2) {
            if (shield.isProtecting(Hit.Direction.LEFT)) {
                System.out.print("< SHIELD ");
            }
            else if (shield.isProtecting(Hit.Direction.RIGHT)) {
                System.out.print(" SHIELD >");
            }
            else {
                System.out.print("  SHIELD ");
            }
        }
        else if (i == (cardLength/2 + 1)) {
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

            System.out.print(result);
        }
    }



    /**
     * Renders a Storage card based on the current line index
     * @param storage the Storage instance
     */
    public void draw(Storage storage, int i, boolean selected) {
        setSelectedBackground(selected);

        if(storage.isCovered()){
            printCovered(i);
        }
        else {
            setColor(storage);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(storage.getLeftConnector(), i));
                printStorageCenter(storage, i);
                System.out.print(rightConnectorToString(storage.getRightConnector(), i) + "│");
            } else {
                printShipCard(storage, i);
            }
        }
    }

    /**
     * Prints the central section of a Storage card
     * @param storage the Storage instance
     */
    public void printStorageCenter(Storage storage, int i) {
        if(i == (cardLength/2 - 1)) {
            if (storage.getType().equals(Storage.Type.DOUBLE_RED) || storage.getType().equals(Storage.Type.SINGLE_RED)) {
                System.out.print(" SPECIAL ");
            } else {
                System.out.print("         ");
            }
        }
        else if (i == cardLength/2) {
            System.out.print("  CARGO  ");
        }
        else if (i == (cardLength/2 + 1)) {
            if(storage.getType().equals(Storage.Type.DOUBLE_RED) || storage.getType().equals(Storage.Type.DOUBLE_BLUE)){
                System.out.print("   ");
                MaterialCLI.print(storage.getMaterials());
                setColor(storage);
                for (int j = 0; j < (2 - storage.getMaterials().size()); j++) {
                    MaterialCLI.printEmpty();
                }
                System.out.print("  ");
            }
            else if(storage.getType().equals(Storage.Type.SINGLE_RED)){
                System.out.print("    ");
                MaterialCLI.print(storage.getMaterials());
                setColor(storage);
                for (int j = 0; j < (1 - storage.getMaterials().size()); j++) {
                    MaterialCLI.printEmpty();
                }
                System.out.print("   ");
            }
            else if(storage.getType().equals(Storage.Type.TRIPLE_BLUE)){
                System.out.print("  ");
                MaterialCLI.print(storage.getMaterials());
                setColor(storage);
                for (int j = 0; j < (3 - storage.getMaterials().size()); j++) {
                    MaterialCLI.printEmpty();
                }
                System.out.print(" ");
            }
            else{
                System.out.print("   ???   ");
            }
        }
    }



    /**
     * Renders a StructuralModule card based on the current line index
     * @param structuralModule the StructuralModule instance
     */
    public void draw(StructuralModule structuralModule, int i, boolean selected) {
        setSelectedBackground(selected);

        if(structuralModule.isCovered()){
            printCovered(i);
        }
        else {
            setColor(structuralModule);

            if (i >= (cardLength / 2 - 1) && i <= (cardLength / 2 + 1)) {
                System.out.print("│" + leftConnectorToString(structuralModule.getLeftConnector(), i));
                printStructuralModuleCenter(structuralModule, i);
                System.out.print(rightConnectorToString(structuralModule.getRightConnector(), i) + "│");
            } else {
                printShipCard(structuralModule, i);
            }
        }
    }

    /**
     * Prints the central section of a StructuralModule card
     * @param structuralModule the StructuralModule instance
     */
    public void printStructuralModuleCenter(StructuralModule structuralModule, int i) {
        if(i == (cardLength/2 - 1)) {
            System.out.print("         ");
        }
        else if (i == cardLength/2) {
            System.out.print("STRUCTURE");
        }
        else if (i == (cardLength/2 + 1)) {
            System.out.print(" MODULE  ");
        }
    }



    /**
     * Renders an empty ship card based on the current line index
     */
    public void printEmptyShipCard(int i) {
        if (i == 0) {
            System.out.print("┌─────────────┐");
        }
        else if (i == (cardLength - 1)){
            System.out.print("└─────────────┘");
        }
        else {
            System.out.print("│             │");
        }
    }



    /**
     * Renders a covered ship card based on the current line index
     */
    public void printCovered(int i) {
        if (i == 1 || i == (cardLength - 2)) {
            System.out.print("│ *         * │");
        }
        else if (i == cardLength/2){
            System.out.print("│   COVERED   │");
        }
        else {
            printEmptyShipCard(i);
        }
    }
}

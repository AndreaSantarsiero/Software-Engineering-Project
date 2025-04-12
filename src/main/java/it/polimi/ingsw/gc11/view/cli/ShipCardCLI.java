package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.shipcard.*;
import org.fusesource.jansi.Ansi;



public class ShipCardCLI implements ShipCardVisitor {

    public static int cardWidth = 15;
    public static int cardLength = 7;
    public int i;



    public ShipCardCLI(){}



    public void setIndex(int i){
        this.i = i;
    }



    public String topConnectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "       ";
            case ShipCard.Connector.SINGLE -> "   ╩   ";
            case ShipCard.Connector.DOUBLE -> "╚═════╝";
            case ShipCard.Connector.UNIVERSAL -> "╚══╩══╝";
        };
    }



    public String leftConnectorToString(ShipCard.Connector connector) {
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



    public String bottomConnectorToString(ShipCard.Connector connector) {
        return switch (connector) {
            case ShipCard.Connector.NONE -> "       ";
            case ShipCard.Connector.SINGLE -> "   ╦   ";
            case ShipCard.Connector.DOUBLE -> "╔═════╗";
            case ShipCard.Connector.UNIVERSAL -> "╔══╦══╗";
        };
    }



    public String rightConnectorToString(ShipCard.Connector connector) {
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



    @Override
    public void visit(AlienUnit alienUnit) {
        printAlienUnit(alienUnit);
    }

    @Override
    public void visit(Battery battery) {
        printBattery(battery);
    }

    @Override
    public void visit(Cannon cannon) {
        printCannon(cannon);
    }

    @Override
    public void visit(Engine engine) {
        printEngine(engine);
    }

    @Override
    public void visit(HousingUnit housingUnit) {
        printHousingUnit(housingUnit);
    }

    @Override
    public void visit(Shield shield) {
        printShield(shield);
    }

    @Override
    public void visit(Storage storage) {
        printStorage(storage);
    }

    @Override
    public void visit(StructuralModule structuralModule) {
        printStructuralModule(structuralModule);
    }



    public void setColor(AlienUnit alienUnit) {
        if (alienUnit.getType().equals(AlienUnit.Type.BROWN)) {
            System.out.print(Ansi.ansi().reset().fg(Ansi.Color.YELLOW));
        }
        else if (alienUnit.getType().equals(AlienUnit.Type.PURPLE)) {
            System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA));
        }
    }

    public void setColor(Battery battery) {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.GREEN));
    }

    public void setColor(Cannon cannon) {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA));
    }

    public void setColor(Engine engine) {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.YELLOW));
    }

    public void setColor(HousingUnit housingUnit) {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE));
    }

    public void setColor(Shield shield) {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.GREEN));
    }

    public void setColor(Storage storage) {
        if (storage.getType().equals(Storage.Type.DOUBLE_RED) || storage.getType().equals(Storage.Type.SINGLE_RED)) {
            System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED));
        }
        else if (storage.getType().equals(Storage.Type.TRIPLE_BLUE) || storage.getType().equals(Storage.Type.DOUBLE_BLUE)) {
            System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE));
        }
    }

    public void setColor(StructuralModule structuralModule) {
        System.out.print(Ansi.ansi().reset());
    }



    public void printShipCard(ShipCard shipCard) {
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



    public void printAlienUnit(AlienUnit alienUnit) {
        setColor(alienUnit);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(alienUnit.getLeftConnector()));
            printAlienUnitCenter(alienUnit);
            System.out.print(rightConnectorToString(alienUnit.getRightConnector()) + "│");
        }
        else {
            printShipCard(alienUnit);
        }
    }

    public void printAlienUnitCenter(AlienUnit alienUnit) {
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



    public void printBattery(Battery battery) {
        setColor(battery);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(battery.getLeftConnector()));
            printBatteryCenter(battery);
            System.out.print(rightConnectorToString(battery.getRightConnector()) + "│");
        }
        else {
            printShipCard(battery);
        }
    }

    public void printBatteryCenter(Battery battery) {
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



    public void printCannon(Cannon cannon) {
        setColor(cannon);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(cannon.getLeftConnector()));
            printCannonCenter(cannon);
            System.out.print(rightConnectorToString(cannon.getRightConnector()) + "│");
        }
        else {
            printShipCard(cannon);
        }
    }

    public void printCannonCenter(Cannon cannon) {
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
            if (cannon.getOrientation().equals(ShipCard.Orientation.DEG_90)){
                if (cannon.getType().equals(Cannon.Type.SINGLE)){
                    System.out.print(" CANNON >");
                }
                else {
                    System.out.print(" CANNON  ");
                }
            }
            else if (cannon.getOrientation().equals(ShipCard.Orientation.DEG_270)){
                if (cannon.getType().equals(Cannon.Type.SINGLE)){
                    System.out.print("< CANNON ");
                }
                else {
                    System.out.print("  CANNON ");
                }
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



    public void printEngine(Engine engine) {
        setColor(engine);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(engine.getLeftConnector()));
            printEngineCenter(engine);
            System.out.print(rightConnectorToString(engine.getRightConnector()) + "│");
        }
        else {
            printShipCard(engine);
        }
    }

    public void printEngineCenter(Engine engine) {
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
            if (engine.getOrientation().equals(ShipCard.Orientation.DEG_90)){
                if (engine.getType().equals(Engine.Type.SINGLE)){
                    System.out.print("< ENGINE ");
                }
                else {
                    System.out.print("  ENGINE ");
                }
            }
            else if (engine.getOrientation().equals(ShipCard.Orientation.DEG_270)){
                if (engine.getType().equals(Engine.Type.SINGLE)){
                    System.out.print(" ENGINE >");
                }
                else {
                    System.out.print(" ENGINE  ");
                }
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



    public void printHousingUnit(HousingUnit housingUnit) {
        setColor(housingUnit);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(housingUnit.getLeftConnector()));
            printHousingUnitCenter(housingUnit);
            System.out.print(rightConnectorToString(housingUnit.getRightConnector()) + "│");
        }
        else {
            printShipCard(housingUnit);
        }
    }

    public void printHousingUnitCenter(HousingUnit housingUnit) {
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



    public void printShield(Shield shield) {
        setColor(shield);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(shield.getLeftConnector()));
            printShieldCenter(shield);
            System.out.print(rightConnectorToString(shield.getRightConnector()) + "│");
        }
        else {
            printShipCard(shield);
        }
    }

    public void printShieldCenter(Shield shield) {
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



    public void printStorage(Storage storage) {
        setColor(storage);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(storage.getLeftConnector()));
            printStorageCenter(storage);
            System.out.print(rightConnectorToString(storage.getRightConnector()) + "│");
        }
        else {
            printShipCard(storage);
        }
    }

    public void printStorageCenter(Storage storage) {
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



    public void printStructuralModule(StructuralModule structuralModule) {
        setColor(structuralModule);

        if (i >= (cardLength/2 - 1) && i <= (cardLength/2 + 1)){
            System.out.print("│" + leftConnectorToString(structuralModule.getLeftConnector()));
            printStructuralModuleCenter(structuralModule);
            System.out.print(rightConnectorToString(structuralModule.getRightConnector()) + "│");
        }
        else {
            printShipCard(structuralModule);
        }
    }

    public void printStructuralModuleCenter(StructuralModule structuralModule) {
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



    public void printEmptyShipCard() {
        System.out.print(Ansi.ansi().reset());

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



    public void printCovered() {
        System.out.print(Ansi.ansi().reset());

        if (i == 1 || i == (cardLength - 2)) {
            System.out.print("│ *         * │");
        }
        else if (i == cardLength/2){
            System.out.print("│   COVERED   │");
        }
        else {
            printEmptyShipCard();
        }
    }
}

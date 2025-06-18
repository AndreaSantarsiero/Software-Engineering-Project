package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import java.util.List;
import java.util.Map;



public abstract class CLITemplate {


    protected final ShipCardCLI shipCardCLI;
    protected final ShipBoardCLI shipBoardCLI;
    protected final AdventureCardCLI adventureCardCLI;
    protected static final List<String> pressEnterToContinue = List.of("┌─┐┬─┐┌─┐┌─┐┌─┐  ┌─┐┌┐┌┌┬┐┌─┐┬─┐  ┌┬┐┌─┐  ┌─┐┌─┐┌┐┌┌┬┐┬┌┐┌┬ ┬┌─┐         ",
                                                                       "├─┘├┬┘├┤ └─┐└─┐  ├┤ │││ │ ├┤ ├┬┘   │ │ │  │  │ ││││ │ │││││ │├┤          ",
                                                                       "┴  ┴└─└─┘└─┘└─┘  └─┘┘└┘ ┴ └─┘┴└─   ┴ └─┘  └─┘└─┘┘└┘ ┴ ┴┘└┘└─┘└─┘  o  o  o");



    public CLITemplate() {
        shipCardCLI = new ShipCardCLI();
        shipBoardCLI = new ShipBoardCLI(shipCardCLI);
        adventureCardCLI = new AdventureCardCLI();
    }



    protected void clearView() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }



    protected void renderMenu(String title, List<String> options, int selected) {
        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                AttributedString highlighted = new AttributedString(
                        "  > " + options.get(i),
                        AttributedStyle.DEFAULT.background(235)
                );
                System.out.println(highlighted.toAnsi());
            } else {
                System.out.println("    " + options.get(i));
            }
        }
    }



    protected int renderMultiLevelMenu(List<List<String>> options, int i, int j, int selected) {
        if (i == selected) {
            AttributedString highlighted = new AttributedString(
                    "   " + options.get(i).get(j) + "   ",
                    AttributedStyle.DEFAULT.background(235)
            );
            System.out.print(highlighted.toAnsi());
        } else {
            System.out.print("   " + options.get(i).get(j) + "   ");
        }

        return (options.get(i).get(j).length() + 6);
    }



    protected void renderIntegerChoice(String label, int value) {
        String line = label + ": " + value;
        AttributedString highlighted = new AttributedString(line, AttributedStyle.DEFAULT.background(235));
        System.out.print(highlighted.toAnsi());
    }



    public void printEmptyShipLine(ShipBoard shipBoard){
        System.out.print("   ");
        for (int x = 0; x < shipBoard.getWidth(); x++) {
            if(x < shipBoard.getWidth()){
                shipBoardCLI.printInvalidSquare();
            }
        }
        System.out.print("         ");
    }



    public void printMenu(ShipBoard shipBoard, int menuIndex, List<List<String>> options, int selected){
        if(menuIndex < options.size()*options.getFirst().size()) {
            int spacesUsed = renderMultiLevelMenu(options, menuIndex / options.getFirst().size(), menuIndex % options.getFirst().size(), selected);
            printMenuLeftSpaces(shipBoard, spacesUsed);

        }
        else {
            printEmptyShipLine(shipBoard);
        }
    }

    public void printMenuLeftSpaces(ShipBoard shipBoard, int spacesUsed){
        int singleSpacesLeft = 15 - ((spacesUsed - 3) % 15);
        int invalidCardsLeft = shipBoard.getWidth() - (spacesUsed - 3)/15 - 1;
        for (int x = 0; x < singleSpacesLeft; x++) {
            System.out.print(" ");
        }
        for (int x = 0; x < invalidCardsLeft; x++) {
            shipBoardCLI.printInvalidSquare();
        }
        System.out.print("         ");
    }



    public void printEnemiesShipBoard(Map<String, ShipBoard> enemiesShipBoard){
        for (Map.Entry<String, ShipBoard> entry : enemiesShipBoard.entrySet()) {
            System.out.println(entry.getKey() + "'S SHIP:");
            shipBoardCLI.printFullShip(entry.getValue());
        }
    }



    public void renderMultiLevelString(String input, int i){
        StringBuilder output = new StringBuilder();

        for (int j = i; j < input.length(); j++){
            output.append(getAsciiArt(input.charAt(j), i));
        }

        System.out.print(output);
    }



    public String getAsciiArt(char ch, int i) {
        String[] lines = {
                "┌─┐┌┐ ┌─┐┌┬┐┌─┐┌─┐┌─┐┬ ┬┬ ┬┬┌─┬  ┌┬┐┌┐┌┌─┐┌─┐┌─┐ ┬─┐┌─┐┌┬┐┬ ┬┬  ┬┬ ┬─┐ ┬┬ ┬┌─┐  ╔═╗╔╗ ╔═╗╔╦╗╔═╗╔═╗╔═╗╦ ╦╦ ╦╦╔═╦  ╔╦╗╔╗╔╔═╗╔═╗╔═╗ ╦═╗╔═╗╔╦╗╦ ╦╦  ╦╦ ╦═╗ ╦╦ ╦╔═╗  ┌─┐┬      ",
                "├─┤├┴┐│   ││├┤ ├┤ │ ┬├─┤│ │├┴┐│  │││││││ │├─┘│─┼┐├┬┘└─┐ │ │ │└┐┌┘│││┌┴┬┘└┬┘┌─┘  ╠═╣╠╩╗║   ║║║╣ ╠╣ ║ ╦╠═╣║ ║╠╩╗║  ║║║║║║║ ║╠═╝║═╬╗╠╦╝╚═╗ ║ ║ ║╚╗╔╝║║║╔╩╦╝╚╦╝╔═╝ o ┌┘│ ─    ",
                "┴ ┴└─┘└─┘─┴┘└─┘└  └─┘┴ ┴┴└┘┴ ┴┴─┘┴ ┴┘└┘└─┘┴  └─┘└┴└─└─┘ ┴ └─┘ └┘ └┴┘┴ └─ ┴ └─┘  ╩ ╩╚═╝╚═╝═╩╝╚═╝╚  ╚═╝╩ ╩╩╚╝╩ ╩╩═╝╩ ╩╝╚╝╚═╝╩  ╚═╝╚╩╚═╚═╝ ╩ ╚═╝ ╚╝ ╚╩╝╩ ╚═ ╩ ╚═╝oo o o   ───"
        };

        String alphabet = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ.:?!-_";
        int index = alphabet.indexOf(ch);
        if (index == -1) return String.valueOf(ch); // default: return char itself
        int lenght = getAsciiArtCharLength(ch);
        int start = 0;
        for (int j = 0; j < alphabet.length(); j++) {
            start += getAsciiArtCharLength(alphabet.charAt(j));
        }

        StringBuilder result = new StringBuilder();
        int end = start + lenght;
        result.append(lines[i], start, end);

        return result.toString();
    }

    public int getAsciiArtCharLength(char ch){
        return switch (Character.toLowerCase(ch)) {
            case 'q', 'x', 'v' -> 4;
            case '.', ':', '!' -> 1;
            case ' ' -> 2;
            default -> 3;
        };
    }
}

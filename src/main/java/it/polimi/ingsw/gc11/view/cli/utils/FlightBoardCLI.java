package it.polimi.ingsw.gc11.view.cli.utils;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class FlightBoardCLI {

    public static void print(FlightBoard flightBoard, List<Player> players, int i) {
        switch (flightBoard.getType()){
            case FlightBoard.Type.TRIAL -> printLevel1(players, i);
            case FlightBoard.Type.LEVEL2 -> printLevel2(players, i);
            default -> System.out.println("Error: Unknown FlightBoard type");
        }
    }



    public static void printLevel1(List<Player> players, int i) {
        String boardColor = Ansi.ansi().reset().fg(Ansi.Color.BLUE).toString();

        switch (i) {
            case 0 -> System.out.print(boardColor + "          " + printSingleCell(players, ">", 1, boardColor) + "  " + printSingleCell(players, ">", 2, boardColor) + "  " + printSingleCell(players, ">", 3, boardColor) + "  " + printSingleCell(players, ">", 4, boardColor) + "          ");
            case 1 -> System.out.print(boardColor + "      " + printSingleCell(players, ">", 0, boardColor) + "                    " + printSingleCell(players, ">", 5, boardColor) + "      ");
            case 2 -> System.out.print(boardColor + "   " + printSingleCell(players, ">", 17, boardColor) + "                          " + printSingleCell(players, ">", 6, boardColor) + "   ");
            case 3 -> System.out.print(boardColor + "                                      \n");
            case 4 -> System.out.print(boardColor + " " + printSingleCell(players, "∧", 16, boardColor) + "                              " + printSingleCell(players, "∨", 7, boardColor) + " ");
            case 5 -> System.out.print(boardColor + "                                      \n");
            case 6 -> System.out.print(boardColor + "   " + printSingleCell(players, "<", 15, boardColor) + "                          " + printSingleCell(players, "<", 8, boardColor) + "   ");
            case 7 -> System.out.print(boardColor + "      " + printSingleCell(players, "<", 14, boardColor) + "                    " + printSingleCell(players, "<", 9, boardColor) + "      ");
            case 8 -> System.out.print(boardColor + "          " + printSingleCell(players, "<", 13, boardColor) + "  " + printSingleCell(players, "<", 12, boardColor) + "  " + printSingleCell(players, "<", 11, boardColor) + "  " + printSingleCell(players, "<", 10, boardColor) + "          ");
        }

        System.out.print(Ansi.ansi().reset());
    }



    public static void printLevel2(List<Player> players, int i) {
        String boardColor = Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).toString();

        switch (i) {
            case  0 -> System.out.print(boardColor + "               " + printSingleCell(players, ">", 2, boardColor) + "  " + printSingleCell(players, ">", 3, boardColor) + "  " + printSingleCell(players, ">", 4, boardColor) + "  " + printSingleCell(players, ">", 5, boardColor) + "  " + printSingleCell(players, ">", 6, boardColor) + "               ");
            case  1 -> System.out.print(boardColor + "          " + printSingleCell(players, ">", 1, boardColor) + "                           " + printSingleCell(players, ">", 7, boardColor) + "          ");
            case  2 -> System.out.print(boardColor + "      " + printSingleCell(players, ">", 0, boardColor) + "                                   " + printSingleCell(players, ">", 8, boardColor) + "      ");
            case  3 -> System.out.print(boardColor + "                                                     ");
            case  4 -> System.out.print(boardColor + "   " + printSingleCell(players, "∧", 23, boardColor) + "                                         " + printSingleCell(players, "∨", 9, boardColor) + "   ");
            case  5 -> System.out.print(boardColor + "                                                     ");
            case  6 -> System.out.print(boardColor + " " + printSingleCell(players, "∧", 22, boardColor) + "                                             " + printSingleCell(players, "∨", 10, boardColor) + " ");
            case  7 -> System.out.print(boardColor + "                                                     ");
            case  8 -> System.out.print(boardColor + "   " + printSingleCell(players, "∧", 21, boardColor) + "                                         " + printSingleCell(players, "∨", 11, boardColor) + "   ");
            case  9 -> System.out.print(boardColor + "                                                     ");
            case 10 -> System.out.print(boardColor + "      " + printSingleCell(players, "<", 20, boardColor) + "                                   " + printSingleCell(players, "<", 12, boardColor) + "      ");
            case 11 -> System.out.print(boardColor + "          " + printSingleCell(players, "<", 19, boardColor) + "                           " + printSingleCell(players, "<", 13, boardColor) + "          ");
            case 12 -> System.out.print(boardColor + "               " + printSingleCell(players, "<", 18, boardColor) + "  " + printSingleCell(players, "<", 17, boardColor) + "  " + printSingleCell(players, "<", 16, boardColor) + "  " + printSingleCell(players, "<", 15, boardColor) + "  " + printSingleCell(players, "<", 14, boardColor) + "               ");
        }

        System.out.print(Ansi.ansi().reset());
    }



    public static void printLevel3(List<Player> players) {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED));
        System.out.println("                ( )  ( )  ( )  ( )  ( )  ( )  ( )  ( )                ");
        System.out.println("           ( )                                          ( )           ");
        System.out.println("       ( )                                                  ( )       ");
        System.out.println("                                                                      ");
        System.out.println("    ( )                                                        ( )    ");
        System.out.println("                                                                      ");
        System.out.println("  ( )                                                            ( )  ");
        System.out.println("                                                                      ");
        System.out.println(" ( )                                                              ( ) ");
        System.out.println("                                                                      ");
        System.out.println("  ( )                                                            ( )  ");
        System.out.println("                                                                      ");
        System.out.println("    ( )                                                        ( )    ");
        System.out.println("                                                                      ");
        System.out.println("       ( )                                                  ( )       ");
        System.out.println("           ( )                                          ( )           ");
        System.out.println("                ( )  ( )  ( )  ( )  ( )  ( )  ( )  ( )                ");

        System.out.print(Ansi.ansi().reset());
    }



    private static String printSingleCell(List<Player> players, String ship, int position, String boardColor) {
        StringBuilder result = new StringBuilder();
        boolean match = false;
        result.append("(");

        for(Player player : players) {
            if(player.getPosition() == position) {
                result.append(getPlayerColor(player.getColor()));
                result.append(ship).append(boardColor);
                match = true;
            }
        }

        if(!match) {
            result.append(" ");
        }
        result.append(")");
        return result.toString();
    }



    private static String getPlayerColor(Player.Color color) {
        return switch (color) {
            case BLUE ->  Ansi.ansi().reset().fg(Ansi.Color.BLUE).toString();
            case GREEN -> Ansi.ansi().reset().fg(Ansi.Color.GREEN).toString();
            case RED -> Ansi.ansi().reset().fg(Ansi.Color.RED).toString();
            case YELLOW -> Ansi.ansi().reset().fg(Ansi.Color.YELLOW).toString();
        };
    }
}

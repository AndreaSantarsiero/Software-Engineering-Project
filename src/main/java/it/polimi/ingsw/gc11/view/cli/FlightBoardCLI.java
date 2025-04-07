package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.FlightBoard;
import org.fusesource.jansi.Ansi;


public class FlightBoardCLI {

    public static void print(FlightBoard flightBoard) {
        switch (flightBoard.getType()){
            case FlightBoard.Type.TRIAL -> printLevel1();
            case FlightBoard.Type.LEVEL2 -> printLevel2();
            default -> System.out.println("Error: Unknown FlightBoard type");
        }
    }



    public static void printLevel1() {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE));
        System.out.print("             ( )  (");
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED) + ">");
        System.out.println(Ansi.ansi().reset().fg(Ansi.Color.BLUE) + ")  ( )  ( )             ");
        System.out.println("        ( )                      ( )        ");
        System.out.println("    ( )                              ( )    ");
        System.out.println("                                            ");
        System.out.println(" ( )                                    ( ) ");
        System.out.println("                                            ");
        System.out.println("    ( )                              ( )    ");
        System.out.println("        ( )                      ( )        ");
        System.out.println("             ( )  ( )  ( )  ( )             ");

        System.out.print(Ansi.ansi().reset());
    }



    public static void printLevel2() {
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA));
        System.out.print("               ( )  (");
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.GREEN) + ">");
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA) + ")  ( )  (");
        System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED) + ">");
        System.out.println(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA) + ")  ( )               ");
        System.out.println("          ( )                           ( )          ");
        System.out.println("      ( )                                   ( )      ");
        System.out.println("                                                     ");
        System.out.println("   ( )                                         ( )   ");
        System.out.println("                                                     ");
        System.out.println(" ( )                                             ( ) ");
        System.out.println("                                                     ");
        System.out.println("   ( )                                         ( )   ");
        System.out.println("                                                     ");
        System.out.println("      ( )                                   ( )      ");
        System.out.println("          ( )                           ( )          ");
        System.out.println("               ( )  ( )  ( )  ( )  ( )               ");

        System.out.print(Ansi.ansi().reset());
    }
}

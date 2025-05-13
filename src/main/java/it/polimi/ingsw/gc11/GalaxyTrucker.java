package it.polimi.ingsw.gc11;

import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.gui.MainGUI;



public class GalaxyTrucker {
    public static void main(String[] args) {
        boolean useCLI = false;
        boolean startServer = false;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("-cli")) {
                useCLI = true;
                break;
            }
            if (arg.equalsIgnoreCase("-server")) {
                startServer = true;
                break;
            }
        }

        if (useCLI) {
            MainCLI.run(args);
        }
        else if (startServer) {
            ServerMAIN.run(args);
        }
        else {
            MainGUI.launch(MainGUI.class, args);
        }
    }
}


package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.EndController;



public class EndTemplate extends CLITemplate {

    private EndController controller;



    public EndTemplate(EndController controller) {
        this.controller = controller;
    }



    public void render() {
        if (!controller.isActive()) {
            return;
        }

        EndPhaseData data = controller.getPhaseData();
        System.out.println("\n\n");
        System.out.println(" _______  _______  _        _______                     _________ _______           _______  _        _______  _______ ");
        System.out.println("(  ____ \\(  ___  )( \\      (  ___  )|\\     /||\\     /|  \\__   __/(  ____ )|\\     /|(  ____ \\| \\    /\\(  ____ \\(  ____ )");
        System.out.println("| (    \\/| (   ) || (      | (   ) |( \\   / )( \\   / )     ) (   | (    )|| )   ( || (    \\/|  \\  / /| (    \\/| (    )|");
        System.out.println("| |      | (___) || |      | (___) | \\ (_) /  \\ (_) /      | |   | (____)|| |   | || |      |  (_/ / | (__    | (____)|");
        System.out.println("| | ____ |  ___  || |      |  ___  |  ) _ (    \\   /       | |   |     __)| |   | || |      |   _ (  |  __)   |     __)");
        System.out.println("| | \\_  )| (   ) || |      | (   ) | / ( ) \\    ) (        | |   | (\\ (   | |   | || |      |  ( \\ \\ | (      | (\\ (   ");
        System.out.println("| (___) || )   ( || (____/\\| )   ( |( /   \\ )   | |        | |   | ) \\ \\__| (___) || (____/\\|  /  \\ \\| (____/\\| ) \\ \\__");
        System.out.println("(_______)|/     \\|(_______/|/     \\||/     \\|   \\_/        )_(   |/   \\__/(_______)(_______/|_/    \\/(_______/|/   \\__/");
        System.out.println("\n\n");
        System.out.println("Game over!");
    }
}

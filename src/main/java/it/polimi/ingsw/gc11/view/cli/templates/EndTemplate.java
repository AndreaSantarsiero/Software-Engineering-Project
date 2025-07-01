package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.EndController;
import java.util.Map;



public class EndTemplate extends CLITemplate {

    private final EndController controller;



    public EndTemplate(EndController controller) {
        this.controller = controller;
    }



    public void render() {
        if (!controller.isActive()) {
            return;
        }

        EndPhaseData data = controller.getPhaseData();
        Player player = data.getPlayer();
        Map<String, Player> enemies = data.getEnemies();


        clearView();
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
        System.out.println("\n\n");
        System.out.println("- Your score: " + player.getCoins());
        for (Map.Entry<String, Player> entry : enemies.entrySet()) {
            System.out.println("- " + entry.getKey() + "'s score: " + entry.getValue().getCoins());
        }
    }
}

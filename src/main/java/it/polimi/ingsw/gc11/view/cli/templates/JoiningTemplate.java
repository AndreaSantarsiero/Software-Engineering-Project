package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.*;
import it.polimi.ingsw.gc11.view.cli.InputHandler;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import java.util.List;



public class JoiningTemplate extends CLITemplate {

    private static final List<String> connectionTypes = List.of("Remote Method Invocation", "Socket");
    private static final List<String> gameOptions = List.of("create a new match", "join an existing match", "exit");
    private final InputHandler inputHandler;



    public JoiningTemplate() {
        inputHandler = new InputHandler();
    }



    @Override
    public void update (JoiningPhaseData joiningPhaseData) {
        render(joiningPhaseData);
    }



    public void render(JoiningPhaseData data) {
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


        renderMenu("Choose networking protocol (Use W/S or ↑/↓ to navigate, Enter to select):", connectionTypes, data.getConnectionTypeMenu());

        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_USERNAME.ordinal()) {
            if(data.getUsername() != null){
                System.out.println("Insert username: " + data.getUsername());
            }
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CREATE_OR_JOIN.ordinal()) {
            System.out.println("\n\n");
            renderMenu("Do you want to create a match or join an existing one?", gameOptions, data.getCreateOrJoinMenu());
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_GAME.ordinal()) {
            List<String> availableMatches = data.getAvailableMatches();
            System.out.println("\n\n");
            renderMenu("Available matches:", availableMatches, data.getExistingGameMenu());
        }


        if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_CONNECTION){
            inputHandler.interactiveMenu(data, connectionTypes, data.getConnectionTypeMenu());
        }
        else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_USERNAME){
            inputHandler.readLine(data, "Insert username: ");
        }
        else if(data.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
            inputHandler.interactiveMenu(data, gameOptions, data.getCreateOrJoinMenu());
        }
        else if (data.getState() == JoiningPhaseData.JoiningState.CHOOSE_GAME){
            List<String> availableMatches = data.getAvailableMatches();
            inputHandler.interactiveMenu(data, availableMatches, data.getExistingGameMenu());
        }
        else{
            inputHandler.interactiveMenu(data, List.of(""), 0); //waiting to start
        }
    }



    private void renderMenu(String title, List<String> options, int selected) {
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
}

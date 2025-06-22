package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.view.*;
import it.polimi.ingsw.gc11.view.cli.controllers.JoiningController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class JoiningTemplate extends CLITemplate {

    private final JoiningController controller;
    private static final List<String> connectionTypes = List.of("Remote Method Invocation", "Socket");
    private static final List<String> gameOptions = List.of("create a new match", "join an existing match", "exit");
    private static final List<String> gameLevels = List.of("Trial", "Level II");
    private static final List<String> colorOptions = List.of("blue", "green", "red", "yellow");



    public JoiningTemplate(JoiningController controller) {
        this.controller = controller;
    }



    public void render() {
        if (!controller.isActive()) {
            return;
        }

        JoiningPhaseData data = controller.getPhaseData();
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


        renderMenu("Choose networking protocol (Use W/S to navigate, Enter to select):", connectionTypes, controller.getConnectionTypeMenu());

        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_USERNAME.ordinal()) {
            if(controller.isUsernameApproved()) {
                System.out.println("Chosen username: " + data.getUsername());
            }
            else {
                String serverMessage = data.getServerMessage();
                if(serverMessage == null || serverMessage.isEmpty()) {
                    System.out.print("Insert username: ");
                }
                else {
                    System.out.print(serverMessage + ". Try again: ");
                    data.resetServerMessage();
                }
            }
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CREATE_OR_JOIN.ordinal()) {
            System.out.println("\n\n");
            if(!controller.isGameApproved()){
                String serverMessage = data.getServerMessage();
                if(serverMessage != null && !serverMessage.isEmpty()) {
                    System.out.println(serverMessage);
                    data.resetServerMessage();
                }
            }
            renderMenu("Do you want to create a match or join an existing one?", gameOptions, controller.getCreateOrJoinMenu());
        }
        if (controller.getCreateOrJoinMenu() == 0 && data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_LEVEL.ordinal()) {
            renderMenu("- Choose match difficulty", gameLevels, controller.getGameLevel());

            if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS.ordinal()){
                renderIntegerChoice("\n- Insert number of players", controller.getNumPlayers());
            }
        }
        if (controller.getCreateOrJoinMenu() == 1 && data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_GAME.ordinal()) {
            List<String> availableMatches = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : data.getAvailableMatches().entrySet()) {
                List<String> usernames = entry.getValue();
                String matchName = String.join(", ", usernames);
                availableMatches.add(matchName);
            }

            System.out.println("\n\n");

            if(availableMatches.isEmpty()) {
                controller.setNoAvailableMatches(true);
                System.out.println("No available matches found, press enter to continue...");
            }
            else {
                renderMenu("Available matches:", availableMatches, controller.getExistingGameMenu());
            }
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_COLOR.ordinal()) {
            System.out.println("\n\n");
            String serverMessage = data.getServerMessage();
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println(serverMessage);
                data.resetServerMessage();
            }
            renderMenu("Choose your color", colorOptions, controller.getChosenColorMenu());
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.WAITING.ordinal()) {
            System.out.println("\n\nWaiting for the match to start...");
        }
    }



    public int getConnectionTypesSize(){
        return connectionTypes.size();
    }

    public int getGameOptionsSize(){
        return gameOptions.size();
    }

    public int getGameLevelsSize(){
        return gameLevels.size();
    }

    public int getColorOptionsSize(){
        return colorOptions.size();
    }

    public String getChosenColor(int choice){
        return colorOptions.get(choice);
    }
}

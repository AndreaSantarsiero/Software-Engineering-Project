package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.*;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.IntegerInput;
import it.polimi.ingsw.gc11.view.cli.input.MenuInput;
import it.polimi.ingsw.gc11.view.cli.input.StringInput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class JoiningTemplate extends CLITemplate {

    private static final List<String> connectionTypes = List.of("Remote Method Invocation", "Socket");
    private static final List<String> gameOptions = List.of("create a new match", "join an existing match", "exit");
    private static final List<String> gameLevels = List.of("Trial", "Level II");
    private static final List<String> colorOptions = List.of("blue", "green", "red", "yellow");
    private boolean usernameApproved = false;
    private boolean noAvailableMatches = false;



    public JoiningTemplate(MainCLI mainCLI) {
        super(mainCLI);
    }



    @Override
    public void update (JoiningPhaseData joiningPhaseData) {
        if (active && joiningPhaseData.equals(mainCLI.getContext().getCurrentPhase())) {
            render(joiningPhaseData);
        }
    }

    @Override
    public void change(){
        active = false;
        mainCLI.changeTemplate(this);
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


        renderMenu("Choose networking protocol (Use W/S to navigate, Enter to select):", connectionTypes, data.getConnectionTypeMenu());

        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_USERNAME.ordinal()) {
            if(usernameApproved) {
                System.out.println("Chosen username: " + data.getUsername());
            }
            else {
                if(serverMessage == null || serverMessage.isEmpty()) {
                    System.out.print("Insert username: ");
                }
                else {
                    System.out.print(serverMessage + " Try again: ");
                    serverMessage = "";
                }
            }
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CREATE_OR_JOIN.ordinal()) {
            System.out.println("\n\n");
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println(serverMessage);
                serverMessage = "";
            }
            renderMenu("Do you want to create a match or join an existing one?", gameOptions, data.getCreateOrJoinMenu());
        }
        if (data.getCreateOrJoinMenu() == 0 && data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_LEVEL.ordinal()) {
            renderMenu("- Choose match difficulty", gameLevels, data.getGameLevel());

            if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS.ordinal()){
                renderIntegerChoice("\n- Insert number of players", data.getNumPlayers());
            }
        }
        if (data.getCreateOrJoinMenu() == 1 && data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_GAME.ordinal()) {
            try{
                data.setAvailableMatches(mainCLI.getVirtualServer().getAvailableMatches());
            } catch (NetworkException e) {
                System.out.println("Connection error: " + e.getMessage());
            }

            List<String> availableMatches = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : data.getAvailableMatches().entrySet()) {
                List<String> usernames = entry.getValue();
                String matchName = String.join(", ", usernames);
                availableMatches.add(matchName);
            }

            System.out.println("\n\n");

            if(availableMatches.isEmpty()) {
                noAvailableMatches = true;
                System.out.println("No available matches found, press enter to continue...");
            }
            else {
                renderMenu("Available matches:", availableMatches, data.getExistingGameMenu());
            }
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.CHOOSE_COLOR.ordinal()) {
            System.out.println("\n\n");
            if(serverMessage != null && !serverMessage.isEmpty()) {
                System.out.println(serverMessage);
                serverMessage = "";
            }
            renderMenu("Choose your color", colorOptions, data.getChosenColorMenu());
        }
        if (data.getState().ordinal() >= JoiningPhaseData.JoiningState.WAITING.ordinal()) {
            System.out.println("\n\nWaiting for the match to start...");
        }


        if(data.isStateNew()){
            addInputRequest(data);
        }
    }



    public void addInputRequest(JoiningPhaseData data) {
        try{
            if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_CONNECTION){
                mainCLI.addInputRequest(new MenuInput(data, connectionTypes.size(), data.getConnectionTypeMenu()));
            }
            else if (data.getState() == JoiningPhaseData.JoiningState.CONNECTION_SETUP){
                mainCLI.virtualServerSetup(data.getConnectionTypeMenu());
                data.updateState();
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_USERNAME){
                mainCLI.addInputRequest(new StringInput(data));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.USERNAME_SETUP){
                try{
                    mainCLI.getVirtualServer().registerSession(data.getUsername());
                    usernameApproved = true;
                    data.updateState();
                } catch (UsernameAlreadyTakenException | IllegalArgumentException e) {
                    data.setState(JoiningPhaseData.JoiningState.CHOOSE_USERNAME);
                    serverMessage = e.getMessage();
                }
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
                mainCLI.addInputRequest(new MenuInput(data, gameOptions.size(), data.getCreateOrJoinMenu()));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_LEVEL) {
                mainCLI.addInputRequest(new MenuInput(data, gameLevels.size(), data.getGameLevel()));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS) {
                mainCLI.addInputRequest(new IntegerInput(data, 2, 4, data.getNumPlayers()));
            }
            else if (data.getState() == JoiningPhaseData.JoiningState.CHOOSE_GAME){
                if(noAvailableMatches){
                    data.setState(JoiningPhaseData.JoiningState.CREATE_OR_JOIN);
                }
                else{
                    List<String> availableMatches = new ArrayList<>(data.getAvailableMatches().keySet());
                    mainCLI.addInputRequest(new MenuInput(data, availableMatches.size(), data.getExistingGameMenu()));
                }
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.GAME_SETUP) {
                try{
                    if(data.getCreateOrJoinMenu() == 0){
                        mainCLI.getVirtualServer().createMatch(flightLevelSetup(data.getGameLevel()), data.getNumPlayers());
                    }
                    else {
                        mainCLI.getVirtualServer().connectToGame(new ArrayList<>(data.getAvailableMatches().keySet()).get(data.getExistingGameMenu()));
                    }
                    data.updateState();
                }
                catch (FullLobbyException e) {
                    data.setState(JoiningPhaseData.JoiningState.CREATE_OR_JOIN);
                    serverMessage = e.getMessage();
                }
                catch (UsernameAlreadyTakenException | IllegalArgumentException e) {
                    data.setState(JoiningPhaseData.JoiningState.CHOOSE_USERNAME);
                    serverMessage = e.getMessage();
                }
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_COLOR) {
                mainCLI.addInputRequest(new MenuInput(data, colorOptions.size(), data.getChosenColorMenu()));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.COLOR_SETUP) {
                mainCLI.getVirtualServer().chooseColor(colorOptions.get(data.getChosenColorMenu()));
                data.updateState();
            }
        } catch (NetworkException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }



    public FlightBoard.Type flightLevelSetup(int choice) {
        if (choice == 0) {
            return FlightBoard.Type.TRIAL;
        }
        else {
            return FlightBoard.Type.LEVEL2;
        }
    }
}

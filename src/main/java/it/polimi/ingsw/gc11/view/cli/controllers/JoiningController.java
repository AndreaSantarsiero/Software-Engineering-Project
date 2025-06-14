package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.cli.MainCLI;
import it.polimi.ingsw.gc11.view.cli.input.IntegerInput;
import it.polimi.ingsw.gc11.view.cli.input.MenuInput;
import it.polimi.ingsw.gc11.view.cli.input.StringInput;
import it.polimi.ingsw.gc11.view.cli.templates.JoiningTemplate;
import java.util.ArrayList;
import java.util.List;



public class JoiningController extends CLIController {

    private final JoiningTemplate template;
    private final JoiningPhaseData data;
    private int connectionTypeMenu = 0;
    private int createOrJoinMenu = 0;
    private int gameLevel = 0;
    private int numPlayers = 2;
    private int existingGameMenu = 0;
    private int chosenColorMenu = 0;
    private boolean usernameApproved = false;
    private boolean noAvailableMatches = false;
    private boolean gameApproved = false;



    public JoiningController(MainCLI mainCLI, JoiningPhaseData data) {
        super(mainCLI);
        this.data = data;
        this.template = new JoiningTemplate(this);
        update(data);
    }

    public JoiningPhaseData getPhaseData(){
        return data;
    }



    @Override
    public void change(){
        active = false;
        mainCLI.changeController(this);
    }



    @Override
    public void update (JoiningPhaseData data) {
        if (!active) {
            return;
        }
        if(data.isStateNew()){
            if(addServerRequest()){
                return;
            }
        }
        template.render();
        if(data.isStateNew()){
            addInputRequest();
        }
    }


    //if it's not necessary to render the template, then return true
    public boolean addServerRequest(){
        return false;
    }



    public void addInputRequest() {
        try{
            if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_CONNECTION){
                mainCLI.addInputRequest(new MenuInput(data, this, template.getConnectionTypesSize(), connectionTypeMenu));
            }
            else if (data.getState() == JoiningPhaseData.JoiningState.CONNECTION_SETUP){
                mainCLI.virtualServerSetup(data, connectionTypeMenu);
                updateInternalState();
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_USERNAME){
                mainCLI.addInputRequest(new StringInput(data, this));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.USERNAME_SETUP){
                mainCLI.getVirtualServer().registerSession(data.getUsername());
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
                usernameApproved = true;
                mainCLI.getVirtualServer().getAvailableMatches();
                mainCLI.addInputRequest(new MenuInput(data, this, template.getGameOptionsSize(), createOrJoinMenu));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_LEVEL) {
                mainCLI.addInputRequest(new MenuInput(data, this, template.getGameLevelsSize(), gameLevel));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS) {
                mainCLI.addInputRequest(new IntegerInput(data, this, 2, 4, numPlayers));
            }
            else if (data.getState() == JoiningPhaseData.JoiningState.CHOOSE_GAME){
                if(noAvailableMatches){
                    data.setState(JoiningPhaseData.JoiningState.CREATE_OR_JOIN);
                }
                else{
                    List<String> availableMatches = new ArrayList<>(data.getAvailableMatches().keySet());
                    mainCLI.addInputRequest(new MenuInput(data, this, availableMatches.size(), existingGameMenu));
                }
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.GAME_SETUP) {
                if(createOrJoinMenu == 0){
                    mainCLI.getVirtualServer().createMatch(flightLevelSetup(gameLevel), numPlayers);
                }
                else {
                    mainCLI.getVirtualServer().connectToGame(new ArrayList<>(data.getAvailableMatches().keySet()).get(existingGameMenu));
                }
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_COLOR) {
                gameApproved = true;
                mainCLI.addInputRequest(new MenuInput(data, this, template.getColorOptionsSize(), chosenColorMenu));
            }
            else if(data.getState() == JoiningPhaseData.JoiningState.COLOR_SETUP) {
                mainCLI.getVirtualServer().chooseColor(template.getChosenColor(chosenColorMenu));
            }
        } catch (NetworkException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }



    public void updateInternalState() {
        if(data.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
            if(createOrJoinMenu == 0) {
                data.setState(JoiningPhaseData.JoiningState.CHOOSE_LEVEL);
            }
            else {
                data.setState(JoiningPhaseData.JoiningState.CHOOSE_GAME);
            }
        }
        else {
            data.updateState();
        }
    }



    @Override
    public void setMenuChoice(int choice){
        data.actualizePreviousState();
        switch (data.getState()) {
            case CHOOSE_CONNECTION -> setConnectionTypeMenu(choice);
            case CREATE_OR_JOIN -> setCreateOrJoinMenu(choice);
            case CHOOSE_LEVEL -> setGameLevel(choice);
            case CHOOSE_GAME -> setExistingGameMenu(choice);
            case CHOOSE_COLOR -> setChosenColorMenu(choice);
            case null, default -> {
            }
        }
    }

    @Override
    public void confirmMenuChoice(){
        updateInternalState();
    }

    @Override
    public void setStringInput(String input) {
        data.setUsername(input);
        updateInternalState();
    }

    @Override
    public void setIntegerChoice(int choice) {
        data.actualizePreviousState();

        if(data.getState() == JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS){
            setNumPlayers(choice);
        }
    }

    @Override
    public void confirmIntegerChoice() {
        updateInternalState();
    }



    public int getConnectionTypeMenu() {
        return connectionTypeMenu;
    }

    public void setConnectionTypeMenu(int connectionTypeMenu) {
        this.connectionTypeMenu = connectionTypeMenu;
        update(data);
    }

    public int getCreateOrJoinMenu() {
        return createOrJoinMenu;
    }

    public void setCreateOrJoinMenu(int createOrJoinMenu) {
        this.createOrJoinMenu = createOrJoinMenu;
        update(data);
    }

    public int getGameLevel(){
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
        update(data);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
        update(data);
    }

    public int getExistingGameMenu() {
        return existingGameMenu;
    }

    public void setExistingGameMenu(int existingGameMenu) {
        this.existingGameMenu = existingGameMenu;
        update(data);
    }

    public int getChosenColorMenu() {
        return chosenColorMenu;
    }

    public void setChosenColorMenu(int chosenColorMenu) {
        this.chosenColorMenu = chosenColorMenu;
        update(data);
    }

    public boolean isUsernameApproved(){
        return usernameApproved;
    }

    public void setUsernameApproved(boolean usernameApproved) {
        this.usernameApproved = usernameApproved;
    }

    public boolean isNoAvailableMatches() {
        return noAvailableMatches;
    }

    public void setNoAvailableMatches(boolean noAvailableMatches) {
        this.noAvailableMatches = noAvailableMatches;
    }

    public boolean isGameApproved() {
        return gameApproved;
    }

    public void setGameApproved(boolean gameApproved) {
        this.gameApproved = gameApproved;
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

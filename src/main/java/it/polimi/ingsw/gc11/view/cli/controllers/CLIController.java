package it.polimi.ingsw.gc11.view.cli.controllers;

import it.polimi.ingsw.gc11.view.*;
import it.polimi.ingsw.gc11.view.cli.MainCLI;



public abstract class CLIController extends Controller {

    protected final MainCLI mainCLI;
    protected boolean active = true;



    public CLIController(MainCLI mainCLI) {
        this.mainCLI = mainCLI;
    }



    public abstract void setMenuChoice(int choice);

    public abstract void confirmMenuChoice();

    public abstract void setStringInput(String input);

    public abstract void setIntegerChoice(int choice);

    public abstract void confirmIntegerChoice();

    public void setCoordinatesChoice(int j, int i) {}

    public void confirmCoordinatesChoice(){}



    public abstract void change();
}

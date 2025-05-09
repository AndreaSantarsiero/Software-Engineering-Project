package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;
import java.io.Serializable;



public abstract class AdventureCard implements Serializable {

    public enum Type {
        TRIAL, LEVEL1, LEVEL2;
    }

    private Type type;
    private boolean used;

    public AdventureCard(Type type) {
        this.type = type;
        this.used = false;
    }

    public void useCard(){
        this.used = true;
    }

    public Type getType(){
        return type;
    }

    public boolean isUsed(){
        return used;
    }

    public abstract AdventureState getInitialState(AdventurePhase advContext);

    public abstract void print(AdventureCardCLI adventureCardCLI, int i);
}

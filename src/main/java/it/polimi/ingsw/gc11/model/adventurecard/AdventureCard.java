package it.polimi.ingsw.gc11.model.adventurecard;

public class AdventureCard {

    public enum Type {
        TRIAL, LEVEL1, LEVEL2;
    }

    private Type type;
    private Boolean used;


    public Type getType(){
        return type;
    }
    public Boolean isUsed(){
        return used;
    }
}

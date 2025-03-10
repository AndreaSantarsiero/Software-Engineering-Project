package it.polimi.ingsw.gc11.model.adventurecard;

public abstract class AdventureCard {

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
}

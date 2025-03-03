package it.polimi.ingsw.gc11.model.shipcard;

public class AlienUnit extends ShipCard {

    public enum Type{
        BROWN, PURPLE;
    }

    private Type type;
    private Boolean presence;


    public Type getType() {
        return type;
    }
    public Boolean isPresent(){
        return presence;
    }
    public void killAlien(){
        this.presence = false;
    }
}

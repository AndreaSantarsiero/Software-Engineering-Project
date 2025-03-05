package it.polimi.ingsw.gc11.model.shipcard;

public class AlienUnit extends ShipCard {

    public enum Type{
        BROWN, PURPLE;
    }

    private Type type;
    private Boolean presence;


    public AlienUnit(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
        presence = false;
    }


    public Type getType() {
        return type;
    }
    public Boolean isPresent(){
        return presence;
    }
    public void killAlien(){
        if (!presence) {
            throw new AlienAlreadyKilledException("Alien already killed");
        }
        this.presence = false;
    }
}

package it.polimi.ingsw.gc11.model.shipcard;

public abstract class ShipCard {

    public enum Connector {
        NONE, SINGLE, DOUBLE, UNIVERSAL;
    }
    public enum Orientation {
        DEG_0, DEG_90, DEG_180, DEG_270;
    }

    private Connector topConnector;
    private Connector rightConnector;
    private Connector bottomConnector;
    private Connector leftConnector;
    private Orientation orientation;

    private int coordX;
    private int coordY;
    private Boolean covered;
    private Boolean reserved;
    private Boolean scrap;
}

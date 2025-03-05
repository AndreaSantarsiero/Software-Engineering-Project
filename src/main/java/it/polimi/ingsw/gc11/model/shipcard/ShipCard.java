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

    private Boolean covered;
    private Boolean scrap;


    public ShipCard(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector) {
        this.topConnector = topConnector;
        this.rightConnector = rightConnector;
        this.bottomConnector = bottomConnector;
        this.leftConnector = leftConnector;
        this.orientation = Orientation.DEG_0;
        this.covered = true;
        this.scrap = false;
    }


    public Connector getTopConnector() {
        return topConnector;
    }
    public Connector getRightConnector() {
        return rightConnector;
    }
    public Connector getBottomConnector() {
        return bottomConnector;
    }
    public Connector getLeftConnector() {
        return leftConnector;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    public Boolean isCovered() {
        return covered;
    }
    public void setCovered(Boolean covered) {
        this.covered = covered;
    }
    public Boolean isScrap() {
        return scrap;
    }
    public void setScrap(Boolean scrap) {
        this.scrap = scrap;
    }
}

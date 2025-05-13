package it.polimi.ingsw.gc11.model;



public class FlightBoard {

    public enum Type {
        TRIAL, LEVEL2;
    }

    private Type type;
    private int length;


    public FlightBoard(Type type) {
        this.type = type;
        if (type.equals(Type.TRIAL)) {
            length = 18;
        } else if (type.equals(Type.LEVEL2)) {
            length = 24;
        }
    }


    public Type getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void initializePosition(Type type, int pos, Player player) {
        switch (type) {
            case TRIAL -> {
                 switch (pos) {
                    case 1 -> player.setPosition(4);
                    case 2 -> player.setPosition(2);
                    case 3 -> player.setPosition(1);
                    case 4 -> player.setPosition(0);
                    default -> throw new IllegalArgumentException("The position you are trying to set is illegal");
                }
                return;
            }
            case LEVEL2 -> {
                switch (pos) {
                    case 1 -> player.setPosition(6);
                    case 2 -> player.setPosition(3);
                    case 3 -> player.setPosition(1);
                    case 4 -> player.setPosition(0);
                    default -> throw new IllegalArgumentException("The position you are trying to set is illegal");
                }
                return;
            }
        }
        throw new IllegalArgumentException("The fligthBoard you are trying to access is illegal");
    }

}

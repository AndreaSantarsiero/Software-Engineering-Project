package it.polimi.ingsw.gc11.model.shipboard;


/**
 * Represents a level 1 shipboard, extending the general ShipBoard class
 * This class defines the board's dimensions and provides a method to validate coordinates
 */
public class Level1ShipBoard extends ShipBoard {

    /**
     * Maximum X coordinate of the shipboard
     */
    private final int X_MAX = 7;

    /**
     * Maximum Y coordinate of the shipboard
     */
    private final int Y_MAX = 5;


    /**
     * Constructs a Level1ShipBoard with predefined dimensions (7x5)
     */
    public Level1ShipBoard() {
        super(7, 5);
    }


    /**
     * Validates whether the given coordinates are within the allowed area of the shipboard
     *
     * @param x The X coordinate to validate
     * @param y The Y coordinate to validate
     * @return true if the coordinates are valid, false otherwise
     * @throws IllegalArgumentException if the coordinates are outside the board's bounds
     */
    public Boolean validateCoordinates(int x, int y){
        x -= 5;
        y -= 4;

        if (x < 0 || y < 0 || x >= X_MAX || y >= Y_MAX) {
            throw new IllegalArgumentException("Coordinates out of the board's bounds");
        }

        if(y == 0 && x != 3){
            return false;
        }
        else if(y == 1 && !(x ==2 || x == 3 || x == 4)){
            return false;
        }
        else if(y == 4 && x == 3){
            return false;
        }
        else if(x == 0 || x == 6){
            return false;
        }
        else{
            return true;
        }
    }
}

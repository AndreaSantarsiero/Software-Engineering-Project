package it.polimi.ingsw.gc11.model.shipboard;


/**
 * Represents a level 2 shipboard, extending the general ShipBoard class
 * This class defines the board's dimensions and provides a method to validate coordinates
 */
public class Level2ShipBoard extends ShipBoard {

    /**
     * Constructs a Level1ShipBoard with predefined dimensions (7x5)
     */
    public Level2ShipBoard() {
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
    public boolean validateCoordinates(int x, int y){
        x -= 4;
        y -= 5;

        if (x < 0 || y < 0 || x >= 7 || y >= 5) {
            throw new IllegalArgumentException("Coordinates out of the board");
        }

        if(y == 0 && (x == 0 || x == 1 || x == 3 || x == 5 || x == 6)){
            return false;
        }
        else if(y == 1 && (x == 0 || x == 6)){
            return false;
        }
        else if(y == 4 && x == 3){
            return false;
        }
        else{
            return true;
        }
    }
}

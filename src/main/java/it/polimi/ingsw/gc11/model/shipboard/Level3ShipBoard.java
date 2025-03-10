package it.polimi.ingsw.gc11.model.shipboard;


/**
 * Represents a level 3 shipboard, extending the general ShipBoard class
 * This class defines the board's dimensions and provides a method to validate coordinates
 */
public class Level3ShipBoard extends ShipBoard {

    /**
     * Constructs a Level1ShipBoard with predefined dimensions (9x6)
     */
    public Level3ShipBoard() {
        super(9, 6);
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
        x -= 3;
        y -= 4;

        if (x < 0 || y < 0 || x >= 9 || y >= 6) {
            throw new IllegalArgumentException("Coordinates out of the board");
        }

        if(y == 0 && x != 4){
            return false;
        }
        else if(y == 1 && !(x == 3 || x == 4 || x == 5)){
            return false;
        }
        else if(y == 2 && (x == 1 || x == 7)){
            return false;
        }
        else if(y == 5 && (x == 2 || x == 6)){
            return false;
        }
        else{
            return true;
        }
    }
}

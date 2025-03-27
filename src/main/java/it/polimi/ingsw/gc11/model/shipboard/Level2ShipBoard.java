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
     * Adjusts the given X coordinate by subtracting 4
     *
     * @param x The original X coordinate
     * @return The adjusted X coordinate
     */
    @Override
    public int adaptX (int x){
        return x - 4;
    }

    /**
     * Adjusts the given Y coordinate by subtracting 5
     *
     * @param y The original Y coordinate
     * @return The adjusted Y coordinate
     */
    @Override
    public int adaptY (int y){
        return y - 5;
    }



    /**
     * Validates whether the given coordinates are within the allowed area of the shipboard
     *
     * @param x The X coordinate to validate
     * @param y The Y coordinate to validate
     * @return true if the coordinates are valid, false otherwise
     * @throws IllegalArgumentException if the coordinates are outside the board's bounds
     */
    @Override
    public boolean validateCoordinates(int x, int y){
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

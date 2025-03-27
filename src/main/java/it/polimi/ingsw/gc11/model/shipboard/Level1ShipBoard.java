package it.polimi.ingsw.gc11.model.shipboard;


/**
 * Represents a level 1 shipboard, extending the general ShipBoard class
 * This class defines the board's dimensions and provides a method to validate coordinates
 */
public class Level1ShipBoard extends ShipBoard {

    /**
     * Constructs a Level1ShipBoard with predefined dimensions (5x5)
     */
    public Level1ShipBoard() {
        super(5, 5);
    }



    /**
     * Adjusts the given X coordinate by subtracting 5
     *
     * @param x The original X coordinate
     * @return The adjusted X coordinate
     */
    @Override
    public int adaptX (int x){
        return x - 5;
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
        if (x < 0 || y < 0 || x >= 5 || y >= 5) {
            throw new IllegalArgumentException("Coordinates out of the board's bounds");
        }

        if(y == 0 && x != 2){
            return false;
        }
        else if(y == 1 && (x == 0 || x == 4)){
            return false;
        }
        else if(y == 4 && x == 2){
            return false;
        }
        else{
            return true;
        }
    }
}

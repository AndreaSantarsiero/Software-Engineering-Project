package it.polimi.ingsw.gc11.model.shipboard;



/**
 * Represents a level 2 shipboard, extending the general ShipBoard class
 * <p>
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
     * Validates whether the given coordinate indexes are within the allowed area of the shipboard
     *
     * @param j The X coordinate index to validate
     * @param i The Y coordinate index to validate
     * @return true if the coordinate indexes are valid, false otherwise
     * @throws IllegalArgumentException if the coordinate indexes are outside the board's bounds
     */
    @Override
    public boolean validateIndexes(int j, int i){
        if (j < 0 || i < 0 || j >= 7 || i >= 5) {
            throw new IllegalArgumentException("Coordinate indexes out of the board");
        }

        if(i == 0 && (j == 0 || j == 1 || j == 3 || j == 5 || j == 6)){
            return false;
        }
        else if(i == 1 && (j == 0 || j == 6)){
            return false;
        }
        else if(i == 4 && j == 3){
            return false;
        }
        else{
            return true;
        }
    }
}

package it.polimi.ingsw.gc11.model.shipboard;



/**
 * Represents a level 3 shipboard, extending the general ShipBoard class
 * <p>
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
     * Adjusts the given X coordinate by subtracting 3
     *
     * @param x The original X coordinate
     * @return The adjusted X coordinate
     */
    @Override
    public int adaptX (int x){
        return x - 3;
    }

    /**
     * Adjusts the given Y coordinate by subtracting 4
     *
     * @param y The original Y coordinate
     * @return The adjusted Y coordinate
     */
    @Override
    public int adaptY (int y){
        return y - 4;
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
        if (j < 0 || i < 0 || j >= 9 || i >= 6) {
            throw new IllegalArgumentException("Coordinate indexes out of the board");
        }

        if(i == 0 && j != 4){
            return false;
        }
        else if(i == 1 && !(j == 3 || j == 4 || j == 5)){
            return false;
        }
        else if(i == 2 && (j == 1 || j == 7)){
            return false;
        }
        else if(i == 5 && (j == 2 || j == 6)){
            return false;
        }
        else{
            return true;
        }
    }
}

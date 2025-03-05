package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class Level3ShipBoard extends ShipBoard {

    private final int X_MAX = 9;
    private final int Y_MAX = 6;


    public Level3ShipBoard() {
        super(9, 6);
    }


    public Boolean validateCoordinates(int x, int y){
        x -= 3;
        y -= 4;

        if (x < 0 || y < 0 || x >= X_MAX || y >= Y_MAX) {
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

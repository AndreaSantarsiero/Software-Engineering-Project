package it.polimi.ingsw.gc11.model.shipboard;

import it.polimi.ingsw.gc11.model.shipcard.ShipCard;

public class Level2ShipBoard extends ShipBoard {

    private final int X_MAX = 7;
    private final int Y_MAX = 5;


    public Level2ShipBoard() {
        super(7, 5);
    }



    public Boolean validateCoordinates(int x, int y){
        x -= 5;
        y -= 4;

        if (x < 0 || y < 0 || x >= X_MAX || y >= Y_MAX) {
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

package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;

public abstract class Template {
    //Listener
    public void update(AdventureCard adventureCard){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(Player player){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(ShipBoard shipBoard){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(Hit hit){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(String username, ShipBoard enemiesShipBoard){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(Player player, int position){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package it.polimi.ingsw.gc11.model.adventurecard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;



public class OpenSpace extends AdventureCard {

    public OpenSpace(AdventureCard.Type type) {
        super(type);
    }
    /*
    In turn, each player declares their engine strength, beginning with the leader, and continuing in the order
    You must decide whether to spend battery
    Then you immediately move your rocket marker that many empty spaces forward.
    This may allow you to pass players ahead of you (occupied spaces are skipped) and perhaps even take the lead.
     */
    @Override
    public void handler(GameModel model) {
        //to implement
    }
}

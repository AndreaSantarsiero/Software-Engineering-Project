package it.polimi.ingsw.gc11.model.adventurecard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;

public class OpenSpace extends AdventureCard {
    public OpenSpace(Type type) {
        super(type);
    }

    /*
    In turn, each player declares their engine strength, beginning with the leader, and continuing in the order
    You must decide whether to spend battery
    Then you immediately move your rocket marker that many empty spaces forward.
    This may allow you to pass players ahead of you (occupied spaces are skipped) and perhaps even take the lead.
     */
    public void handler(GameModel gameModel, int numBatteries) {
        Player[] players = gameModel.getPlayers();

        for(int i = 1; i < 5; i++){

            for(int j = 0; j < players.length; j++){
                if(players[j].getPosition() == i){
                    int delta = players[j].getShipBoard().getEnginePower(numBatteries);
                    players[j].setPosition(delta, gameModel);
                }
            }

        }

    }
}

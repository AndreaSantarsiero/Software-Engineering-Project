package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.StarDustStates.StarDustState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;



public class StarDust extends AdventureCard {

    public StarDust(AdventureCard.Type type) {
        super(type);
    }

    //NEED METHOD IN SHIPBOARD THAT COUNTS THE EXPOSED CONNECTORS
    public void handler(GameModel model, String username) {
        int num = model.getPlayerShipBoard(username).getExposedConnectors();

        //Lose Days of flight
        model.move(username, num);
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new StarDustState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}

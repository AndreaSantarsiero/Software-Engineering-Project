package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.OpenSpaceStates.OpenSpaceState;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;



public class OpenSpace extends AdventureCard {

    public OpenSpace(AdventureCard.Type type) {
        super(type);
    }

    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new OpenSpaceState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }


//    public void handler(GameModel model, String username, List<Battery> batteriesUserAccepted, List<Integer> numBatteries) {
//        int TotalNumBatteries = 0;
//        for (int num : numBatteries) {
//            TotalNumBatteries += num;
//        }
//        int power = model.getPlayerShipBoard(username).getEnginesPower(TotalNumBatteries);
//
//        //Remove batteries from shipboard
//        for(int i = 0; i < batteriesUserAccepted.size(); i++) {
//            batteriesUserAccepted.get(i).useBatteries(numBatteries.get(i));
//        }
//
//        //Move Player on flightBoard
//        model.move(username, power);
//    }
}

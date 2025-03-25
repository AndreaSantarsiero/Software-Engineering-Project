package it.polimi.ingsw.gc11.model.adventurecard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.List;


public class OpenSpace extends AdventureCard {

    public OpenSpace(AdventureCard.Type type) {
        super(type);
    }

    public void handler(GameModel model, String username, List<Battery> batteriesUserAccepted, List<Integer> numBatteries) {
        int TotalNumBatteries = 0;
        for (int num : numBatteries) {
            TotalNumBatteries += num;
        }
        int power = model.getPlayerShipBoard(username).getEnginesPower(TotalNumBatteries);

        //Remove batteries from shipboard
        for(int i = 0; i < batteriesUserAccepted.size(); i++) {
            batteriesUserAccepted.get(i).useBatteries(numBatteries.get(i));
        }

        //Move Player on flightBoard
        model.loseDays(username, power);
    }
}

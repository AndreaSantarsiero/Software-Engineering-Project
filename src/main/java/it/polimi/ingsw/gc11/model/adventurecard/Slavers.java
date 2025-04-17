package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;
import java.util.List;



public class Slavers extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int lostMembers;
    private final int coins;


    public Slavers(AdventureCard.Type type, int lostDays, int firePower, int lostMembers, int coins) throws IllegalArgumentException{
        super(type);
        if (lostDays < 0 || firePower < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("lostDays and numMaterials is null.");
        }
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.lostMembers = lostMembers;
        this.coins = coins;
    }

    public int getLostDays() {return lostDays;}

    public int getFirePower() {return firePower;}

    public int getLostMembers() {return lostMembers;}

    public int getCoins() {return coins;}

    public void handler(GameModel model, String username, List<Cannon> doubleCannons, List<Integer> numBatteries, List<Battery> batteries, List<HousingUnit> housingUnitsUserAccepted, List<Integer> killedMembers, boolean wantToMove) {
        int totalNumBatteries = 0;
        for (int num : numBatteries) {
            totalNumBatteries += num;
        }

        double power = model.getPlayerShipBoard(username).getCannonsPower(doubleCannons);

        if(totalNumBatteries != doubleCannons.size()){
            throw new IllegalArgumentException("totalNumBatteries is not equal to the number of double Cannons");
        }

        //Remove Batteries
        for(int i = 0; i < batteries.size(); i++) {
            batteries.get(i).useBatteries(numBatteries.get(i));
        }

        if(power > firePower){
            if(wantToMove){
                model.addCoins(username, coins);
                model.move(username, lostDays);
            }
        }
        else{
            //model.getPlayerShipBoard(username).killMembers(housingUnitsUserAccepted, killedMembers);  /* da adattare usando la mappa al posto delle due liste */
        }
    }

    @Override
    public AdventureState getInitialState(AdventureCard adventureCard, GameModel gameModel, Player player){
        return null;
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}

package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.PiratesStates.PiratesState;
import it.polimi.ingsw.gc11.model.Shot;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;
import java.util.ArrayList;



public class Pirates extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int coins;
    public ArrayList<Shot> shots;



    public Pirates(String id, AdventureCard.Type type, int lostDays, int firePower, int coins, ArrayList<Shot> shots) throws IllegalArgumentException{
        super(id, type);
        if (shots == null || shots.isEmpty()|| lostDays < 0 || firePower < 0 || coins < 0){
            throw new IllegalArgumentException();
        }
        for(Shot shot: shots) {
            if (shot == null) {
                throw new NullPointerException("shot is null.");
            }
        }
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.coins = coins;
        this.shots = shots;
    }



    public int getLostDays() {return lostDays;}

    public int getFirePower() {return firePower;}

    public int getCoins() {return coins;}

    public ArrayList<Shot> getShots() {return shots;}



    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new PiratesState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    @Override
    public void getStates(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setStates(this);
    }
}

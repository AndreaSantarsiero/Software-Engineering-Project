package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.SlaversStates.SlaversState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;



public class Slavers extends AdventureCard {

    private final int lostDays;
    private final int firePower;
    private final int lostMembers;
    private final int coins;


    public Slavers(String id, AdventureCard.Type type, int lostDays, int firePower, int lostMembers, int coins) throws IllegalArgumentException{
        super(id, type);
        if (type == null || lostDays < 0 || firePower < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("At least 1 param isn't valid.");
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



    @Override
    public AdventureState getInitialState(AdventurePhase advContext){
        return new SlaversState(advContext);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

    @Override
    public void getHintMessage(AdventurePhaseData adventurePhaseData){
        adventurePhaseData.setHintMessage(this);
    }
}

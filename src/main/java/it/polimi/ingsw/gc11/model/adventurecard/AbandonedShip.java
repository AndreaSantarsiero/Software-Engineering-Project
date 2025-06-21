package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.AbandonedShipState;
import it.polimi.ingsw.gc11.controller.State.AdventurePhase;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.utils.AdventureCardCLI;



public class AbandonedShip extends AdventureCard {

    private final int lostDays;
    private final int lostMembers;
    private final int coins;
    private boolean resolved;



    public AbandonedShip(String id, AdventureCard.Type type, int lostDays, int lostMembers, int coins) throws IllegalArgumentException{
        super(id, type);

        if(lostDays < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        this.lostDays = lostDays;
        this.lostMembers = lostMembers;
        this.coins = coins;
        this.resolved = false;

    }



    public int getLostDays() {return lostDays;}

    public int getLostMembers() {return lostMembers;}

    public int getCoins() {return coins;}

    public boolean isResolved() {return resolved;}

    public void resolveCard(){
        this.resolved = true;
    }



    @Override
    public AdventureState getInitialState(AdventurePhase advContext ){
        return new AbandonedShipState(advContext);
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

package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AbandonedShipStates.ChooseHousing;
import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;



public class AbandonedShip extends AdventureCard {

    private final int lostDays;
    private final int lostMembers;
    private final int coins;
    private boolean resolved;

    public AbandonedShip(AdventureCard.Type type, int lostDays, int lostMembers, int coins) throws IllegalArgumentException{
        super(type);

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
    public AdventureState getInitialState(GameModel gameModel, Player player){
        return new ChooseHousing(this, gameModel, player);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }

//    //Username is the player playing the card
//    public void handler(GameModel model, String username, List<HousingUnit> housingUnit, List<Integer> killedMembers) {
//        //KillMember from the shipboard of the Player
//         //model.getPlayerShipBoard(username).killMembers(housingUnit, killedMembers);  /* da adattare usando la mappa al posto delle due liste */
//
//         //Add coins to the player
//         model.addCoins(username, coins);
//
//         //Player lose days of flight
//        model.move(username, lostDays);
//    }
}

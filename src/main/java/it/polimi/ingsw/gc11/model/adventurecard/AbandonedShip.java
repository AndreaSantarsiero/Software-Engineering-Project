package it.polimi.ingsw.gc11.model.adventurecard;

public class AbandonedShip extends AdventureCard {

    private int lostDays;
    private int lostMembers;
    private int coins;
    private boolean resolved;

    public AbandonedShip(AdventureCard.Type type, int lostDays, int lostMembers, int coins) {
        super(type);

        if(lostDays < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("lostDays or lostMembers or coins is null.");
        }

        this.lostDays = lostDays;
        this.lostMembers = lostMembers;
        this.coins = coins;
        this.resolved = false;

    }

    public void handler(){
        //To implement
    }

}

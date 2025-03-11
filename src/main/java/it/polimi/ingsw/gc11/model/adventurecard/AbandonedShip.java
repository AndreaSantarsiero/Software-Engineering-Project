package it.polimi.ingsw.gc11.model.adventurecard;

public class AbandonedShip extends AdventureCard {

    private int lostDays;
    private int lostMembers;
    private int coins;
    private boolean resolved;

    public AbandonedShip(int lostDays, int lostMembers, int coins) {
        super(Type.TRIAL);

        if(lostDays < 0 || lostMembers < 0 || coins < 0) {
            throw new IllegalArgumentException("lostDays or lostMembers or coins is null.");
        }

        this.lostDays = lostDays;
        this.lostMembers = lostMembers;
        this.coins = coins;
        this.resoled = false;

    }

    public void repairShip(){
        if(this.resoled){
            throw new IllegalStateException("ship already repaired.");
        }
        this.resoled = true;
    }

    public int getLostDays() {
        return lostDays;
    }
    public int getLostMembers() {
        return lostMembers;
    }
    public int getCoins() {
        return coins;
    }
}

package it.polimi.ingsw.gc11.model.adventurecard;



public class Slavers extends AdventureCard {

    private int lostDays;
    private int firePower;
    private int membersKilled;
    private int coins;


    public Slavers(AdventureCard.Type type, int lostDays, int firePower, int membersKilled, int coins) {
        super(type);
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.membersKilled = membersKilled;
        this.coins = coins;
    }


    public int getLostDays() {
        return lostDays;
    }
    public int getFirePower() {
        return firePower;
    }
    public int getMembersKilled() {
        return membersKilled;
    }
    public int getCoins() {
        return coins;
    }
}

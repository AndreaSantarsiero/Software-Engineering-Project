package it.polimi.ingsw.gc11.model.adventurecard;



public class Pirates extends AdventureCard {

    private int lostDays;
    private int firePower;
    private int coins;


    public Pirates(AdventureCard.Type type, int lostDays, int firePower, int coins) {
        super(type);
        this.lostDays = lostDays;
        this.firePower = firePower;
        this.coins = coins;
    }


    public int getLostDays() {
        return lostDays;
    }
    public int getFirePower() {
        return firePower;
    }
    public int getCoins() {
        return coins;
    }
}

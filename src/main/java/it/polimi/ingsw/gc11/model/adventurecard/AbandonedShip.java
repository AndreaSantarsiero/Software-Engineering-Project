package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.GameModel;

public class AbandonedShip extends AdventureCard {

    private final int lostDays;
    private final int lostMembers;
    private final int coins;
    private final boolean resolved;

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

    @Override
    public void handler(GameModel model) {

    }
}

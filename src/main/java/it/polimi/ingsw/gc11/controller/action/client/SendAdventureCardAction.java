package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

public class SendAdventureCardAction extends ServerAction{
    private  final AdventureCard adventureCard;

    public SendAdventureCardAction(AdventureCard adventureCard) {
        this.adventureCard = adventureCard;
    }

    public AdventureCard getAdventureCard() {
        return adventureCard;
    }

    @Override
    public void execute() {

    }

}

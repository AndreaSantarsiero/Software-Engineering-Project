package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.ShipCard;
import it.polimi.ingsw.gc11.view.cli.templates.CLITemplate;

import java.util.List;

abstract class Observable {
    private Template listener;

    public void setListener(CLITemplate listener) {
        this.listener = listener;
    }

    //BuildingPhase
    protected void notifyListeners(ShipBoard shipBoard) {
        listener.update(shipBoard);
    }

    protected void notifyListeners(String username, ShipBoard shipBoard) {
        listener.update(username, shipBoard);
    }

    protected void notifyListeners(ShipCard heldShipCard) {
        listener.update(heldShipCard);
    }

    protected void notifyListeners(List<AdventureCard> miniDeck) {
        listener.update(miniDeck);
    }

    //AdventurePhase
    protected void notifyListeners(Player player) {
        listener.update(player);
    }

    protected void notifyListeners(AdventureCard card) {
        listener.update(card);
    }

    protected void notifyListeners(Hit hit) {
        listener.update(hit);
    }

    protected void notifyListeners(Player player, int position) {
        listener.update(player, position);
    }
}

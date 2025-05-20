package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.cli.templates.CLITemplate;

abstract class Observable {
    private Template listener;

    public void setListener(CLITemplate listener) {
        this.listener = listener;
    }

    protected void notifyListeners(AdventureCard card) {
        listener.update(card); //Per non avvisare tutti i listener dovrei fare un visitor?

    }

    protected void notifyListeners(Hit hit) {
        listener.update(hit);
    }
}

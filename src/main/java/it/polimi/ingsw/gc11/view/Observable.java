package it.polimi.ingsw.gc11.view;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.view.cli.templates.CLITemplate;

import java.util.ArrayList;
import java.util.List;

abstract class Observable {
    List<CLITemplate> listeners = new ArrayList<>();

    public void addListener(CLITemplate listener) {
        listeners.add(listener);
    }

    public void removeListener(CLITemplate listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners(AdventureCard card) {
        for (CLITemplate listener : listeners) {
            listener.update(card); //Per non avvisare tutti i listener dovrei fare un visitor?
        }
    }

    protected void notifyListeners(Hit hit) {
        for (CLITemplate listener : listeners) {
            listener.update(hit);
        }
    }
}

package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class ObserveMiniDeckAction extends ClientAction{
    private int numDeck;

    public ObserveMiniDeckAction(String username, int numDeck) {
        super(username);
        this.numDeck = numDeck;
    }

    @Override
    public void execute(GameContext context) {
        context.observeMiniDeck(username, numDeck);
    }
}

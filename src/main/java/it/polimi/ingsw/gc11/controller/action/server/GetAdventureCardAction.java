package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class GetAdventureCardAction extends ClientAction{
    public GetAdventureCardAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        context.getAdventureCard(username);
    }
}

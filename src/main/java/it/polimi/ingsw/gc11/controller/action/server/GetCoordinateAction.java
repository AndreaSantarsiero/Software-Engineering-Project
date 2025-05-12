package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class GetCoordinateAction extends ClientAction {
    public GetCoordinateAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.getCoordinate(getUsername());
    }
}


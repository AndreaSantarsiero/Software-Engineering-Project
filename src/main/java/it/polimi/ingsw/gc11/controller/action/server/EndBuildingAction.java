package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class EndBuildingAction extends ClientAction{
    private int pos;

    public EndBuildingAction(String username, int pos) {
        super(username);
        this.pos = pos;
    }

    @Override
    public void execute(GameContext context) {
        context.endBuilding(username, pos);
    }
}

package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.Map;

public class MeteorDefenseAction extends ClientAction {

    private final Map<Battery, Integer> batteries;
    private final Cannon cannon;

    public MeteorDefenseAction(String username, Map<Battery, Integer> batteries, Cannon cannon) {
        super(username);
        this.batteries = batteries;
        this.cannon = cannon;
    }

    @Override
    public void execute(GameContext gameContext) {
        gameContext.meteorDefense(getUsername(), batteries, cannon);
    }
}

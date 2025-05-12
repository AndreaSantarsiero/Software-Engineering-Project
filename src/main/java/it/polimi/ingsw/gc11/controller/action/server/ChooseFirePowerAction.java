package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;

import java.util.List;
import java.util.Map;

public class ChooseFirePowerAction extends ClientAction {
    private final Map<Battery, Integer> batteries;
    private final List<Cannon> cannons;

    public ChooseFirePowerAction(String username, Map<Battery, Integer> batteries, List<Cannon> cannons) {
        super(username);
        this.batteries = batteries;
        this.cannons = cannons;
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.chooseFirePower(getUsername(), batteries, cannons);
    }
}

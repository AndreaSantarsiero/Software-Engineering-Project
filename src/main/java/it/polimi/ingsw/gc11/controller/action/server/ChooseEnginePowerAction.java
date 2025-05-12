package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Map;

public class ChooseEnginePowerAction extends ClientAction {
    private final Map<Battery, Integer> batteries;

    public ChooseEnginePowerAction(String username, Map<Battery, Integer> batteries) {
        super(username);
        this.batteries = batteries;
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.chosenEnginePower(getUsername(), batteries);
    }
}


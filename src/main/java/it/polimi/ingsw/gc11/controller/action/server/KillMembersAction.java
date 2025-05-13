package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Map;

public class KillMembersAction extends ClientAction {
    private final Map<HousingUnit, Integer> housingUsage;

    public KillMembersAction(String username, Map<HousingUnit, Integer> housingUsage) {
        super(username);
        this.housingUsage = housingUsage;
    }

    @Override
    public void execute(GameContext ctx) {
        try {
            ctx.killMembers(getUsername(), housingUsage);
            NotifySuccessAction response = new NotifySuccessAction();
            ctx.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            ctx.sendAction(username, exception);
        }
    }
}


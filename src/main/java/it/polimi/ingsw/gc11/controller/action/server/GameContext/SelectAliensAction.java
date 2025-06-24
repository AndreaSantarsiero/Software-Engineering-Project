package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.model.shipcard.AlienUnit;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

public class SelectAliensAction extends ClientGameAction {
    private AlienUnit alienUnit;
    private HousingUnit housingUnit;

    public SelectAliensAction(String username, AlienUnit alienUnit, HousingUnit housingUnit) {
        super(username);
        this.alienUnit = alienUnit;
        this.housingUnit = housingUnit;
    }

    @Override
    public void execute(GameContext context) {
        try {
            context.selectAliens(username, alienUnit, housingUnit);
            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

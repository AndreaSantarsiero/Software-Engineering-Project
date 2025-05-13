package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyShipBoardAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
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
    public void execute(GameContext context) {
        try {
            Player player = context.chooseFirePower(getUsername(), batteries, cannons);

            if (player.getUsername().equals(username)) {
                UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
                context.sendAction(username, response);
            }
            else {
                UpdateEnemyShipBoardAction response1 = new UpdateEnemyShipBoardAction(player.getShipBoard(), username);
                context.sendAction(username, response1);

                UpdateEnemyProfileAction response2 = new UpdateEnemyProfileAction(player);
                context.sendAction(username, response2);
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

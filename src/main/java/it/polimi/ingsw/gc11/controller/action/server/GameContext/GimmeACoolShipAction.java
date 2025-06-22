package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.LoadACoolShipAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;



public class GimmeACoolShipAction extends ClientGameAction {

    private final int num;


    public GimmeACoolShipAction(String username, int num) {
        super(username);
        this.num = num;
    }


    @Override
    public void execute(GameContext context) {
        try{
            ShipBoard coolShip = context.gimmeACoolShip(username, num);
            LoadACoolShipAction response = new LoadACoolShipAction(coolShip);
            context.sendAction(username, response);
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.action.client.SendHitAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;



public class GetCoordinateAction extends ClientGameAction {

    public GetCoordinateAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            Hit hit = context.getCoordinate(username);

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                SendHitAction response = new SendHitAction(hit);
                context.sendAction(p.getUsername(), response);
            }

            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}


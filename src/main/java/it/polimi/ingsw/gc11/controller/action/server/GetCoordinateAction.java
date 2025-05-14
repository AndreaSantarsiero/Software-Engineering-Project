package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.SendHitAction;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;

public class GetCoordinateAction extends ClientAction {
    public GetCoordinateAction(String username) {
        super(username);
    }

    @Override
    public void execute(GameContext context) {
        try {
            Hit hit = context.getCoordinate(username);

            if(hit.getCoord() < 0){
                NotifyExceptionAction exception = new NotifyExceptionAction("Coordinate not valid");
                context.sendAction(username, exception);
            }

            //Invio a tutti i player l'hit in arrivo
            for(Player p : context.getGameModel().getPlayers()) {
                if(!p.isAbort()){
                    SendHitAction response = new SendHitAction(hit);
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}


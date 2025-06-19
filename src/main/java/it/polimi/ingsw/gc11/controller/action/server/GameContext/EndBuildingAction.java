package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.action.client.SetCheckPhaseAction;
import it.polimi.ingsw.gc11.model.Player;


public class EndBuildingAction extends ClientGameAction {

    private int pos;


    public EndBuildingAction(String username) {
        super(username);
    }


    @Override
    public void execute(GameContext context) {
        try {
            context.endBuilding(username);
            if(context.getPhase().getPhaseName().equals("CheckPhase")){
                SetCheckPhaseAction send = new SetCheckPhaseAction();
                for (Player player : context.getGameModel().getPlayers()) {
                    context.sendAction(player.getUsername(), send);
                }
            }
            else{
                NotifySuccessAction response = new NotifySuccessAction();
                context.sendAction(username, response);
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

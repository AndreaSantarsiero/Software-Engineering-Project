package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;

public class RewardDecisionAction extends ClientAction {
    private final boolean decision;

    public RewardDecisionAction(String username, boolean decision) {
        super(username);
        this.decision = decision;
    }

    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.rewardDecision(username, decision);

            //Invio il cambiamento della posizione sulla flightboard del player che ha giocato la carta a tutti i player
            for(Player p : context.getGameModel().getPlayers()) {
                if(!p.isAbort()){
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, context.getGameModel().getPositionOnBoard(p.getUsername()));
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

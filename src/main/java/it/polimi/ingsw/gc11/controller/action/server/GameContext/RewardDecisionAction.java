package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;



public class RewardDecisionAction extends ClientGameAction {

    private final boolean decision;


    public RewardDecisionAction(String username, boolean decision) {
        super(username);
        this.decision = decision;
    }


    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.rewardDecision(username, decision);
            String currentPlayer = context.getCurrentPlayerUsername().getUsername();

            for(Player p : context.getGameModel().getPlayers()) {
                if(player.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, currentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, currentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}

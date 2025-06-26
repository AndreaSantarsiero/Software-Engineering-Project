package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;

import java.util.Arrays;
import java.util.Map;



public class KillMembersAction extends ClientGameAction {

    private final Map<HousingUnit, Integer> housingUsage;


    public KillMembersAction(String username, Map<HousingUnit, Integer> housingUsage) {
        super(username);
        this.housingUsage = housingUsage;
    }


    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.killMembers(getUsername(), housingUsage);

            String newCurrentPlayer = context.getCurrentPlayer().getUsername();
            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, newCurrentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, newCurrentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        }
        catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}


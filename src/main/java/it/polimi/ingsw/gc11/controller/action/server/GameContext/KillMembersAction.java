package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.*;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
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

            for(Player p : context.getGameModel().getPlayers()) {
                if(player.getUsername().equals(p.getUsername())) {
                    UpdateShipBoardAction response = new UpdateShipBoardAction(player.getShipBoard());
                    context.sendAction(username, response);
                }
                else if(!p.isAbort()){
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player);
                    context.sendAction(p.getUsername(), response);
                }
            }
        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, exception);
        }
    }
}


package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdateShipBoardAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import java.util.Map;



public class ChooseEnginePowerAction extends ClientGameAction {

    private final Map<Battery, Integer> batteries;


    public ChooseEnginePowerAction(String username, Map<Battery, Integer> batteries) {
        super(username);
        this.batteries = batteries;
    }


    @Override
    public void execute(GameContext context) {
        try {
            Player player = context.chooseEnginePower(getUsername(), batteries);

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


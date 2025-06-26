package it.polimi.ingsw.gc11.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.action.client.UpdateEnemyProfileAction;
import it.polimi.ingsw.gc11.action.client.UpdatePlayerProfileAction;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipcard.Battery;

import java.util.Arrays;
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
            String currentPlayer = context.getCurrentPlayer().getUsername();

            for(Player p : context.getGameModel().getPlayersNotAbort()) {
                if(p.getUsername().equals(username)) {
                    UpdatePlayerProfileAction response = new UpdatePlayerProfileAction(player, currentPlayer);
                    context.sendAction(username, response);
                }
                else {
                    UpdateEnemyProfileAction response = new UpdateEnemyProfileAction(player, currentPlayer);
                    context.sendAction(p.getUsername(), response);
                }
            }

        } catch (Exception e){
            NotifyExceptionAction exception = new NotifyExceptionAction(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            context.sendAction(username, exception);
        }
    }
}


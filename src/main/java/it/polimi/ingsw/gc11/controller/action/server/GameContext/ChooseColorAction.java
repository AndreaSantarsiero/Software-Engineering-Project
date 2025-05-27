package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayerColorAction;



public class ChooseColorAction extends ClientGameAction {

    String playerColor;


    public ChooseColorAction(String username, String playerColor) {
        super(username);
        this.playerColor = playerColor;
    }


    @Override
    public void execute(GameContext gameContext) {
        try{
            String chosenColor = gameContext.chooseColor(username, playerColor);
            UpdatePlayerColorAction updatePlayerColorAction = new UpdatePlayerColorAction(chosenColor);
            gameContext.sendAction(username, updatePlayerColorAction);
        }
        catch (Exception e){
            NotifyExceptionAction notifyExceptionAction = new NotifyExceptionAction(e.getMessage());
            gameContext.sendAction(username, notifyExceptionAction);
        }
    }
}

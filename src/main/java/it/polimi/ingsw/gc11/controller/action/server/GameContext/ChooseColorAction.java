package it.polimi.ingsw.gc11.controller.action.server.GameContext;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.NotifyExceptionAction;
import it.polimi.ingsw.gc11.controller.action.client.NotifySuccessAction;
import it.polimi.ingsw.gc11.controller.action.client.SetBuildingPhaseAction;
import it.polimi.ingsw.gc11.controller.action.client.UpdatePlayersColorAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;

import java.util.ArrayList;
import java.util.Map;



public class ChooseColorAction extends ClientGameAction {

    String playerColor;


    public ChooseColorAction(String username, String playerColor) {
        super(username);
        this.playerColor = playerColor;
    }


    @Override
    public void execute(GameContext context) {
        try{
            context.chooseColor(username, playerColor);
            NotifySuccessAction success = new NotifySuccessAction();
            context.sendAction(username, success);

            Thread.sleep(10);

            Map<String, String> playersColor = context.getPlayersColor();
            UpdatePlayersColorAction response = new UpdatePlayersColorAction(playersColor);
            for (Player player : context.getGameModel().getPlayers()) {
                context.sendAction(player.getUsername(), response);
            }

            if (context.getGameModel().isGameStarted()) {
                GameModel gameModel = context.getGameModel();
                ArrayList<String> allPlayersUsernames = new ArrayList<>();
                for (Player player : gameModel.getPlayers()) {
                    allPlayersUsernames.add(player.getUsername());
                }

                for (Player p : context.getGameModel().getPlayers()) {
                    ArrayList<String> othersPlayers = new ArrayList<>(allPlayersUsernames);
                    othersPlayers.remove(p.getUsername());
                    System.out.println("FlightType: " + gameModel.getFlightBoard().getType());

                    SetBuildingPhaseAction send = new SetBuildingPhaseAction(p.getShipBoard(), gameModel.getFreeShipCardsCount(), gameModel.getFlightBoard().getType(), othersPlayers);
                    context.sendAction(p.getUsername(), send);
                }
            }

        }
        catch (Exception e){
            NotifyExceptionAction notifyExceptionAction = new NotifyExceptionAction(e.getMessage());
            context.sendAction(username, notifyExceptionAction);
        }
    }
}

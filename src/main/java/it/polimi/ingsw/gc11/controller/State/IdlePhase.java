package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetBuildingPhaseAction;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;



public class IdlePhase extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private boolean isFullLobby;
    private int numReadyPlayers;



    public IdlePhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.numReadyPlayers = 0;
        this.isFullLobby = false;
    }



    @Override
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        this.gameModel.addPlayer(playerUsername);

        if (gameModel.getPlayers().size() == gameModel.getMaxNumPlayers()){
            this.isFullLobby = true;
        }
    }



    @Override
    public void chooseColor(String username, String chosenColor) {
        Player player = this.gameModel.getPlayer(username);
        String color = chosenColor.toLowerCase();
        this.gameModel.setPlayerColor(username, color);
        this.numReadyPlayers++;

        if (isFullLobby && (numReadyPlayers == gameModel.getMaxNumPlayers())) {
            if (gameModel.getFlightBoard().getType().equals(FlightBoard.Type.LEVEL2)){
                this.gameContext.setPhase(new BuildingPhaseLv2(this.gameContext));
            }
            else if (gameModel.getFlightBoard().getType().equals(FlightBoard.Type.TRIAL)){
                this.gameContext.setPhase(new BuildingPhaseTrial(this.gameContext));
            }

            for (Player p : gameModel.getPlayers()) {
                SetBuildingPhaseAction send = new SetBuildingPhaseAction(p.getShipBoard(), gameModel.getFreeShipCardsCount());
                gameContext.sendAction(p.getUsername(), send);
            }
        }
    }

}

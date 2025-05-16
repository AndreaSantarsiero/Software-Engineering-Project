package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.ServerAction;
import it.polimi.ingsw.gc11.controller.action.client.SetAdventurePhaseAction;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import java.util.ArrayList;
import java.util.List;

public class CheckPhase extends GamePhase {
    private final GameContext gameContext;
    private List<Player> badShipPlayers;
    private int numShipsCorrected;

    public CheckPhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.badShipPlayers = new ArrayList<>();
        this.numShipsCorrected = 0;

        GameModel gameModel = gameContext.getGameModel();
        List<Player> players = gameModel.getPlayers();
        for (Player player : players) {
            if(!player.getShipBoard().checkShip()){
                this.badShipPlayers.add(player);
            }
        }

        if (this.badShipPlayers.isEmpty()) {
            gameContext.setPhase(new AdventurePhase(this.gameContext));

            //Chiedo approval di santa:
//            SetAdventurePhaseAction send = new SetAdventurePhaseAction();
//            for (Player p : gameModel.getPlayers()) {
//                gameContext.sendAction(p.getUsername(), send);
//            }

        }
    }

    //Immagino che al player vengano proposte diverse scelte per riparare la nave, lui ne sceglie una alla volta fino a
    //che non sistema la nave
    public void repairShip(String username, int choice){

    }

}

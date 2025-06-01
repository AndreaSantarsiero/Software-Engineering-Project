package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import java.util.ArrayList;
import java.util.List;



public class CheckPhase extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private List<Player> badShipPlayers;



    public CheckPhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.badShipPlayers = new ArrayList<>();

        List<Player> players = gameModel.getPlayers();
        for (Player player : players) {
            //Player's shipboard is illegal
            if(!player.getShipBoard().checkShip()){
                player.setPosition(-1); //Player is removed from the ranking (position)
                this.badShipPlayers.add(player);
            }
        }

        //All the players have a correct shipboard
        if (this.badShipPlayers.isEmpty()) {
            this.gameContext.setPhase(new AdventurePhase(this.gameContext));

            //Chiedo approval di santa:
//            SetAdventurePhaseAction send = new SetAdventurePhaseAction();
//            for (Player p : gameModel.getPlayers()) {
//                gameContext.sendAction(p.getUsername(), send);
//            }

        }
    }



    @Override
    public ShipBoard repairShip(String username, ArrayList<Integer> cardsToEliminateX,
                                ArrayList<Integer> cardsToEliminateY) {

        Player player = this.gameModel.getPlayer(username);
        if (!badShipPlayers.contains(player)) {
            throw new IllegalStateException("You don't have to repair your ship.");
        }

        for (int i = 0; i < cardsToEliminateX.size(); i++) {
            if (this.gameModel.getFlightBoard().getType().equals(FlightBoard.Type.LEVEL2)) {
                player.getShipBoard()
                        .getShipCard(cardsToEliminateX.get(i), cardsToEliminateY.get(i))
                        .destroy();
            }
            player.getShipBoard()
                    .destroyShipCard(cardsToEliminateX.get(i), cardsToEliminateY.get(i));
        }

        if (player.getShipBoard().checkShip()){
            this.badShipPlayers.remove(player);
            this.gameModel.endBuilding(username);//Player position is set to the first available
            if (this.badShipPlayers.isEmpty()) {
                this.gameContext.setPhase(new AdventurePhase(this.gameContext));
            }
            //Avvisa il player che ora la sua shipboard è stata riparata
            return player.getShipBoard();
        }
        else{
            //Avvisa il player che la sua shipBoard è ancora da riparare
            throw new IllegalStateException("Your shipboard wasn't repaired correctly.");
        }
    }

    @Override
    public void changePosition(String username, int pos){

        Player player = this.gameModel.getPlayer(username);
        if (badShipPlayers.contains(player)) {
            throw new IllegalStateException("You can't change your position because you have to repair your ship.");
        }

        this.gameModel.endBuilding(username, pos);
    }



    @Override
    public String getPhaseName(){
        return "CheckPhase";
    }
}

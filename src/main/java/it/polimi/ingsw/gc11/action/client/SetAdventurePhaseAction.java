package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.*;
import java.util.Map;



public class SetAdventurePhaseAction extends ServerAction {

    private final FlightBoard flightBoard;
    private final Player player;
    private final Map<String, Player> enemies;
    private final String currentPlayer;



    public SetAdventurePhaseAction(FlightBoard flightBoard, Player player, Map<String, Player> enemies, String currentPlayer) {
        this.flightBoard = flightBoard;
        this.player = player;
        this.enemies = enemies;
        this.currentPlayer = currentPlayer;
    }



    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    @Override public void loadData(BuildingPhaseData buildingPhaseData) {}

    @Override public void loadData(CheckPhaseData checkPhaseData) {}

    @Override public void loadData(AdventurePhaseData adventurePhaseData) {
        adventurePhaseData.initialize(flightBoard, player, enemies, currentPlayer);
    }

    @Override public void loadData(EndPhaseData endPhaseData) {}



    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting adventure phase");
        playerContext.setAdventurePhase();
        playerContext.getCurrentPhase().handle(this);
    }
}

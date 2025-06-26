package it.polimi.ingsw.gc11.action.client;

import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.*;
import java.time.Instant;
import java.util.ArrayList;


/**
 * Action that sets up the Building phase by initializing
 * the BuildingPhaseData with the ship board, free ship card count,
 * flight type, player list, timer expiration, and remaining timers.
 */
public class SetBuildingPhaseAction extends ServerAction {

    private final ShipBoard shipBoard;
    private final int freeShipCardsCount;
    private final FlightBoard.Type flightType;
    private ArrayList<String> playersUsernames;
    private final Instant expireTimerInstant;
    private final int timersLeft;


    /**
     * Constructs a new SetBuildingPhaseAction.
     *
     * @param shipBoard           the ShipBoard for the Building phase
     * @param freeShipCardsCount  the count of free ship cards available
     * @param flightType          the FlightBoard.Type for the session
     * @param playersUsernames    the list of current player usernames
     * @param expireTimerInstant  the Instant when the build timer expires
     * @param timersLeft          the number of timers remaining
     */
    public SetBuildingPhaseAction(ShipBoard shipBoard, int freeShipCardsCount, FlightBoard.Type flightType, ArrayList<String> playersUsernames, Instant expireTimerInstant, int timersLeft) {
        this.shipBoard = shipBoard;
        this.freeShipCardsCount = freeShipCardsCount;
        this.flightType = flightType;
        this.playersUsernames = playersUsernames;
        this.expireTimerInstant = expireTimerInstant;
        this.timersLeft = timersLeft;
    }


    /**
     * No-op for the Joining phase.
     *
     * @param joiningPhaseData the data for the Joining phase
     */
    @Override public void loadData(JoiningPhaseData joiningPhaseData) {}

    /**
     * Initializes the Building phase data with game setup parameters.
     *
     * @param buildingPhaseData the data model for the Building phase
     */
    @Override public void loadData(BuildingPhaseData buildingPhaseData) {
        buildingPhaseData.initialize(shipBoard, freeShipCardsCount, flightType, playersUsernames, expireTimerInstant, timersLeft);
    }

    /**
     * No-op for the Check phase.
     *
     * @param checkPhaseData the data for the Check phase
     */
    @Override public void loadData(CheckPhaseData checkPhaseData) {}

    /**
     * No-op for the Adventure phase.
     *
     * @param adventurePhaseData the data for the Adventure phase
     */
    @Override public void loadData(AdventurePhaseData adventurePhaseData) {}

    /**
     * No-op for the End phase.
     *
     * @param endPhaseData the data for the End phase
     */
    @Override public void loadData(EndPhaseData endPhaseData) {}


    /**
     * Executes this action by switching to the Building phase
     * and dispatching this action to the new phase handler.
     *
     * @param playerContext the context containing the current phase and view data
     */
    @Override
    public void execute(PlayerContext playerContext) {
        System.out.println("[CLIENT] setting building phase");
        playerContext.setBuildingPhase();
        playerContext.getCurrentPhase().handle(this);
    }
}

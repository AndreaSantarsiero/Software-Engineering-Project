package it.polimi.ingsw.gc11.controller;

import it.polimi.ingsw.gc11.model.FlightBoard;
import java.util.HashMap;

public class ServerController {
    private HashMap<String,GameContext> games; //multiple games version

    public ServerController() {
        this.games = new HashMap<>();
    }

    /**
     * Creates a new match with the specified flight type and player username.
     *
     * <p>This method initializes a new {@link GameContext}, sets the game level
     * based on the provided flight type, adds the player to the game context,
     * and stores the new game instance in the {@code games} map. The unique
     * game ID is then returned.
     *
     * @param flightType      the type of flight level for the game
     * @param playerUsername  the username of the player creating the match
     * @return the unique identifier of the newly created match
     */
    public String createNewMatch(FlightBoard.Type flightType, String playerUsername) {
        GameContext newGameContext = new GameContext(flightType);
        newGameContext.connectPlayerToGame(playerUsername);
        String id = newGameContext.getMatchID();
        this.games.put(id,newGameContext);
        return id;
    }


}

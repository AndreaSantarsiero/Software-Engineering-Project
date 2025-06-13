package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.controller.action.client.SetBuildingPhaseAction;
import it.polimi.ingsw.gc11.exceptions.FullLobbyException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;


/**
 * Represents the initial phase of the game, where players can join the lobby and choose their colors.
 * Once the lobby is full and all players have chosen a color, the game transitions to the appropriate building phase.
 */
public class IdlePhase extends GamePhase {

    private final GameContext gameContext;
    private final GameModel gameModel;
    private boolean isFullLobby;
    private int numReadyPlayers;

    /**
     * Constructs a new {@code IdlePhase} with the given game context.
     *
     * @param gameContext The context that handles the current state of the game and allows transitions.
     */
    public IdlePhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
        this.numReadyPlayers = 0;
        this.isFullLobby = false;
    }



    /**
     * Adds a player to the game if the lobby is not full and the username is not already taken.
     * If the maximum number of players is reached, marks the lobby as full.
     *
     * @param playerUsername The username of the player to add.
     * @throws FullLobbyException If the lobby is already full.
     * @throws UsernameAlreadyTakenException If the username is already used by another player.
     */
    @Override
    public void connectPlayerToGame(String playerUsername) throws FullLobbyException, UsernameAlreadyTakenException {
        this.gameModel.addPlayer(playerUsername);

        if (gameModel.getPlayers().size() == gameModel.getMaxNumPlayers()){
            this.isFullLobby = true;
        }
    }



    /**
     * Sets the color for the player with the given username and checks if all players are ready.
     * If the lobby is full and all players have selected a color, transitions to the appropriate building phase
     * and notifies each player with a {@link SetBuildingPhaseAction}.
     *
     * @param username The username of the player who chose the color.
     * @param chosenColor The color selected by the player.
     */
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

            this.gameModel.setGameStarted(true);    //to notify last player that is connecting
        }
    }


    /**
     * Returns the name of this phase.
     *
     * @return The string "IdlePhase".
     */
    @Override
    public String getPhaseName(){
        return "IdlePhase";
    }
}

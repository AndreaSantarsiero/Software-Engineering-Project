package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;

//Adventure Context
public class AdventurePhase extends GamePhase {
    private GameModel gameModel;
    private AdventureState advState;
    private AdventureCard drawnAdvCard;
    private int idxCurrentPlayer;
    private boolean resolvingAdvCard; //Flag is used to know if an advCard is currently being resolved

    public AdventurePhase(GameModel gameModel) {
        this.gameModel = gameModel;
        this.advState = new IdleState(this);
        this.drawnAdvCard = null;
        this.idxCurrentPlayer = 0;
        this.resolvingAdvCard = false;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public AdventureCard getDrawnAdvCard() {
        return drawnAdvCard;
    }

    public int getIdxCurrentPlayer() {
        return idxCurrentPlayer;
    }

    public boolean isResolvingAdvCard() {
        return resolvingAdvCard;
    }

    public void setAdvState(AdventureState adventureState) {
        this.advState = adventureState;
    }

    public void setDrawnAdvCard(AdventureCard drawnAdvCard) {
        this.drawnAdvCard = drawnAdvCard;
    }

    public void setIdxCurrentPlayer(int idxCurrentPlayer) {
        this.idxCurrentPlayer = idxCurrentPlayer;
    }

    public void setResolvingAdvCard(boolean resolvingAdvCard) {
        this.resolvingAdvCard = resolvingAdvCard;
    }

    @Override
    public void nextPhase(GameContext context) {
        context.setPhase(new EndgamePhase());
    }

    @Override
    public String getPhaseName(){
        return "ADVENTURE";
    }

    /**
     * Initializes the adventure state for the current adventure card extracted by the player using the specified game model.
     *
     */
    public void initAdventureState() {
        this.advState = drawnAdvCard.getInitialState(this);
    }

    @Override
    public AdventureCard getAdventureCard(String username) throws IllegalStateException {
        if (username == null) {
            throw new NullPointerException();
        }
        try {
            return this.advState.getAdventureCard(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void acceptAdventureCard(String username) {
        if (username == null) {
            throw new NullPointerException();
        }
        try {
            this.advState.getAdventureCard(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void declineAdventureCard(String username) {
        Player expectedPlayer = this.gameModel.getPlayers().get(this.idxCurrentPlayer);
        if (expectedPlayer.getUsername().equals(username)) {
            this.idxCurrentPlayer++;
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

}

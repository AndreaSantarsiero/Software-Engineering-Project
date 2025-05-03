package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

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
            this.advState.declineAdventureCard(username);
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        try {
            this.advState.killMembers(username, housingUsage);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void chosenMaterial(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        try {
            this.advState.chosenMaterial(username, storageMaterials);
        }
        catch(IllegalStateException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
        try{
            this.advState.chooseFirePower(username, batteries, doubleCannons);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void rewardDecision(String username, boolean decision){
        try{
            this.advState.rewardDecision(username, decision);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getCoordinate(String username){
        try{
            this.advState.getCoordinate(username);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleShot(String username, Map<Battery, Integer> batteries){
        try{
            this.advState.handleShot(username, batteries);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}

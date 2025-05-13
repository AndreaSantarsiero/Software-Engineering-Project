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

    public AdventurePhase(GameContext context) {
        this.gameModel = context.getGameModel();
        this.advState = new IdleState(this);
        this.drawnAdvCard = null;
        this.idxCurrentPlayer = 0;
        this.resolvingAdvCard = false;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public AdventureState getCurrentAdvState(){ return advState;}

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
        setAdvState(drawnAdvCard.getInitialState(this));
    }

    @Override
    public AdventureCard getAdventureCard(String username) {
        return advState.getAdventureCard(username);
    }

    @Override
    public void acceptAdventureCard(String username) {
        //this.advState.getAdventureCard(username);//è sbagliato?? uguale al metodo sopra?
        Player expectedPlayer = gameModel.getPlayers().get(idxCurrentPlayer);
        if (expectedPlayer.getUsername().equals(username)) {
            advState.acceptAdventureCard(username);
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

    @Override
    public void declineAdventureCard(String username) {
        Player expectedPlayer = gameModel.getPlayers().get(idxCurrentPlayer);
        if (expectedPlayer.getUsername().equals(username)) {
            advState.declineAdventureCard(username);
        }
        else {
            throw new IllegalArgumentException("It's not your turn to play!");
        }
    }

    @Override
    public void killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        this.advState.killMembers(username, housingUsage);
    }

    @Override
    public void chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        try {
            this.advState.chooseMaterials(username, storageMaterials);
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

    @Override
    public void useBatteries(String username, Map<Battery, Integer> batteries){
        try{
            this.advState.useBatteries(username, batteries);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void landOn(String username, int numPlanet){
        try{
            this.advState.landOn(username, numPlanet);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        try{
            this.advState.chooseEnginePower(username, Batteries);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void meteorHit(String username, Map<Battery, Integer> batteries, Cannon cannon){
        try{
            this.advState.meteorHit(username, batteries, cannon);
        }
        catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}

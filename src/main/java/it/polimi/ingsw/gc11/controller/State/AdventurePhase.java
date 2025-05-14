package it.polimi.ingsw.gc11.controller.State;

import it.polimi.ingsw.gc11.controller.GameContext;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.Battery;
import it.polimi.ingsw.gc11.model.shipcard.Cannon;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import it.polimi.ingsw.gc11.model.shipcard.Storage;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

//Adventure Context
public class AdventurePhase extends GamePhase {
    private GameContext gameContext;
    private GameModel gameModel;
    private AdventureState advState;
    private AdventureCard drawnAdvCard;
    private int idxCurrentPlayer;
    private boolean resolvingAdvCard; //Flag is used to know if an advCard is currently being resolved

    public AdventurePhase(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameModel = gameContext.getGameModel();
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


    public void nextPhase() {
        this.gameContext.setPhase(new EndgamePhase(this.gameContext));
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
    public Player killMembers(String username, Map<HousingUnit, Integer> housingUsage){
        return advState.killMembers(username, housingUsage);
    }

    @Override
    public Player chooseMaterials(String username, Map<Storage, AbstractMap.SimpleEntry<List<Material>, List<Material>>> storageMaterials){
        return advState.chooseMaterials(username, storageMaterials);
    }

    @Override
    public Player chooseFirePower(String username, Map<Battery, Integer> batteries, List<Cannon> doubleCannons){
            return advState.chooseFirePower(username, batteries, doubleCannons);
    }

    @Override
    public Player rewardDecision(String username, boolean decision){
        return advState.rewardDecision(username, decision);
    }

    @Override
    public Hit getCoordinate(String username){
        return advState.getCoordinate(username);
    }

    @Override
    public Player handleShot(String username, Map<Battery, Integer> batteries){
        return advState.handleShot(username, batteries);
    }

    @Override
    public Player useBatteries(String username, Map<Battery, Integer> batteries){
        return advState.useBatteries(username, batteries);
    }

    @Override
    public Player landOnPlanet(String username, int numPlanet){
        return advState.landOnPlanet(username, numPlanet);
    }

    public Player chooseEnginePower(String username, Map<Battery, Integer> Batteries){
        return advState.chooseEnginePower(username, Batteries);
    }

    @Override
    public Player meteorDefense(String username, Map<Battery, Integer> batteries, Cannon cannon){
        return advState.meteorDefense(username, batteries, cannon);
    }

}

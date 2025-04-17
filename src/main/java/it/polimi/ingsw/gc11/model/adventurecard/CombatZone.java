package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.controller.State.AdventureState;
import it.polimi.ingsw.gc11.controller.State.CombatZoneStates.SelectPlayerCombat;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.cli.AdventureCardCLI;



public class CombatZone extends AdventureCard {

    private CombatPhase[] combatPhases;

    public CombatZone(AdventureCard.Type type, CombatPhase[] combatPhases) {
        super(type);

        if(combatPhases==null || combatPhases.length!=3){
            throw new IllegalArgumentException();
        }
        this.combatPhases = combatPhases;
    }

    public CombatPhase getCombatPhase(int index) {
        return combatPhases[index];
    }

    @Override
    public AdventureState getInitialState(AdventureCard adventureCard, GameModel gameModel, Player player){
        return new SelectPlayerCombat(this, gameModel, 0);
    }

    @Override
    public void print(AdventureCardCLI adventureCardCLI, int i){
        adventureCardCLI.draw(this, i);
    }
}

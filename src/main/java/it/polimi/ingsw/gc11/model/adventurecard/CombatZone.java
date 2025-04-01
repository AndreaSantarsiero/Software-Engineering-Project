package it.polimi.ingsw.gc11.model.adventurecard;


import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.Shot;

import java.util.ArrayList;

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

}

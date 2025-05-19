package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;

import java.util.HashMap;
import java.util.Map;

public class AdventureTemplate implements CLITemplate {
    private AdventurePhaseData adventurePhaseData;
    private AdventureCard adventureCard; //carta correntemente in esecuzione, quella pescata dal player
    private Player player;
    private ShipBoard myShipBoard;
    private Hit hit; //hit coming from the advCard, contiene tutti i parametri: direzione, coordinate, dimensione
    private Map<String, ShipBoard> enemiesShipBoard;  //associo username altri player alla loro nave
    private Map<Player, Integer> players; //list of enemies players

    private AdventureTemplate() {
        enemiesShipBoard = new HashMap<>();
        players          = new HashMap<>();
    }

    @Override
    public void render() {

    }


}

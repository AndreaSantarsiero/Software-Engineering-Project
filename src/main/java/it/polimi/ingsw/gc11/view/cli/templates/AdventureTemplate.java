package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.Hit;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;

import java.util.HashMap;
import java.util.Map;

public class AdventureTemplate extends CLITemplate {
    private AdventurePhaseData adventurePhaseData;
    private AdventureCard adventureCard; //carta correntemente in esecuzione, quella pescata dal player
    private Player player;
    private ShipBoard myShipBoard;
    private Hit hit; //hit coming from the advCard, contiene tutti i parametri: direzione, coordinate, dimensione
    private Map<String, ShipBoard> enemiesShipBoard;  //associo username altri player alla loro nave
    private Map<Player, Integer> players; //list of enemies players

    public AdventureTemplate(AdventurePhaseData adventurePhaseData) {
        enemiesShipBoard = new HashMap<>();
        players          = new HashMap<>();
        adventurePhaseData.setListener(this); //Funziona?? Chi crea i template? si può passare AdventurePhaseData?? mi sa di no
    }

    @Override
    public void render() {
        //To implement
    }

    @Override
    public void update(Player player){
        if(!player.equals(this.player)){
            this.player = player;
            render();
        }
    }

    //manipola i dati ricevuti in ingresso e li disegna
    @Override
    public void update(AdventureCard adventureCard) {
        if(!adventureCard.equals(this.adventureCard)) {
            this.adventureCard = adventureCard;
            render();
        }
    }

    @Override
    public void update(Hit hit) {
        if(!hit.equals(this.hit)) {
            this.hit = hit;
            render();
        }
    }

    @Override
    public void update(String username, ShipBoard shipBoard) {
        if(!enemiesShipBoard.get(username).equals(shipBoard)) {
            enemiesShipBoard.put(username, shipBoard);
            render();
        }
    }

    @Override
    public void update(Player player, int position) {
        if(!players.get(player).equals(position)) {
            players.put(player, position);
            render();
        }
    }

    @Override
    public void update(ShipBoard shipBoard){
        if(!shipBoard.equals(this.myShipBoard)) {
            this.myShipBoard = shipBoard;
            render();
        }
    }
}

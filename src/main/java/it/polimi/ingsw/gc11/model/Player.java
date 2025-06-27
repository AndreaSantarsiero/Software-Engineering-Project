package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.model.shipboard.Level1ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.Level2ShipBoard;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.model.shipcard.HousingUnit;
import java.io.Serializable;
import java.util.Map;


/**
 * Rappresenta un giocatore nel gioco, con attributi come username, colore, monete, posizione,
 * e una {@link ShipBoard} associata che rappresenta la sua astronave.
 *
 * <p>Ogni giocatore può scegliere un colore, posizionarsi, ottenere/rimuovere monete
 * e abbandonare una partita. Inoltre, può selezionare il tipo di plancia di volo e manipolarla.</p>
 */
public class Player implements Serializable {

    private final String username;
    private Color color;
    private int coins;
    private int position;
    private boolean abort;
    private ShipBoard shipBoard;



    public enum Color implements Serializable{
        RED, GREEN, BLUE, YELLOW;
    }


    /**
     * Costruisce un nuovo giocatore con lo username specificato.
     * Il colore e la shipBoard sono inizialmente null.
     * Le monete sono inizializzate a 0, e la posizione a -1.
     *
     * @param username il nome identificativo del giocatore
     */
    public Player(String username) {
        this.username = username;
        this.color = null;
        this.coins = 0;
        this.position = -1;
        this.abort = false;
        this.shipBoard = null;
    }


    /**
     * Restituisce il colore del giocatore come stringa in minuscolo.
     *
     * @return stringa con il colore, oppure {@code null} se non assegnato
     */
    public String getColorToString() {
        if (this.color == null) {
            return null;
        }
        return color.toString().toLowerCase();
    }
    /**
     * Restituisce il colore del giocatore.
     *
     * @return un valore dell'enum {@link Color}
     */
    public Player.Color getColor() {
        return color;
    }

    /**
     * Assegna un colore al giocatore e imposta l'unità centrale corrispondente
     * nella sua plancia di gioco.
     *
     * @param color         il colore in formato stringa minuscola ("red", "green", ...)
     * @param centralUnits  mappa colore → {@link HousingUnit} centrale
     */
    public void setColor(String color, Map<String, HousingUnit> centralUnits) {
        switch (color) {
            case "red" -> {
                this.color = Color.RED;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
            case "green" -> {
                this.color = Color.GREEN;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
            case "blue" -> {
                this.color = Color.BLUE;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
            case "yellow" -> {
                this.color = Color.YELLOW;
                shipBoard.setCentralUnit(centralUnits.get(color));
            }
        }
    }

    /**
     * Restituisce lo username del giocatore.
     *
     * @return il nome utente del giocatore
     */
    public String getUsername() {
        return username;
    }
    /**
     * Restituisce il numero attuale di monete del giocatore.
     *
     * @return numero di monete possedute
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Aggiunge un numero positivo di monete al giocatore.
     *
     * @param delta numero di monete da aggiungere
     * @throws IllegalArgumentException se {@code delta < 0}
     */
    public void addCoins(int delta) {
        if(delta < 0) {
            throw new IllegalArgumentException("Delta value cannot be negative");
        }

        this.coins += delta;
    }
    /**
     * Rimuove un numero positivo di monete dal giocatore.
     * Se il numero da rimuovere supera il totale, il conto viene azzerato.
     *
     * @param delta numero di monete da rimuovere
     * @throws IllegalArgumentException se {@code delta < 0}
     */
    public void removeCoins(int delta) {
        if(delta < 0) {
            throw new IllegalArgumentException("Delta value cannot be negative");
        }

        if (coins > delta) {
            coins -= delta;
        } else {
            coins = 0;
        }
    }
    /**
     * Restituisce la posizione del giocatore nell'ordine di turno o su una mappa.
     *
     * @return posizione del giocatore
     */
    public int getPosition() {
        return position;
    }
    /**
     * Imposta la posizione del giocatore.
     *
     * @param position la nuova posizione
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Indica se il giocatore ha abbandonato la partita.
     *
     * @return {@code true} se il giocatore ha abbandonato
     */
    public boolean isAbort() {return abort;}

    /**
     * Segna il giocatore come "ritirato" (abbandono).
     */
    public void setAbort() { this.abort = true; }

    /**
     * Assegna una {@link ShipBoard} al giocatore, in base al tipo di volo scelto.
     * <ul>
     *     <li>{@code TRIAL} → {@link Level1ShipBoard}</li>
     *     <li>{@code LEVEL2} → {@link Level2ShipBoard}</li>
     * </ul>
     *
     * @param flightType il tipo di volo scelto
     * @throws NullPointerException se {@code flightType} è {@code null}
     * @throws IllegalArgumentException se {@code flightType} non è riconosciuto
     */
    public void setShipBoard(FlightBoard.Type flightType) {
        if (flightType == null)
            throw new NullPointerException();
        if (flightType == FlightBoard.Type.TRIAL)
            this.shipBoard = new Level1ShipBoard();
        else if (flightType == FlightBoard.Type.LEVEL2)
            this.shipBoard = new Level2ShipBoard();
        else
            throw new IllegalArgumentException();
    }

    /**
     * Restituisce la plancia di gioco del giocatore.
     *
     * @return la {@link ShipBoard} assegnata
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }



    //cheating methods
    /**
     * Metodo di cheating che consente di impostare manualmente una plancia arbitraria.
     * Utile per test o modalità speciali.
     *
     * @param coolShip la plancia da assegnare direttamente
     */
    public void setACoolShip(ShipBoard coolShip) {
        this.shipBoard = coolShip;
    }
}

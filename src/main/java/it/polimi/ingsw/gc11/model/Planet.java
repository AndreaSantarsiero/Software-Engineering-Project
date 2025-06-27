package it.polimi.ingsw.gc11.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Rappresenta un pianeta nel gioco, che può contenere materiali di diversi tipi
 * e può essere visitato da un giocatore.
 * <p>
 * Ogni pianeta ha una lista di {@link Material} inizializzata al momento della costruzione
 * e può essere marcato come "visitato" da un {@link Player}. La classe implementa {@link Serializable}
 * per supportare la serializzazione del suo stato.
 * </p>
 */
public class Planet implements Serializable {

    private boolean visited;
    private final ArrayList<Material> materials;
    private Player player;

    /**
     * Costruisce un nuovo pianeta contenente un numero specificato di materiali
     * di ciascun colore (blu, verde, giallo, rosso).
     *
     * @param numBlue   il numero di materiali blu da aggiungere
     * @param numGreen  il numero di materiali verdi da aggiungere
     * @param numYellow il numero di materiali gialli da aggiungere
     * @param numRed    il numero di materiali rossi da aggiungere
     */
    public Planet(int numBlue, int numGreen, int numYellow, int numRed) {
        materials = new ArrayList<>();
        visited = false;
        player = null;
        //add materials
        for (int i = 0; i < numBlue; i++)
            materials.add(new Material(Material.Type.BLUE));
        for (int i = 0; i < numGreen; i++)
            materials.add(new Material(Material.Type.GREEN));
        for (int i = 0; i < numYellow; i++)
            materials.add(new Material(Material.Type.YELLOW));
        for (int i = 0; i < numRed; i++)
            materials.add(new Material(Material.Type.RED));
    }

    /**
     * Indica se il pianeta è già stato visitato da un giocatore.
     *
     * @return {@code true} se il pianeta è stato visitato, {@code false} altrimenti
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Imposta lo stato del pianeta come visitato e registra il giocatore che lo ha visitato.
     *
     * @param player il giocatore che visita il pianeta
     */
    public void setVisited(Player player) {
        this.visited = true;
        this.player = player;
    }

    /**
     * Restituisce il giocatore che ha visitato il pianeta.
     *
     * @return il giocatore associato, o {@code null} se non ancora visitato
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Restituisce la lista dei materiali presenti sul pianeta.
     *
     * @return lista di oggetti {@link Material}
     */
    public ArrayList<Material> getMaterials() { return materials;}
}

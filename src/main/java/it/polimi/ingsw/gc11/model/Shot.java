package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;


/**
 * Rappresenta un colpo sparato (Shot) che può colpire una plancia di gioco.
 * È una specifica implementazione concreta della classe astratta {@link Hit}.
 *
 * <p>Un {@code Shot} è caratterizzato da un tipo (BIG o SMALL) e da una direzione
 * (TOP, RIGHT, BOTTOM, LEFT) da cui proviene. Viene utilizzato per modellare eventi
 * che causano danno controllato da parte del giocatore o del sistema.</p>
 */
public class Shot extends Hit {

    /**
     * Costruisce un nuovo {@code Shot} con tipo e direzione specificati.
     *
     * @param type il tipo del colpo (BIG o SMALL)
     * @param direction la direzione da cui proviene il colpo (TOP, RIGHT, BOTTOM, LEFT)
     */
    public Shot(Hit.Type type, Hit.Direction direction) {
        super(type, direction);
    }

    /**
     * Visualizza il colpo sull'interfaccia testuale utilizzando il {@link AdventureTemplate} fornito.
     * Questo metodo è chiamato per rappresentare graficamente il colpo nella vista CLI.
     *
     * @param adventureTemplate il template utilizzato per visualizzare il colpo
     */
    @Override
    public void print(AdventureTemplate adventureTemplate){
        adventureTemplate.setHitType(this);
    }
}

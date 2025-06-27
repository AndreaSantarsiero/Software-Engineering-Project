package it.polimi.ingsw.gc11.model;

import it.polimi.ingsw.gc11.view.cli.templates.AdventureTemplate;


/**
 * Rappresenta un colpo di tipo meteorite che può colpire l'astronave.
 * Un {@code Meteor} è una particolare istanza di {@link Hit}, con tipo e direzione specificati.
 *
 * <p>Viene utilizzato per modellare i meteoriti che impattano sulla plancia di gioco,
 * e può essere visualizzato tramite un {@link AdventureTemplate} nell'interfaccia testuale.</p>
 */
public class Meteor extends Hit {

    /**
     * Costruisce un nuovo {@code Meteor} con il tipo e la direzione specificati.
     *
     * @param type il tipo del meteorite (BIG o SMALL)
     * @param direction la direzione da cui arriva il meteorite (TOP, RIGHT, BOTTOM, LEFT)
     */
    public Meteor(Hit.Type type, Hit.Direction direction) {
        super(type, direction);
    }


    /**
     * Visualizza il meteorite utilizzando il {@link AdventureTemplate} fornito.
     * Chiama il metodo {@code setHitType} sul template, passando sé stesso.
     *
     * @param adventureTemplate il template CLI utilizzato per visualizzare il meteorite
     */
    @Override
    public void print(AdventureTemplate adventureTemplate){
        adventureTemplate.setHitType(this);
    }
}

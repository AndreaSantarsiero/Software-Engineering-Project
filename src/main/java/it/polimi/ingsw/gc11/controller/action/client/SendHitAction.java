package it.polimi.ingsw.gc11.controller.action.client;

import it.polimi.ingsw.gc11.model.Hit;

public class SendHitAction extends ServerAction{
    private Hit hit;

    public SendHitAction(Hit hit) {
        this.hit = hit;
    }

    public Hit getHit() {
        return hit;
    }

    @Override
    public void execute() {}
}

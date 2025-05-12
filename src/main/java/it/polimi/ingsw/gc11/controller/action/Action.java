package it.polimi.ingsw.gc11.controller.action;

import it.polimi.ingsw.gc11.controller.GameContext;
import java.io.Serializable;



public interface Action extends Serializable {
    void execute(GameContext gameContext);
}

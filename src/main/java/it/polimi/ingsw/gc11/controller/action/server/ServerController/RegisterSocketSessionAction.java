package it.polimi.ingsw.gc11.controller.action.server.ServerController;

import it.polimi.ingsw.gc11.controller.ServerController;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.exceptions.UsernameAlreadyTakenException;
import java.util.UUID;



public class RegisterSocketSessionAction extends ClientControllerAction {

    public RegisterSocketSessionAction(String username) {
        super(username, null);
    }


    @Override
    public void execute(ServerController serverController) throws NetworkException {
        try {
            UUID token = serverController.registerSocketSession(username);
            //rispondi con il token
        } catch (UsernameAlreadyTakenException e) {

        }
    }
}

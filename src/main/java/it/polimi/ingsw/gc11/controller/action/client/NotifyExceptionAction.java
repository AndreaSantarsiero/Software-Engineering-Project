package it.polimi.ingsw.gc11.controller.action.client;

public class NotifyExceptionAction extends ServerAction {
    private final String message;

    public NotifyExceptionAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void execute() {

    }
}

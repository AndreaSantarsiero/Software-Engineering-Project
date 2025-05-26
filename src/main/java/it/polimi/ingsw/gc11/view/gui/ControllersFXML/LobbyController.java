package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class LobbyController {

    @FXML
    private TableView<Player> playersTable;

    @FXML
    protected void onEnterButtonClick(ActionEvent event) {}
}

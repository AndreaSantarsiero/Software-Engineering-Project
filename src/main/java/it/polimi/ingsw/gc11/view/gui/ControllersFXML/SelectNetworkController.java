package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SelectNetworkController {

    @FXML
    private Button rmiButton;

    @FXML
    private Button socketButton;

    @FXML
    protected void onRMIButtonClick(ActionEvent event) {
        System.out.println("RMI selected");


    }

    @FXML
    protected void onSOCKETButtonClick(ActionEvent event) {
        System.out.println("SOCKET selected");


    }


}

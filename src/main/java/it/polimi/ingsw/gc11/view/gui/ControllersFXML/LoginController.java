package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button enterButton;
    @FXML
    private TextField usernameText;
    @FXML
    private HBox status;
    @FXML
    private Label label2;
    @FXML
    private HBox match;
    @FXML
    private Button join;
    @FXML
    private Button create;

    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

        Scene scene = enterButton.getScene();
        ViewModel viewModel = (ViewModel) scene.getUserData();
        Stage stage = (Stage) scene.getWindow();
        String username = usernameText.getText();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        try {
            virtualServer.registerSession(username);
            viewModel.setMyself(username);
            enterButton.setVisible(false);

            status.setVisible(true);
            label2.setText("You are logged in as: " + username);
            label2.setStyle("-fx-text-fill: green;" + label2.getStyle());
            System.out.println("You are logged in as: " + username);

            match.setVisible(true);
        }
        catch (Exception e) {
            label2.setText("Error");
            label2.setStyle("-fx-text-fill: red;" + label2.getStyle());
            System.out.println(e.getMessage());
        }

    }

    @FXML
    protected void onCreateMatchClick(ActionEvent event){

    }

    @FXML
    protected void onJoinMatchClick(ActionEvent event){

    }
}

package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Template;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends Template {

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

    private Stage stage;

    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

        Scene scene = enterButton.getScene();
        this.stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        String username = usernameText.getText();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setListener(this);

        try {
            virtualServer.registerSession(username);
        }
        catch (Exception e) {
            label2.setText("Error");
            label2.setStyle("-fx-text-fill: red;" + label2.getStyle());
            System.out.println("Error:  " + e.getMessage());
        }

    }


    @FXML
    protected void onCreateMatchClick(ActionEvent event){

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        joiningPhaseData.setCreateOrJoinMenu(0); //create
        joiningPhaseData.updateState();
        this.update(joiningPhaseData);

    }


    @FXML
    protected void onJoinMatchClick(ActionEvent event){

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        joiningPhaseData.setCreateOrJoinMenu(1); //join
        joiningPhaseData.updateState();
        this.update(joiningPhaseData);
    }


    @Override
    public void update(JoiningPhaseData joiningPhaseData) {

        if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.USERNAME_SETUP){
            label2.setText("An errror occured");
            label2.setStyle("-fx-text-fill: red;" + label2.getStyle());
            System.out.println("Error:  " + joiningPhaseData.getServerMessage());
        }
        else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
            enterButton.setVisible(false);
            status.setVisible(true);
            label2.setText("You are logged in as:   " + joiningPhaseData.getUsername());
            label2.setStyle("-fx-text-fill: green;" + label2.getStyle());
            System.out.println("You are logged in as:   " + joiningPhaseData.getUsername());
            match.setVisible(true);
        }
        else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_LEVEL) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/CreateMatch.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());

                System.out.println(joiningPhaseData.getUsername() + ": clicked on create a new match");
                this.stage.setScene(newScene);
                this.stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_GAME) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/JoinMatch.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());

                System.out.println(joiningPhaseData.getUsername() + ": clicked on join a match");
                this.stage.setScene(newScene);
                this.stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

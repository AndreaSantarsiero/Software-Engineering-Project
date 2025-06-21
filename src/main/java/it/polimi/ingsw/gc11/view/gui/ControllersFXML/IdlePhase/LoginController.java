package it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
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
import java.io.IOException;

public class LoginController extends Controller {

    @FXML
    private Button enterButton;
    @FXML
    private TextField usernameText;
    @FXML
    private HBox status;
    @FXML
    private Label errorLabel;

    private Stage stage;


    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

        errorLabel.setVisible(false);

        Scene scene = enterButton.getScene();
        this.stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        String username = usernameText.getText();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.USERNAME_SETUP); //curr_state = USERNAME_SETUP

        try {
            virtualServer.registerSession(username);
        }
        catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Network Error");
            errorLabel.setStyle("-fx-text-fill: red;" + errorLabel.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }

    }



    @Override
    public void update(JoiningPhaseData joiningPhaseData) {

        Platform.runLater(() -> {

            //Can't register the session
            if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_USERNAME){
                errorLabel.setVisible(true);
                errorLabel.setText("Error:  " + joiningPhaseData.getServerMessage());
                System.out.println("Error:  " + joiningPhaseData.getServerMessage());
                joiningPhaseData.resetServerMessage();
            }

            //Session registered successfully
            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
                enterButton.setDisable(true);
                status.setVisible(true);
                errorLabel.setText("You are logged in as:   " + joiningPhaseData.getUsername());
                errorLabel.setStyle("-fx-text-fill: green;" + errorLabel.getStyle());
                System.out.println("You are logged in as:   " + joiningPhaseData.getUsername());
                while (true) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/CreateOrJoin.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load());
                        CreateOrJoinController controller = fxmlLoader.getController();
                        controller.setStage(stage);
                        //Delay
                        Task<Void> sleeper = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(event -> {
                            stage.setScene(newScene);
                            stage.show();
                        });
                        new Thread(sleeper).start();
                        joiningPhaseData.setListener(controller);
                        break;
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        });
    }

}

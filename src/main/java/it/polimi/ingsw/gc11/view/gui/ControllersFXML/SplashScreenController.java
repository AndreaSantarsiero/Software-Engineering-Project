package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class SplashScreenController {

    @FXML
    Button playButton;

    @FXML
    protected void onPlayButtonClick(ActionEvent event) {

        Scene scene = playButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.CONNECTION_SETUP); //curr_state = CONNECTION_SETUP

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SelectNetwork.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            stage.setScene(newScene);
            stage.show();
            joiningPhaseData.setListener(fxmlLoader.getController());
        }
        catch (IOException e) {
            System.out.println("Error: Can't load Splash Screen.");
        }

    }
}

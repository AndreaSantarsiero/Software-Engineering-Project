package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

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

        System.out.println("onPlayButtonClick");
        Scene scene = playButton.getScene();
        Stage stage = (Stage) scene.getWindow();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SelectNetwork.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            stage.setScene(newScene);
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException("Can't load Splash Screen.");
        }

    }
}

package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.view.gui.MainGUI;
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

        Stage stage = (Stage) playButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SelectNetwork.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

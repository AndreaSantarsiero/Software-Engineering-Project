package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.view.PlayerContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewModel viewModel = new ViewModel();
        stage.setTitle("Galaxy Trucker");

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SplashScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 860, 640);
        stage.setUserData(viewModel);

        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}
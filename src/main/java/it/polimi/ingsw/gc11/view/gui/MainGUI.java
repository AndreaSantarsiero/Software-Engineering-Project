package it.polimi.ingsw.gc11.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;



public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private ViewModel viewModel;

    @Override
    public void start(Stage stage) throws IOException {
        this.viewModel = new ViewModel();

        stage.setTitle("Galaxy Trucker");

        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SplashScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setUserData(this.viewModel);

        stage.setScene(scene);
        stage.show();
    }
}
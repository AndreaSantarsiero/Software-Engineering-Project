package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import it.polimi.ingsw.gc11.view.cli.templates.JoiningTemplate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;



public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        PlayerContext context = new PlayerContext();
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new JoiningTemplate());

        data.notifyListener();

        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SplashScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Galaxy Trucker");

        stage.setScene(scene);
        stage.show();
    }
}
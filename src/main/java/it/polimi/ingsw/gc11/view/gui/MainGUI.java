package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;



public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws IOException {
        List<String> args = getParameters().getRaw();
        String serverIp = null;
        Integer serverPort = null;

        if(!args.isEmpty()) {
            serverIp = args.getFirst();
        }
        if(args.size() > 1) {
            serverPort = Integer.parseInt(args.get(1));
        }

        ViewModel viewModel = new ViewModel(serverIp, serverPort);
        stage.setTitle("Galaxy Trucker");

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/SplashScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 860, 640);
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        joiningPhaseData.setListener(loader.getController());
        stage.setUserData(viewModel);

        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}
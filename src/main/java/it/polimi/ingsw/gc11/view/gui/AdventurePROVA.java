package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase.AdventureControllerLv1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;



public class AdventurePROVA extends Application {

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
        stage.setUserData(viewModel);
        stage.setTitle("Galaxy Trucker");

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/AdventurePhase/AdventureLv1.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 720);
        viewModel.getPlayerContext().setAdventurePhase();
        AdventurePhaseData adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();
        AdventureControllerLv1 controller = (AdventureControllerLv1) loader.getController();
        adventurePhaseData.setListener(controller);
        controller.initialize(stage);

        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

    }

}

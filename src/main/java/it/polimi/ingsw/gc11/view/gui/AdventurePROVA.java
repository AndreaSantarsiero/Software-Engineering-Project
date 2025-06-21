package it.polimi.ingsw.gc11.view.gui;


import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.AdventurePhase.AdventureController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class AdventurePROVA extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewModel viewModel = new ViewModel();
        stage.setUserData(viewModel);
        stage.setTitle("Galaxy Trucker");

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Adventure.fxml"));
        Scene scene = new Scene(loader.load(), 1400, 780);
        viewModel.getPlayerContext().setAdventurePhase();
        AdventurePhaseData adventurePhaseData = (AdventurePhaseData) viewModel.getPlayerContext().getCurrentPhase();
        AdventureController controller = (AdventureController) loader.getController();
        adventurePhaseData.setListener(controller);
        controller.initialize(stage);

        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

    }

}

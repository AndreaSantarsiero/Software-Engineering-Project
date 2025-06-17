package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase.BuildingLv2Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class BuildingLv2PROVA extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewModel viewModel = new ViewModel();
        stage.setUserData(viewModel);
        stage.setTitle("Galaxy Trucker");

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/oldBuildingLv2.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 720);
        viewModel.getPlayerContext().setBuildingPhase();
        BuildingPhaseData buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        BuildingLv2Controller controller = (BuildingLv2Controller) loader.getController();
        buildingPhaseData.setListener(controller);
        //controller.initialize(stage);

        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

}

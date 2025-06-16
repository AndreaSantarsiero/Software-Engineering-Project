package it.polimi.ingsw.gc11.view.gui;

import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase.BuildingLv1Controller;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase.BuildingLv2Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class BuildingLv1PROVA extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewModel viewModel = new ViewModel();
        stage.setUserData(viewModel);
        stage.setTitle("Galaxy Trucker");

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/BuildingLv1.fxml"));
        Scene scene = new Scene(loader.load(), 1400, 780);
        viewModel.getPlayerContext().setBuildingPhase();
        BuildingPhaseData buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
        BuildingLv1Controller controller = (BuildingLv1Controller) loader.getController();
        buildingPhaseData.setListener(controller);
        //controller.initialize(stage);

        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

        controller.initialize(stage);
    }

}

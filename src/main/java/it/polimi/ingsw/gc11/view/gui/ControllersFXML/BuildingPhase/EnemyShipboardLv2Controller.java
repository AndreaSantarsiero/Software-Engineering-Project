package it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EnemyShipboardLv2Controller extends Controller {

    @FXML private GridPane slotGrid;
    @FXML private Button goBackButton;

    private Stage stage;
    private BuildingPhaseData buildingPhaseData;

    public void initialize(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
    }

    @FXML
    protected void onGoBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/buildingLV2.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
            BuildingLv2Controller controller = fxmlLoader.getController();
            buildingPhaseData.setListener(controller);
            controller.initialize(stage);
            stage.setScene(newScene);
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

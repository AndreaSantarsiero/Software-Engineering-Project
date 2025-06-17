package it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.BuildingPhaseData;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase.BuildingLv1Controller;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.BuildingPhase.BuildingLv2Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;



public class LobbyController extends Controller {

    @FXML
    private TableView<Map.Entry<String, String>> playersTable;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> playerColumn;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> colorColumn;
    @FXML
    private Label label;

    private Stage stage;
    private JoiningPhaseData joiningPhaseData;


    public void init(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        //curr_state = WAITING

        // Configure columns using lambda expressions to access key/value of Map.Entry
        playerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        colorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));

        this.setPlayersInfo();
    }

    private void setPlayersInfo(){

        Map<String, String> playersColor = joiningPhaseData.getPlayersColor();

        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(playersColor.entrySet());
        playersTable.setItems(items);

    }

    @Override
    public void update(JoiningPhaseData joiningPhaseData) {
        Platform.runLater(() -> {

            if(joiningPhaseData.getState() == JoiningPhaseData.JoiningState.WAITING) {
                this.setPlayersInfo();
            }

        });
    }



    @Override
    public void change() {

        Platform.runLater(() -> {

            ViewModel viewModel = (ViewModel) stage.getUserData();
            BuildingPhaseData buildingPhaseData = (BuildingPhaseData) viewModel.getPlayerContext().getCurrentPhase();
            while (true) {
                try {
                    if (buildingPhaseData.getFlightType().equals(FlightBoard.Type.TRIAL)) {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/buildingLV1.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load(), 1400, 780);
                        BuildingLv1Controller controller = fxmlLoader.getController();
                        buildingPhaseData.setListener(controller);
                        controller.initialize(stage);
                        stage.setScene(newScene);
                        stage.show();
                        break;
                    }
                    else if (buildingPhaseData.getFlightType().equals(FlightBoard.Type.LEVEL2)) {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/BuildingLV2.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load(), 1400, 780);
                        BuildingLv2Controller controller = fxmlLoader.getController();
                        buildingPhaseData.setListener(controller);
                        controller.initialize(stage);
                        stage.setScene(newScene);
                        stage.show();
                        break;
                    } else {
                        System.out.println("Error: " + buildingPhaseData.getFlightType());
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        });
    }
}

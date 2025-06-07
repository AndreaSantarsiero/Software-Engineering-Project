package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Template;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;



public class CreateMatchController extends Template implements Initializable {

    @FXML
    private ComboBox<String> flightType;
    @FXML
    private ComboBox<String> numberOfPlayers;
    private final String[] flightTypes = {"Trial", "Lv 2"};
    private final String[] numberPlayers = {"2", "3", "4"};
    @FXML
    private Button enterButton;
    @FXML
    private Label label;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flightType.getItems().addAll(flightTypes);
        numberOfPlayers.getItems().addAll(numberPlayers);
    }



    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

        Scene scene = enterButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();


        String selectedFlightTypeString = flightType.getValue();
        FlightBoard.Type selectedFlightType = null;
        if (selectedFlightTypeString.equals("Trial")) {
            selectedFlightType = FlightBoard.Type.TRIAL;
        }
        else if (selectedFlightTypeString.equals("Lv 2")) {
            selectedFlightType = FlightBoard.Type.LEVEL2;
        }
        int selectedNumberOfPlayers = Integer.parseInt(numberOfPlayers.getValue());


        try {
            virtualServer.createMatch(selectedFlightType, selectedNumberOfPlayers);

            FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Lobby.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            stage.setScene(newScene);
            stage.show();

            LobbyController controller = fxmlLoader.getController();
            controller.setVirtualServer(virtualServer);

            controller.init();

            System.out.println( joiningPhaseData.getUsername() + ": created a new match of type " +
                    selectedFlightTypeString  + " with " + selectedNumberOfPlayers + " players" );
        }
        catch (Exception e) {
            label.setVisible(true );
            label.setText(e.getMessage());
            label.setStyle("-fx-text-fill: red;" + label.getStyle());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void update(JoiningPhaseData joiningPhaseData) {}

    @Override
    public void change() {}
}

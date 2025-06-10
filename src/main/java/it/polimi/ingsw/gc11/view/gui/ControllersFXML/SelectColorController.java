package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Template;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SelectColorController extends Template {

    @FXML
    private ComboBox<String> availableColors;
    @FXML
    private Button enterButton;
    @FXML
    private Label label;

    private Stage stage;
    private JoiningPhaseData joiningPhaseData;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {

        //curr_state = CHOOSE_COLOR

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        joiningPhaseData.resetServerMessage();

        try {
            virtualServer.getPlayersColor(); // calls update
            this.setAvailableColors();
        }
        catch (Exception e) {
            label.setVisible(true);
            label.setText(e.getMessage());
            label.setStyle("-fx-text-fill: red;" + label.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }

    }

    private void setAvailableColors(){
        String[] allColors = { "red", "blue", "green", "yellow" };
        ArrayList<String> avColors = new ArrayList<>(List.of(allColors));
        Map<String, String> playersColor = joiningPhaseData.getPlayersColor();
        if (playersColor != null) {
            avColors.removeAll(joiningPhaseData.getPlayersColor().values());
        }
        availableColors.getItems().clear();
        availableColors.getItems().addAll(avColors.toArray(String[]::new));
    }

    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

         if(availableColors.getValue() == null ) {
           return;
        }

        ViewModel viewModel = (ViewModel) stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        String selectedColorString = availableColors.getValue();

        //Change state and call update
        joiningPhaseData.setState(JoiningPhaseData.JoiningState.COLOR_SETUP);

        try {
            virtualServer.chooseColor(selectedColorString);
        }
        catch (Exception e) {
            label.setVisible(true);
            label.setText(e.getMessage());
            label.setStyle("-fx-text-fill: red;" + label.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }
    }

    @Override
    public void update(JoiningPhaseData joiningPhaseData) {

        Platform.runLater(() -> {

            if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_COLOR) {
                //Can't load available colors or can't choose selected color
                if ( !joiningPhaseData.getServerMessage().isEmpty()) {
                    label.setVisible(true);
                    label.setText("An errror has occured, try again");
                    label.setStyle("-fx-text-fill: red;" + label.getStyle());
                    System.out.println("Error:  " + joiningPhaseData.getServerMessage());
                    joiningPhaseData.resetServerMessage();
                }
                else {
                    this.setAvailableColors();
                }
            }


            //Color setup sucessfull
            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.WAITING) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Lobby.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    LobbyController controller = fxmlLoader.getController();
                    controller.setStage(this.stage);
                    controller.init();
                    stage.setScene(newScene);
                    stage.show();
                    joiningPhaseData.setListener(controller);

                    System.out.println(joiningPhaseData.getUsername() + ": selected color " +
                            joiningPhaseData.getPlayersColor().get(joiningPhaseData.getUsername()));
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }
}

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
import java.util.List;

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

        //curr_state = GAME_SETUP

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        joiningPhaseData.setState(JoiningPhaseData.JoiningState.CHOOSE_COLOR);

        try {
            virtualServer.getPlayersColor(); // calls update
            //Change state and call update
            joiningPhaseData.setState(JoiningPhaseData.JoiningState.COLOR_SETUP);
        }
        catch (Exception e) {
            label.setVisible(true);
            label.setText(e.getMessage());
            label.setStyle("-fx-text-fill: red;" + label.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }

        String[] col = { "red", "blue", "green", "yellow" };
       availableColors.getItems().addAll(col);

    }

    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

         if(availableColors.getValue() == null ) {
           return;
        }

        ViewModel viewModel = (ViewModel) stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        //curr_state = COLOR_SETUP

        String selectedColorString = availableColors.getValue();

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
                if(joiningPhaseData.getServerMessage() != null) {
                    label.setVisible(true);
                    label.setText("An errror has occured, try again");
                    label.setStyle("-fx-text-fill: red;" + label.getStyle());
                    System.out.println("Error:  " + joiningPhaseData.getServerMessage());
                }
                else {
                    ArrayList<String> allColors = new ArrayList<>();
                    allColors.add("red");
                    allColors.add("blue");
                    allColors.add("green");
                    allColors.add("yellow");
                    allColors.removeAll(joiningPhaseData.getPlayersColor().values());
                    //String[] col = { "red", "blue", "green", "yellow" };
                    availableColors.getItems().addAll(allColors);

                }
            }

            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.COLOR_SETUP) {
                enterButton.setVisible(true);
            }

            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.WAITING) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/Lobby.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    LobbyController controller = fxmlLoader.getController();
                    joiningPhaseData.setListener(controller);
                    controller.setStage(this.stage);
                    controller.init();
                    stage.setScene(newScene);
                    stage.show();

                    System.out.println(joiningPhaseData.getUsername() + ": selected color " +
                            joiningPhaseData.getPlayersColor().get(joiningPhaseData.getUsername()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }
}

package it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase;

import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.model.FlightBoard;
import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.ControllersFXML.EndGamePhase.EndGameController;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;



public class CreateMatchController extends Controller {

    @FXML
    private ComboBox<String> flightType;
    private final String[] flightTypes = {"Trial", "Lv 2"};
    @FXML
    private ComboBox<String> numberOfPlayers;
    private final String[] numberPlayers = {"2", "3", "4"};
    @FXML
    private Button enterButton;
    @FXML
    private Label label;

    private Stage stage;
    private String selectedFlightTypeString;
    private  int selectedNumberOfPlayers;


    public void init() {
        flightType.getItems().addAll(flightTypes);
        numberOfPlayers.getItems().addAll(numberPlayers);
    }


    @FXML
    protected void onEnterButtonClick(ActionEvent event) {

        if(flightType.getValue() == null || numberOfPlayers.getValue() == null) {
            return;
        }

        Scene scene = enterButton.getScene();
        this.stage = (Stage) scene.getWindow();
        ViewModel viewModel = (ViewModel) stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        JoiningPhaseData joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        //joiningPhaseData.setState(JoiningPhaseData.JoiningState.CHOOSE_NUM_PLAYERS);

        this.selectedFlightTypeString = flightType.getValue();
        FlightBoard.Type selectedFlightType = null;
        if (selectedFlightTypeString.equals("Trial")) {
            selectedFlightType = FlightBoard.Type.TRIAL;
        }
        else if (selectedFlightTypeString.equals("Lv 2")) {
            selectedFlightType = FlightBoard.Type.LEVEL2;
        }
        this.selectedNumberOfPlayers = Integer.parseInt(numberOfPlayers.getValue());

        joiningPhaseData.setState(JoiningPhaseData.JoiningState.GAME_SETUP);

        try {
            virtualServer.createMatch(selectedFlightType, selectedNumberOfPlayers);
            enterButton.setDisable(true);
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

            //Match created successfully
            if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_COLOR) {
                while(true) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/IdlePhase/SelectPlayerColor.fxml"));
                        Scene newScene = new Scene(fxmlLoader.load());
                        SelectColorController controller = fxmlLoader.getController();
                        stage.setScene(newScene);
                        stage.setFullScreen(true);
                        stage.show();
                        controller.setStage(this.stage);
                        joiningPhaseData.setListener(controller);
                        controller.init();

                        System.out.println(joiningPhaseData.getUsername() + ": created a new match of type " +
                                this.selectedFlightTypeString + " with " + this.selectedNumberOfPlayers + " players");
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            //Can't create a match, new state is create_or_join a match
            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
                enterButton.setDisable(true);
                label.setVisible(true);
                label.setText("Error:  " + joiningPhaseData.getServerMessage());
                label.setStyle("-fx-text-fill: red;" + label.getStyle());
                System.out.println("Error:  " + joiningPhaseData.getServerMessage());

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/IdlePhase/CreateOrJoin.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    CreateOrJoinController controller = fxmlLoader.getController();
                    controller.setStage(this.stage);
                    //Delay
                    Task<Void> sleeper = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {}
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(event -> {
                        stage.setScene(newScene);
                        stage.setFullScreen(true);
                        stage.show();
                    });
                    joiningPhaseData.setListener(controller);
                    new Thread(sleeper).start();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }

    @Override
    public void change() {
        Platform.runLater(() -> {
            ViewModel viewModel = (ViewModel) stage.getUserData();
            if ( viewModel.getPlayerContext().getCurrentPhase().isEndPhase() ){
                EndPhaseData endPhaseData = (EndPhaseData) viewModel.getPlayerContext().getCurrentPhase();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class
                            .getResource("/it/polimi/ingsw/gc11/gui/EndGamePhase/Endgame.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 1280, 720);
                    EndGameController controller = fxmlLoader.getController();
                    endPhaseData.setListener(controller);
                    controller.init(stage);
                    stage.setScene(newScene);
                    stage.setFullScreen(true);
                    stage.show();
                }
                catch (Exception e) {
                    System.out.println("FXML Error: " + e.getMessage());
                }
            }
        });
    }
}

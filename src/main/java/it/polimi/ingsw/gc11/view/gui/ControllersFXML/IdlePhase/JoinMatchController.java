package it.polimi.ingsw.gc11.view.gui.ControllersFXML.IdlePhase;

import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.gui.MainGUI;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JoinMatchController extends Controller {

    @FXML
    private TableView<MatchEntry> matchTable;

    @FXML
    private TableColumn<MatchEntry, String> matchIdColumn;

    @FXML
    private TableColumn<MatchEntry, String> playersColumn;

    @FXML
    private Button joinButton;

    @FXML
    private Label errorLabel;

    private Stage stage;
    private JoiningPhaseData joiningPhaseData;

    public static class MatchEntry {
        private final SimpleStringProperty matchId;
        private final SimpleStringProperty players;

        public MatchEntry(String matchId, List<String> players) {
            this.matchId = new SimpleStringProperty(matchId);
            this.players = new SimpleStringProperty(String.join(", ", players));
        }

        public String getMatchId() { return matchId.get(); }
        public String getPlayers() { return players.get(); }
    }


    public void init(Stage stage) {

        this.stage = stage;

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        matchIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatchId()));
        playersColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlayers()));

        try{
            virtualServer.getAvailableMatches();
        }
        catch(Exception e){
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;" + errorLabel.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }

    }

    private void setMatchInfo(){
        Map<String, List<String>> availableMatches = joiningPhaseData.getAvailableMatches();
        List<MatchEntry> matchList = new ArrayList<MatchEntry>();

        for (Map.Entry<String, List<String>> entry : availableMatches.entrySet()) {
            matchList.add(new MatchEntry(entry.getKey(), entry.getValue()));
        }

        matchTable.getItems().setAll(matchList);
    }

    @FXML
    protected void onJoinMatchClick(ActionEvent event) {

        MatchEntry selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        ViewModel viewModel = (ViewModel) stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        joiningPhaseData.setState(JoiningPhaseData.JoiningState.GAME_SETUP);

        String selectedMatchId = selected.getMatchId();

        try {
            virtualServer.connectToGame(selectedMatchId);
            joinButton.setDisable(true);
        }
        catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;" + errorLabel.getStyle());
            System.out.println("Network Error:  " + e.getMessage());
        }

    }


    @Override
    public void update(JoiningPhaseData joiningPhaseData) {
        Platform.runLater(() -> {

            if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_GAME) {
                this.setMatchInfo();
            }

            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CHOOSE_COLOR) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("/it/polimi/ingsw/gc11/gui/IdlePhase/SelectPlayerColor.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    SelectColorController controller = fxmlLoader.getController();
                    stage.setScene(newScene);
                    stage.show();
                    joiningPhaseData.setListener(controller);
                    controller.setStage(this.stage);
                    controller.init();

                    System.out.println(joiningPhaseData.getUsername() + ": joined " +
                            matchTable.getSelectionModel().getSelectedItem().getMatchId());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (joiningPhaseData.getState() == JoiningPhaseData.JoiningState.CREATE_OR_JOIN) {
                joinButton.setDisable(true);
                errorLabel.setVisible(true);
                errorLabel.setText("Error:  " + joiningPhaseData.getServerMessage());
                errorLabel.setStyle("-fx-text-fill: red;" + errorLabel.getStyle());
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

}

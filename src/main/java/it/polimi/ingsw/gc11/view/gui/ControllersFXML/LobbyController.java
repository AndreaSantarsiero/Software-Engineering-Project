package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.Template;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Map;

public class LobbyController extends Template {

    @FXML
    private TableView<Player> playersTable;
    @FXML
    private TableColumn<Player, String> playerColumn;
    @FXML
    private TableColumn<Player, Player.Color> colorColumn;
    @FXML
    private Label label;

    private Stage stage;
    private JoiningPhaseData joiningPhaseData;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.joiningPhaseData = (JoiningPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        //curr_stage = GAME_SETUP

        playerColumn = new TableColumn<>("Username");
        colorColumn = new TableColumn<>("Color");
        try {
            virtualServer.getPlayersColor();  //usare JoiningPhaseData
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

            if(joiningPhaseData.getState() == JoiningPhaseData.JoiningState.GAME_SETUP) {
                Map<String, String> player_color = joiningPhaseData.getPlayersColor();
                for (Map.Entry<String, String> entry : player_color.entrySet()) {
                    playerColumn.setCellValueFactory(new PropertyValueFactory<>(entry.getKey()));
                    colorColumn.setCellValueFactory(new PropertyValueFactory<>(entry.getValue()));
                }
//            ObservableList<Player> players = (ObservableList<Player>) virtualServer.getPlayers();
//            playersTable.setItems(players);

                playerColumn = new TableColumn<>("Username");
                playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

                colorColumn = new TableColumn<>("Color");
                colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

                playersTable.getColumns().add(playerColumn);
                playersTable.getColumns().add(colorColumn);
            }
        });
    }

}

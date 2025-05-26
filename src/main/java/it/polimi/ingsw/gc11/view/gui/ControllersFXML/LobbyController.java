package it.polimi.ingsw.gc11.view.gui.ControllersFXML;

import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class LobbyController {

    private Stage stage;

    @FXML
    private TableView<Player> playersTable;

    @FXML
    private TableColumn<Player, String> playerColumn;

    @FXML
    private TableColumn<Player, Player.Color> colorColumn;



    public void initialize() {

        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();

        try {
            ObservableList<Player> players = (ObservableList<Player>) virtualServer.getPlayers();
            playersTable.setItems(players);

            playerColumn = new TableColumn<>("Username");
            playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

            colorColumn = new TableColumn<>("Color");
            colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

            playersTable.getColumns().add(playerColumn);
            playersTable.getColumns().add(colorColumn);

        } catch (NetworkException e) {
            //label.setText ...
            throw new RuntimeException(e);
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

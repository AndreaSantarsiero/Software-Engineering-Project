package it.polimi.ingsw.gc11.view.gui.ControllersFXML.EndGamePhase;

import it.polimi.ingsw.gc11.controller.State.EndGamePhase;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.view.Controller;
import it.polimi.ingsw.gc11.view.EndPhaseData;
import it.polimi.ingsw.gc11.view.JoiningPhaseData;
import it.polimi.ingsw.gc11.view.gui.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;


public class EndGameController extends Controller {

    private Stage stage;
    private EndPhaseData data;


    @FXML VBox pointText;

    public void init(Stage stage) {
        this.stage = stage;
        ViewModel viewModel = (ViewModel) this.stage.getUserData();
        VirtualServer virtualServer = viewModel.getVirtualServer();
        this.data = (EndPhaseData) viewModel.getPlayerContext().getCurrentPhase();

        Label label = new Label("- Your score: " + data.getPlayer().getCoins());
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        pointText.setAlignment(Pos.CENTER);
        pointText.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        pointText.getChildren().add(label);

        for (Map.Entry<String, Player> entry : data.getEnemies().entrySet()) {
            label = new Label("- " + entry.getKey() + "'s score: " + entry.getValue().getCoins());
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

            pointText.setAlignment(Pos.CENTER);
            pointText.setPrefWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            pointText.getChildren().add(label);
        }
    }
}

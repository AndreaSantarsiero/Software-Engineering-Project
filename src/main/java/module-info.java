module it.polimi.ingsw.gc11 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.gc11 to javafx.fxml;
    exports it.polimi.ingsw.gc11;
}
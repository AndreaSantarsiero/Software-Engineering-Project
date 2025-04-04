module it.polimi.ingsw.gc11 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jdk.jdi;
    requires com.fasterxml.jackson.databind;
    requires org.fusesource.jansi;

    opens it.polimi.ingsw.gc11 to javafx.fxml;
    exports it.polimi.ingsw.gc11;
}
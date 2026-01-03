package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public final class Nav {
    private Nav() {}

    public static void go(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(Nav.class.getResource(fxml)));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            // KEY LINE: keep SAME scene so CSS stays
            scene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
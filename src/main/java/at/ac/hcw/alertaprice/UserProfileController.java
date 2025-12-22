package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML private TextField nameTextField;
    @FXML private TextField emailTextField;
    @FXML private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Prefer the current single user (es ist so angelegt überall für v1 des Apps)
        User u = User.getInstance();
        if (u.getName() == null || u.getEmail() == null) {
            //zürückfallen falls kein user vorhanden aus irgendeiner Grund
            User loaded = UserRepository.load();
            if (loaded != null) User.setInstance(loaded);
            u = User.getInstance();
        }

        nameTextField.setText(u.getName() == null ? "" : u.getName());
        emailTextField.setText(u.getEmail() == null ? "" : u.getEmail());
    }

    public void saveProfile(ActionEvent event) {
        String name = nameTextField.getText().trim();
        String email = emailTextField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Name is required.");
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            statusLabel.setText("Enter a valid email address.");
            return;
        }

        try {
            User updated = new User(name, email);
            UserRepository.save(updated);
            User.setInstance(updated);
            statusLabel.setText("Profile saved.");
            switchTo(event, NavState.getUserProfileReturnFxml());
        } catch (Exception e) {
            statusLabel.setText("Save failed: " + e.getMessage());
        }
    }

    public void deleteProfile(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Profile");
        alert.setHeaderText("This will delete your user profile (user.json).");
        alert.setContentText("Continue?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            // Also clear alerts so the next created user doesn't inherit them (single-user v1 design)
            // Do both: clear in-memory list AND remove the persisted file.
            try {
                WebAlertManager.deleteAllAlerts();
                Files.deleteIfExists(Path.of("webalerts.json"));
            } catch (Exception alertsEx) {
                // Don't block profile deletion if alert cleanup fails; just inform the user.
                statusLabel.setText("Profile deleted, but alert cleanup failed: " + alertsEx.getMessage());
            }

            UserRepository.delete();
            User.setInstance(new User()); //resettet single user
            switchTo(event, "webbotView.fxml"); //geht zurück zum anfang
        } catch (Exception e) {
            statusLabel.setText("Delete failed: " + e.getMessage());
        }
    }

    public void back(ActionEvent event) {
        try {
            switchTo(event, NavState.getUserProfileReturnFxml()); //repariert going back button
        } catch (Exception e) {
            statusLabel.setText("Navigation failed: " + e.getMessage());
        }
    }

    private void switchTo(ActionEvent event, String fxml) throws Exception {
        Parent content = FXMLLoader.load(getClass().getResource(fxml));

        // Wrap every view in the colorful frame so it stays consistent everywhere
        BorderPane shell = new BorderPane(content);
        shell.getStyleClass().add("app-shell");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // IMPORTANT: do NOT create a new Scene here; keep the existing Scene so CSS stays applied
        stage.getScene().setRoot(shell);
    }
}
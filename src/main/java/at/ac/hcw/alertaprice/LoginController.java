package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField nameTextfield;
    @FXML
    private TextField emailTextfield;
    @FXML
    private TextField errorTextField;

    private Stage stage;
    private Parent root;


    public void clearTextFields(){
        nameTextfield.setText("");
        emailTextfield.setText("");
        errorTextField.setText("");
    }

    private void go(ActionEvent event, String fxml) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource(fxml));

        // Wrap every view in the colorful frame so it stays consistent everywhere
        BorderPane shell = new BorderPane(content);
        shell.getStyleClass().add("app-shell");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // IMPORTANT: keep the existing Scene so CSS stays applied
        stage.getScene().setRoot(shell);
    }

    public void logIn(ActionEvent event) throws IOException {

        String name = nameTextfield.getText().trim();
        String email = emailTextfield.getText().trim();

        // 1. Eingabe validieren
        if (name.isEmpty() || email.isEmpty()) {
            errorTextField.setText("Bitte Name und E-Mail eingeben.");
            return;
        }
        if (!email.contains("@")) {
            errorTextField.setText("Bitte eine gültige E-Mail eingeben.");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonFile = new File("user.json");

        try {
            // 2. Wenn noch kein User existiert (erste Nutzung), dann anlegen
            if (!jsonFile.exists() || jsonFile.length() == 0) {
                User createdUser = new User(name, email);
                try (FileWriter writer = new FileWriter(jsonFile)) {
                    gson.toJson(createdUser, writer);
                }
                User.setInstance(createdUser);

                go(event, "showAlertsView.fxml");
                return;
            }

            // 3. Bestehenden User laden
            User savedUser;
            try (FileReader reader = new FileReader(jsonFile)) {
                savedUser = gson.fromJson(reader, User.class);
            }

            if (savedUser == null) {
                errorTextField.setText("Login fehlgeschlagen: user.json ist ungültig.");
                return;
            }

            // 4. Single-User-Login: Name + E-Mail müssen exakt passen
            String savedName = (savedUser.getName() == null) ? "" : savedUser.getName().trim();
            String savedEmail = (savedUser.getEmail() == null) ? "" : savedUser.getEmail().trim();

            boolean match = name.equalsIgnoreCase(savedName) && email.equalsIgnoreCase(savedEmail);

            if (!match) {
                errorTextField.setText("Login fehlgeschlagen: Name/E-Mail passen nicht zu diesem Gerät.");
                return;
            }

            // 5. Erfolgreich: Singleton setzen und weiter
            User.setInstance(savedUser);
            go(event, "showAlertsView.fxml");

        } catch (FileNotFoundException e) {
            errorTextField.setText("Login fehlgeschlagen: user.json Datei nicht gefunden.");
        } catch (Exception e) {
            errorTextField.setText("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

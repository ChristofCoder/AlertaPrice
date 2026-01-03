package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SetupController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;

    private Stage stage;
    private Parent root;


    public void clearTextFields() {
        nameTextField.setText("");
        emailTextField.setText("");
    }

    public void saveUser(ActionEvent event) throws IOException {

        String email = emailTextField.getText();
        String name = nameTextField.getText();

        // 1. Eingabeprüfung
        if (!email.contains("@")) {
            emailTextField.setPromptText("Enter a valid E-Mail Address here!");
            return;
        }
        if (name.isEmpty()) {
            nameTextField.setPromptText("Enter your name here");
            return;
        }

        User newUser = new User(name, email);

        // --- JSON Speicherung mit Gson ---
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonFile = new File("user.json");

        try {
            // Da nur ein User gespeichert werden soll, benötigen wir keine Liste
            // und müssen auch keine bestehenden Benutzer laden/prüfen,
            // da der neue Benutzer den alten einfach ersetzt.

            // 2. Neues Benutzerobjekt in die Datei schreiben (Serialisierung)
            // Der FileWriter überschreibt die Datei standardmäßig.
            try (FileWriter writer = new FileWriter(jsonFile)) { // try-with-resources
                gson.toJson(newUser, writer); // Speichert nur das einzelne User-Objekt
            }

            System.out.println("Benutzer erfolgreich in user.json gespeichert und bestehender überschrieben.");

        } catch (IOException e) {
            System.err.println("Fehler beim Speichern des Benutzers: " + e.getMessage());
            // Behandle den Fehler angemessen
        }


        User.setInstance(newUser);
        System.out.println("SetupController: navigating to addFirstAlertView.fxml");

        root = FXMLLoader.load(getClass().getResource("addFirstAlertView.fxml"));

        // Wrap every view in the colorful frame so it stays consistent everywhere
        BorderPane shell = new BorderPane(root);
        shell.getStyleClass().add("app-shell");

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // IMPORTANT: keep the existing Scene so CSS stays applied
        stage.getScene().setRoot(shell);
        stage.show();
    }



}




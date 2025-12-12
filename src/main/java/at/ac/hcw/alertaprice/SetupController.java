package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Scene scene;
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


        User.getInstance().setName(name);
        User.getInstance().setEmail(name);

        root = FXMLLoader.load(getClass().getResource("addFirstAlertView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}




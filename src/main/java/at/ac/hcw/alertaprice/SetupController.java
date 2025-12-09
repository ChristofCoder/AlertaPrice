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


    public void clearTextFields(){
        nameTextField.setText("");
        emailTextField.setText("");
    }

    public void saveUser(ActionEvent event) throws IOException{

        String email = emailTextField.getText();
        String name = nameTextField.getText();
        if (!email.contains("@")){
            emailTextField.setPromptText("Enter a valid E-Mail Address here!");
            return;
        }
        if (name.isEmpty()){
            nameTextField.setPromptText("Enter your name here");
            return;
        }

        User newUser = new User(name, email);

        // --- JSON Speicherung mit Gson ---
        // GsonBuilder().setPrettyPrinting().create() für besser lesbare JSON-Dateien
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonFile = new File("users.json");
        List<User> userList;

        // TypeToken wird benötigt, um Gson mitzuteilen, welche generische Struktur (List<User>) gelesen wird
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();

        try {
            // 2. Bestehende Benutzer lesen
            if (jsonFile.exists() && jsonFile.length() > 0) {
                // Aus der Datei lesen und in die Liste deserialisieren
                try (FileReader reader = new FileReader(jsonFile)) {
                    userList = gson.fromJson(reader, userListType);
                    if (userList == null) {
                        userList = new ArrayList<>();
                    }
                }
            } else {
                // Wenn die Datei neu ist, eine neue Liste erstellen
                userList = new ArrayList<>();
            }

            // 3. Neuen Benutzer hinzufügen
            userList.add(newUser);

            // 4. Aktualisierte Liste zurück in die Datei schreiben (Serialisierung)
            try (FileWriter writer = new FileWriter(jsonFile)) { //try-with-resources
                gson.toJson(userList, writer);
            }

            System.out.println("Benutzer erfolgreich in users.json gespeichert.");

        } catch (IOException e) {
            System.err.println("Fehler beim Speichern des Benutzers: " + e.getMessage());
            // Behandle den Fehler angemessen
        }

        User.getInstance().setName(name);
        User.getInstance().setEmail(name);

        root = FXMLLoader.load(getClass().getResource("addFirstAlertView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}

package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    private Scene scene;
    private Parent root;


    public void clearTextFields(){
        nameTextfield.setText("");
        emailTextfield.setText("");
    }

    public void logIn(ActionEvent event) throws IOException {

        String name = nameTextfield.getText();
        String email = emailTextfield.getText();

        // 1. Eingabe validieren (optional, aber empfohlen)
        if (name.isEmpty() || email.isEmpty()) {
            errorTextField.setText("Bitte Name und E-Mail eingeben.");
            return;
        }

        // 2. Erzeuge das User-Objekt zum Suchen (muss equals/hashCode überschreiben!)
        User newUser = new User(name, email);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonFile = new File("user.json");
        List<User> userList = new ArrayList<>(); // Starte mit leerer Liste
        boolean userFound = false;

        // Typ-Token für Gson
        Type userListType = new TypeToken<ArrayList<User>>() {}.getType();

        try {
            // 3. Bestehende Benutzer lesen
            if (jsonFile.exists() && jsonFile.length() > 0) {
                try (FileReader reader = new FileReader(jsonFile)) {

                    List<User> loadedList = gson.fromJson(reader, userListType);
                    if (loadedList != null) {
                        userList = loadedList;
                    }

                    // 4. Prüfen, ob der Benutzer existiert (durch equals-Methode)
                    if (userList.contains(newUser)) {
                        userFound = true;
                        // Lade die neue Szene
                        root = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                }
            }

            // 5. Ergebnis melden, wenn der Benutzer nicht gefunden wurde ODER die Datei leer/fehlt
            if (!userFound) {
                errorTextField.setText("Login fehlgeschlagen: Benutzer nicht gefunden.");
            }


        } catch (FileNotFoundException e) {
            // Datei existiert nicht.
            errorTextField.setText("Login fehlgeschlagen: user.json Datei nicht gefunden.");
        } catch (Exception e) {
            // Allgemeine Fehlerbehandlung
            errorTextField.setText("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button setupButton;
//    @FXML
//    private Button loginButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label helloLabel;
//    @FXML
//    private MenuItem setupMenuItem;


//    public void login(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginView.fxml")));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //Man bleibt auf der gleichen Stage
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    public void switchToSetupView(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("setupView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //Man bleibt auf der gleichen Stage
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToShowAlertsView(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //Man bleibt auf der gleichen Stage
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Liest den einzelnen Benutzer aus der user.json-Datei.
     * * @return Das User-Objekt, oder null, wenn die Datei nicht existiert oder leer ist.
     */
    public User loadUser() {
        // ... (Der Code aus Ihrer loadUser Methode) ...
        Gson gson = new Gson();
        File jsonFile = new File("user.json");

        if (jsonFile.exists() && jsonFile.length() > 0) {
            try (FileReader reader = new FileReader(jsonFile)) {
                User savedUser = gson.fromJson(reader, User.class);

                // HIER: Setzen Sie die geladene Instanz als das globale Singleton
                User.setInstance(savedUser);

                System.out.println("Benutzer erfolgreich aus user.json geladen: " + savedUser.getName());
                return savedUser; // Gibt das neu deserialisierte Objekt zurück

            } catch (IOException e) {
                // ... Fehlerbehandlung ...
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 1. Speichern Sie das Ergebnis von loadUser()
        User loadedUser = loadUser();

        if (loadedUser != null){

            // 2. WICHTIG: Verwenden Sie die geladene Instanz, nicht User.getInstance()
            // (Es sei denn, User.getInstance() kann eine Instanz entgegennehmen)
            String name = loadedUser.getName();

            // 3. Setzen Sie den Text
            helloLabel.setText("Hello " + name + ", welcome back!");

            // Optional: Button/UI für eingeloggten Zustand anpassen
            setupButton.setText("Show Alerts");
            setupButton.setOnAction(this::switchToShowAlertsView); // Beispiel für eine Umleitung

        }
        else {
            helloLabel.setText("Please set up a new Account");
            setupButton.setText("Setup Account"); // Behält den Originaltext
        }
    }
}





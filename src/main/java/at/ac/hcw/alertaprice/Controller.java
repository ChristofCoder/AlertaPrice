package at.ac.hcw.alertaprice;

import javafx.scene.layout.BorderPane;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    @FXML
    private Button setupButton;
    @FXML
    private Button myAlertsButton;

    @FXML
    private Button profileMenuButton;

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
        // This should open the initial account setup screen
        go(event, "setupView.fxml");
    }
    public void switchToShowAlertsView(ActionEvent event) throws IOException {
        go(event, "showAlertsView.fxml");
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
    public void switchToMyAlertsView(ActionEvent event) throws IOException {

        User loadedUser = loadUser();
        if (loadedUser == null) {
            switchToSetupView(event);
            return;
        }

        String targetFxml = hasAnyAlerts() ? "showAlertsView.fxml" : "addFirstAlertView.fxml";
        go(event, targetFxml);
    }
    public void switchToUserProfileView(ActionEvent event) throws IOException {

        User loadedUser = loadUser();
        if (loadedUser == null) {
            switchToSetupView(event);
            return;
        }

        NavState.setUserProfileReturnFxml("webbotView.fxml");
        go(event, "userProfileView.fxml");
    }
    private boolean hasAnyAlerts() {
        File file = new File(WebAlertManager.FILE_PATH); // "webalerts.json"
        if (!file.exists() || file.length() == 0) return false;

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<WebAlert>>(){}.getType();
            ArrayList<WebAlert> alerts = new Gson().fromJson(reader, listType);
            return alerts != null && !alerts.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    private void go(ActionEvent event, String fxml) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource(fxml));

        BorderPane shell = new BorderPane(content);
        shell.getStyleClass().add("app-shell");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(shell); // IMPORTANT: no new Scene
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

            setupButton.setText("Show Alerts");
            setupButton.setOnAction(e -> {
                try { switchToMyAlertsView(e); }
                catch (IOException ex) { throw new RuntimeException(ex); }
            });

            myAlertsButton.setDisable(false);
            profileMenuButton.setDisable(false);


        }
        else {
            helloLabel.setText("Please set up a new Account");
            setupButton.setText("Setup Account");
            setupButton.setOnAction(e -> {
                try { switchToSetupView(e); }
                catch (IOException ex) { throw new RuntimeException(ex); }
            });

            myAlertsButton.setDisable(true);
            profileMenuButton.setDisable(true);
        }
    }
}





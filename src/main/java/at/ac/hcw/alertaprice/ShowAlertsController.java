package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShowAlertsController implements Initializable {

    private Stage stage;
    private Parent root;

    @FXML
    private AnchorPane showAlertsPane;

    @FXML
    private Label myLabel;

    @FXML private TableView<WebAlert> itemTable;
    @FXML private TableColumn<WebAlert, Integer>  id;
    @FXML private TableColumn<WebAlert, String> name;
    @FXML private TableColumn<WebAlert, String> currentValue;

    @FXML
    private TextField idTextField;
    @FXML
    private Label outputLabel;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteAlertsButton;
    @FXML
    private Button addAlertButton;
    @FXML
    private Button start;
    @FXML
    private TextField inputTextfield;
    @FXML
    private Button stopButton;
    @FXML
    private TextField editAlertTextField;
    @FXML
    private Button editAlertButton;


    private volatile boolean klicked = false;
    private ScheduledExecutorService scheduler;




    private static final Gson GSON_INSTANCE = new GsonBuilder()
            // Registering a TypeAdapter for LocalDate
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting() // Optional: formats the output JSON nicely
            .create();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String username = User.getInstance().getName();
        myLabel.setText(username + "'s Alerts");

        // 1. Define how columns read data from the Item class
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        columnUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
//        cssSelector.setCellValueFactory(new PropertyValueFactory<>("cssSelector"));
        currentValue.setCellValueFactory(new PropertyValueFactory<>("currentValue"));
//        stringCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Option B: automatically strip anything that is not a digit in ID fields
        stripNonDigits(idTextField);
        stripNonDigits(editAlertTextField);

        // 2. Load the data from JSON
        List<WebAlert> loadedItems = loadItemsFromJson(WebAlertManager.FILE_PATH);

        // 3. Convert List to ObservableList (required for JavaFX controls)
        ObservableList<WebAlert> data = FXCollections.observableArrayList(loadedItems);

        // 4. Bind the data to the TableView
        itemTable.setItems(data);
    }

    private void stripNonDigits(TextField field) {
        field.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) return;
            String digitsOnly = newV.replaceAll("\\D+", "");
            if (!newV.equals(digitsOnly)) {
                field.setText(digitsOnly);
            }
        });
    }

    public static List<WebAlert> loadItemsFromJson(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {

            // Define the type of the list to deserialize (List<Item>)
            Type itemListType = new TypeToken<List<WebAlert>>() {
            }.getType();

            // 1. Create Gson instance
            Gson gson = new Gson();

            // 2. Parse JSON into a List<Item>
            List<WebAlert> items = gson.fromJson(reader, itemListType);

            return items;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on failure
        }
    }

    private void go(ActionEvent event, String fxml) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource(fxml));

        // Wrap every view in the colorful frame so it stays consistent everywhere
        BorderPane shell = new BorderPane(content);
        shell.getStyleClass().add("app-shell");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // IMPORTANT: do NOT create a new Scene here; keep the existing Scene so CSS stays applied
        stage.getScene().setRoot(shell);
    }

    public void addAlert(ActionEvent event) throws IOException {
        go(event, "addFirstAlertView.fxml");
    }

    public void openProfile(ActionEvent event) throws IOException {
        NavState.setUserProfileReturnFxml("showAlertsView.fxml");
        go(event, "userProfileView.fxml");
    }
    public void goBack(ActionEvent event) throws IOException {
        go(event, NavState.getShowAlertsReturnFxml());
    }

    public void deleteAlert(ActionEvent event) throws IOException {
        int id = Integer.parseInt(idTextField.getText());
        WebAlertManager.deleteWebAlert(id);
        go(event, "showAlertsView.fxml");
    }

    public void updatePrices(ActionEvent event) throws IOException {
        try{
// 1. Get the current date and time
            LocalDateTime now = LocalDateTime.now();

// 2. Define the desired format: Month (short name), Day, Hour (24h), Minute
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss");

// 3. Format the LocalDateTime object
            String formattedTime = now.format(formatter);

            if (WebAlertManager.updatePrices()){
                outputLabel.setText(formattedTime + ": 1+ price(s) changed!");
            }
            else outputLabel.setText(formattedTime + ": No price has changed!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void stopclick(){
        klicked = true;
        if (scheduler != null && !scheduler.isShutdown()) {
            // Stop the scheduled task cleanly
            scheduler.shutdownNow();
        }
    }

    // This method is called by the "Start" button
    public void start(ActionEvent event) throws IOException {
        // 1. Ensure the loop is not already running and klicked is reset
        if (scheduler != null && !scheduler.isShutdown()) {
            System.out.println("The task is already running.");
            return;
        }

        klicked = false;

        // 2. Parse the interval (input is in seconds, so multiply by 1000)
        long intervalSeconds;
        try {
            intervalSeconds = Long.parseLong(inputTextfield.getText());
            if (intervalSeconds <= 0) {
                System.err.println("Interval must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in text field.");
            return;
        }

        // 3. Create a single-threaded scheduler
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // 4. Schedule the repetitive task
        // The task will run immediately, and then every 'intervalSeconds' after that.
        scheduler.scheduleAtFixedRate(() -> {
            // --- Task Logic ---
            if (klicked) {
                // Check the stop flag *inside* the background thread
                scheduler.shutdownNow();
                return;
            }

            // IMPORTANT: If 'updatePrices' modifies the GUI, it MUST be wrapped in Platform.runLater()
            Platform.runLater(() -> {
                try {
                    updatePrices(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // --- End Task Logic ---

        }, 0, intervalSeconds, TimeUnit.SECONDS); // Delay 0, Repeat every 'intervalSeconds'
    }

    public void quit(ActionEvent event){

        event.consume();// Das Event bricht ab, wenn die Methode vorbei ist. Die folgende Methode logout kümmert sich um das Schliessen, wenn gewünscht.


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("All your Alerts will be saved!");
        alert.setContentText("See you next time!");
        DialogUtil.style(alert);
        DialogUtil.styleButtons(alert, ButtonType.OK, "danger-btn", ButtonType.CANCEL, "secondary-btn");

        if (alert.showAndWait().get() == ButtonType.OK){

            stage = (Stage) showAlertsPane.getScene().getWindow(); //so that our stage is the current stage that we are working with
            System.out.println("You successfully logged out");
//            WebAlertManager.deleteAllAlerts();
            stage.close();
        }
    }

    public void switchToEditView(ActionEvent event) throws IOException {
        String raw = editAlertTextField.getText();
        if (raw == null || raw.isBlank()) {
            outputLabel.setText("Please enter an ID to edit.");
            return;
        }

        int editId;
        try {
            editId = Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            outputLabel.setText("Invalid ID (must be a number). ");
            return;
        }

        // Optional sanity check: ensure the ID exists before navigating
        List<WebAlert> loadedItems = loadItemsFromJson(WebAlertManager.FILE_PATH);
        boolean exists = false;
        for (WebAlert a : loadedItems) {
            if (a.getId() == editId) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            outputLabel.setText("No alert found with ID " + editId);
            return;
        }

        //Die eingegebene ID wird in der einen Instanz der Klasse ID_Saver gespeichert um im nächsten Fenster verfügbar zu sein.
        ID_Saver.getInstance().setId(editId);
        System.out.println("ID_Saver Instanz erstellt: " + ID_Saver.getInstance().getId());

        go(event, "editAlertView.fxml");
    }
}

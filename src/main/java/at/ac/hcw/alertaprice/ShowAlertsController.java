package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import java.nio.file.Paths;
import java.util.List;
import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ShowAlertsController implements Initializable {
    @FXML
    private Label myLabel;

    @FXML private TableView<WebAlert> itemTable;
    @FXML private TableColumn<WebAlert, Integer>  id;
    @FXML private TableColumn<WebAlert, String> name;
    @FXML private TableColumn<WebAlert, String> originalValue;

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
        myLabel.setText("Hey " + username);

        // 1. Define how columns read data from the Item class
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        columnUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
//        cssSelector.setCellValueFactory(new PropertyValueFactory<>("cssSelector"));
        originalValue.setCellValueFactory(new PropertyValueFactory<>("originalValue"));
//        stringCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));



        // 2. Load the data from JSON
        List<WebAlert> loadedItems = loadItemsFromJson(WebAlertManager.FILE_PATH);

        // 3. Convert List to ObservableList (required for JavaFX controls)
        ObservableList<WebAlert> data = FXCollections.observableArrayList(loadedItems);

        // 4. Bind the data to the TableView
        itemTable.setItems(data);
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

    public void addAlert (ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        root = FXMLLoader.load(getClass().getResource("addFirstAlertView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAlert(ActionEvent event) throws IOException{

        Stage stage;
        Scene scene;
        Parent root;

        int id = Integer.parseInt(idTextField.getText());

        WebAlertManager.deleteWebAlert(id);

        root = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
}

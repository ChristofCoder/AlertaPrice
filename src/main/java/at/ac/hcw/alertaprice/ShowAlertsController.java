package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ResourceBundle;

import java.nio.file.Paths;
import java.util.List;
import java.lang.reflect.Type;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ShowAlertsController implements Initializable {
    @FXML
    private Label myLabel;

    @FXML private TableView<WebAlert> itemTable;
    @FXML private TableColumn<WebAlert, Integer>  id;
    @FXML private TableColumn<WebAlert, String> name;
    @FXML private TableColumn<WebAlert, String> columnUrl;
    @FXML private TableColumn<WebAlert, String> cssSelector;
    @FXML private TableColumn<WebAlert, String> originalValue;
    @FXML private TableColumn<WebAlert, String> stringCreatedAt;

    @FXML
    private TextField idTextField;



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
        columnUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
        cssSelector.setCellValueFactory(new PropertyValueFactory<>("cssSelector"));
        originalValue.setCellValueFactory(new PropertyValueFactory<>("originalValue"));
        stringCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));



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
//        Stage stage;
//        Scene scene;
//        Parent root;
//
//        root = FXMLLoader.load(getClass().getResource("deleteAlertView.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();

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
}

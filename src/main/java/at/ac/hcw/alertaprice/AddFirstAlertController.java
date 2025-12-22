package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;

public class AddFirstAlertController implements Initializable {

    @FXML
    private Label firstAlertLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField urlTextField;
    @FXML
    private TextField cssTextField;
    @FXML
    private Label errorLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String username = User.getInstance().getName();
        firstAlertLabel.setText("Welcome " + username);
    }
    public void clearTextFields(ActionEvent event){
        nameTextField.setText("");
        urlTextField.setText("");
        cssTextField.setText("");
    }

    public void saveAlert(ActionEvent event) throws IOException {

        //TODO: save to JSON File
        String name = nameTextField.getText();
        String url = urlTextField.getText();
        String css = cssTextField.getText();

        if (name.isEmpty() || url.isEmpty() || css.isEmpty()){
            errorLabel.setText("OOPS! Some input was missing!");
            return;
        }

        try {
            WebAlertManager.createWebAlert(name, url, css);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            return;
        }

        Parent content = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));

        // Wrap the view so the colorful frame + CSS stay consistent
        BorderPane shell = new BorderPane(content);
        shell.getStyleClass().add("app-shell");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(shell); // keep the existing Scene (keeps styles.css)

    }
}

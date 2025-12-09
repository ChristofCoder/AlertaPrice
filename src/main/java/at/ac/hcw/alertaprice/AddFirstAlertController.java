package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    private Stage stage;
    private Scene scene;
    private Parent root;

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
    public void login(ActionEvent event){
        //TODO: über Users in users.json iterieren etc.
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

        System.out.println("Name: " + name);
        System.out.println("URL: " + url);
        System.out.println("CSS-Selector: " + css);
        try {
            WebAlertManager.createWebAlert(name, url, css);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            return;
        }


        //TODO: switch to showAlertView

        root = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

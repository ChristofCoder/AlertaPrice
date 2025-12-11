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

import java.net.URL;
import java.util.ResourceBundle;

public class EditViewController implements Initializable {

    @FXML
    private Button editButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField urlTextField;
    @FXML
    private TextField cssTextField;

    @FXML
    private Label firstAlertLabel;

    @FXML
    private Button clearButton;

    @FXML
    private Label errorLabel;

    int id = ID_Saver.getInstance().getId();


    public void editAlert(ActionEvent event){

        String name = nameTextField.getText();
        String url = urlTextField.getText();
        String css = cssTextField.getText();

        try {
            WebAlertManager.updateWebAlert(id, name,url,css);
            errorLabel.setText("Update successful");
            Stage stage;
            Scene scene;
            Parent root;

            root = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            errorLabel.setText("Update failed" + e.getMessage());
        }



    }
    public void clearTextFields(ActionEvent event){
        nameTextField.setText("");
        urlTextField.setText("");
        cssTextField.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        firstAlertLabel.setText("Edit ID: " + id);
    }
}

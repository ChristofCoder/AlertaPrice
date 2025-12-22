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
import javafx.scene.layout.BorderPane;
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

        // Basic validation
        if (name == null || name.isBlank() || url == null || url.isBlank() || css == null || css.isBlank()) {
            errorLabel.setText("OOPS! Some input was missing!");
            return;
        }

        try {
            boolean ok = WebAlertManager.updateWebAlert(id, name, url, css);
            if (!ok) {
                errorLabel.setText("Update failed: ID not found or could not save.");
                return;
            }

            errorLabel.setText("Update successful");

            Parent content = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));

            // Wrap the view so the colorful frame + CSS stay consistent
            BorderPane shell = new BorderPane(content);
            shell.getStyleClass().add("app-shell");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(shell); // keep the existing Scene (keeps styles.css)

        } catch (Exception e) {
            errorLabel.setText("Update failed: " + e.getMessage());
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

        WebAlert a = WebAlertManager.getWebAlertById(id);
        if (a == null) {
            errorLabel.setText("No alert found for ID " + id);
            editButton.setDisable(true);
            return;
        }

        nameTextField.setText(a.getName());
        urlTextField.setText(a.getUrl());
        cssTextField.setText(a.getCssSelector());
    }
}

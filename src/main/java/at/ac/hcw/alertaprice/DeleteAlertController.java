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

public class DeleteAlertController implements Initializable {

    @FXML
    private Label deleteAlertLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField idTextField;


    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String username = User.getInstance().getName();
        deleteAlertLabel.setText("Welcome " + username);
    }
    public void clearTextFields(ActionEvent event){
        idTextField.setText("");

    }
    public void deleteAlert(ActionEvent event) throws IOException {


        int id = Integer.parseInt(idTextField.getText());

        WebAlertManager.deleteWebAlert(id);



        root = FXMLLoader.load(getClass().getResource("showAlertsView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

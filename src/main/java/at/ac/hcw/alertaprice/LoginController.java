package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField nameTextfield;
    @FXML
    private TextField emailTextfield;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void clearTextFields(){
        nameTextfield.setText("");
        emailTextfield.setText("");
    }

    public void logIn(ActionEvent event){

    }
}

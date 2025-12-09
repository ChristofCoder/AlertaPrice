package at.ac.hcw.alertaprice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button setupButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label welcomeLabel;

    public void login(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("loginView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //Man bleibt auf der gleichen Stage
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSetupView(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("setupView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow(); //Man bleibt auf der gleichen Stage
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

//    protected void switchToSetupView() {
//        SceneManager.getInstance().switchScene("setupView.fxml");
//    }
}




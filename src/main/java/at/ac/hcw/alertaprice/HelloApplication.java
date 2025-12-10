package at.ac.hcw.alertaprice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    String[] user = new String[2];

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("webbotView.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        Scene scene = new Scene(root);

        stage.setTitle("AlertaPrice WebBot");
        stage.setScene(scene);
        stage.show();

//        stage.setOnCloseRequest(event -> {
//            event.consume();// Das Event bricht ab, wenn die Methode vorbei ist. Die folgende Methode logout kümmert sich um das Schliessen, wenn gewünscht.
//            logout(stage);//wenn die Stage mit dem roten X geschlossen wird wird die Methode logout aufgerufen
//        });
    }
//    public void logout(Stage stage){
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Logout");
//        alert.setHeaderText("You're about to logout!");
//        alert.setContentText("Do you want to save before exiting?");
//
//        if (alert.showAndWait().get() == ButtonType.OK){
//
//            System.out.println("You successfully logged out");
//            stage.close();
//        }
//    }
}

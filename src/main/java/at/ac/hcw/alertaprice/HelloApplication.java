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
//        SceneManager.getInstance().setPrimaryStage(stage, "AlertaPrice WebBot");
//        SceneManager.getInstance().switchScene("webbotView.fxml");
//        Parent root = FXMLLoader.load(getClass().getResource("menuBar.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("webbotView.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Scene scene = new Scene(root);
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                System.out.println(keyEvent.getCode());
//            }
//        });
        //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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

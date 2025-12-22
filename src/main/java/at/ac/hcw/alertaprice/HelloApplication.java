package at.ac.hcw.alertaprice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.Objects;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;

public class HelloApplication extends Application {
    public static final double APP_W = 800;
    public static final double APP_H = 550;

    String[] user = new String[2];

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("webbotView.fxml"));
        Parent content = loader.load();

        StackPane inner = new StackPane(content);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        clip.widthProperty().bind(inner.widthProperty());
        clip.heightProperty().bind(inner.heightProperty());
        inner.setClip(clip);

        BorderPane shell = new BorderPane(inner);
        shell.getStyleClass().add("app-shell");
        Scene scene = new Scene(shell, APP_W, APP_H);

        URL css = Objects.requireNonNull(
                getClass().getResource("/at/ac/hcw/alertaprice/styles.css"),
                "styles.css not found! Check resources path."
        );
        scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(APP_W);
        stage.setMinHeight(APP_H);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();// Das Event bricht ab, wenn die Methode vorbei ist. Die folgende Methode logout kümmert sich um das Schliessen, wenn gewünscht.
            logout(stage);//wenn die Stage mit dem roten X geschlossen wird, wird die Methode logout aufgerufen
        });
    }
    public void logout(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("All your Alerts will be saved!");
        alert.setContentText("See you next time!");

        if (alert.showAndWait().get() == ButtonType.OK){

            System.out.println("You successfully logged out");
//            WebAlertManager.deleteAllAlerts();
            stage.close();
        }
    }

}

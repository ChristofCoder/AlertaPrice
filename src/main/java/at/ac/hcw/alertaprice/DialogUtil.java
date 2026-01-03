package at.ac.hcw.alertaprice;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.net.URL;

public final class DialogUtil {
    private DialogUtil() {}

    public static void style(Alert alert) {
        URL css = DialogUtil.class.getResource("/at/ac/hcw/alertaprice/styles.css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css.toExternalForm());
        }
        alert.getDialogPane().getStyleClass().add("app-dialog");
    }

    public static void styleButtons(Alert alert, ButtonType okType, String okClass, ButtonType cancelType, String cancelClass) {
        if (okType != null) {
            Button okBtn = (Button) alert.getDialogPane().lookupButton(okType);
            if (okBtn != null) okBtn.getStyleClass().add(okClass);
        }
        if (cancelType != null) {
            Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelType);
            if (cancelBtn != null) cancelBtn.getStyleClass().add(cancelClass);
        }
    }
}
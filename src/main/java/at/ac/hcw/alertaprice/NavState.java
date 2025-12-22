package at.ac.hcw.alertaprice;

public final class NavState {
    private NavState() {}

    //where the User Profile "Back" button should go
    private static String userProfileReturnFxml = "webbotView.fxml";

    public static void setUserProfileReturnFxml(String fxml) {
        userProfileReturnFxml = fxml;
    }

    public static String getUserProfileReturnFxml() {
        return userProfileReturnFxml;
    }
    //where the Show Alerts "Back" button should go
    private static String showAlertsReturnFxml = "webbotView.fxml";

    public static void setShowAlertsReturnFxml(String fxml) {
        showAlertsReturnFxml = fxml;
    }

    public static String getShowAlertsReturnFxml() {
        return showAlertsReturnFxml;
    }
}
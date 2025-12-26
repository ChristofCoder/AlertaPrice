package at.ac.hcw.alertaprice;

import jakarta.mail.MessagingException;
import javafx.application.Application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Launcher {
    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        Application.launch(HelloApplication.class, args);

        try {
            boolean checknewprices = WebAlertManager.updatePrices();
            if (checknewprices) { //hat sich was verändert? Sonst schick ich nix
                EmailAlert mail = new EmailAlert(
                        "alertaprice@outlook.de",
                        "bfyyqiotsdyrummo"
                );


                mail.send(
                        "alertaprice@outlook.de",
                        "AlertaPrice",
                        User.getInstance().getEmail(),
                        "Preisalarm!",
                        "<h2>Preis hat sich verändert! 🎉</h2>"
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

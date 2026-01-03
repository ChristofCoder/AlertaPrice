package at.ac.hcw.alertaprice;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import static jakarta.mail.Message.*;
//mithilfe von https://www.youtube.com/watch?v=bkpHQD9e5hc

public class EmailAlert {

    protected Session mailsession; //Verbindung zum Mailserver
    String receiverAddress = User.getInstance().getEmail();

    public EmailAlert(String mailUser, String appPassword) {
        login(mailUser, appPassword);
    }

    private void login(String mailUser, String appPassword){

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.office365.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Authenticator auth;
        auth = new Authenticator() {  //abstract!

            protected PasswordAuthentication getPasswordAuthentication(){
             return new PasswordAuthentication(mailUser,appPassword);
            }
        };
        this.mailsession = Session.getDefaultInstance(prop,auth);
    }

    public void send(String sendermail, String sendername, String recieveraddresses, String subject,String message) throws MessagingException, UnsupportedEncodingException {
        if (mailsession == null){
            throw  new IllegalStateException("login required");
        }



        MimeMessage msg = new MimeMessage(mailsession);
        msg.addHeader("Content-type","text/HTML; charset = UTF-8");
        msg.addHeader("format","flowed");
        msg.addHeader("Content-Transfer-Encoding","8bit");

        msg.setFrom(new InternetAddress("alertaprice@outlook.de", "AlertaPrice"));
        msg.setSubject(subject,"UTF-8");
        msg.setText(message,"UTF-8");
        msg.setSentDate(new Date());
        //rfc822 format wird NICHT erzwungen, da Strict = false
        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receiverAddress,false));

        Transport.send(msg);

    }

}

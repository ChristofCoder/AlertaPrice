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

    public void login(String smtphost,String port, String mailuser, String password){
        Properties prop = new Properties(); //Wertepaare zusammen bringen - Object key und Object value
        prop.put("mail.smpt.host",smtphost); //speichert Wert zu Key
        prop.put("mail.smtp.socketFactory.port",port);
        prop.put("mail.smtp.socketFactrory.class","javax.net.ssl.SslSocketFactory");
        prop.put("mail.smtp.auth","true");//Authentifizierung zwingend
        prop.put("mail.smtp.port",port);

        Authenticator auth = new Authenticator() {  //abstract!
            @Override
            protected PasswordAuthentication getPasswordAuthentification(){
             return new PasswordAuthentication(mailuser,password);
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

        msg.setFrom(new InternetAddress(sendermail, sendername));
        msg.setReplyTo(InternetAddress.parse(sendermail, false)); //rfc822 format wird NICHT erzwungen, da Strict = false
        msg.setSubject(subject,"UTF-8");
        msg.setText(message,"UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recieveraddresses,false));

        Transport.send(msg);

    }

}

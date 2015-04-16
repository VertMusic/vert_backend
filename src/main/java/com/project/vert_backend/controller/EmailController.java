package com.project.vert_backend.controller;
import com.project.vert_backend.model.User;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Controls the sending of email messages to recipients
 * @author dev
 */
public class EmailController {

    private Session session;

    // Sender's email ID needs to be mentioned
    private static final String SENDER_EMAIL = "VertMusicService@gmail.com";
    private static final String PASSWORD = "y0u11n3v3rgu3ss";

    public static final String WELCOME_SUBJECT = "Welcome to VertMusic";
    public static final String WELCOME_BODY = "Hello {1}, \r\n\r\nThank you for joining VertMusic. This is a confirmation "
            + "that your account has been successfully been set up. Please take some time "
            + "to download our VertMusic app from the Apple Appstore or Google Play to listen "
            + "to your music from your mobile devices!\r\n\r\n"
            + "Sincerely,\r\n"
            + "The VertMusic team";

    public EmailController() {
        Authenticator auth = new Auth();

        /// Use smtp via Google
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "*");

        session = Session.getInstance(props, auth);
    }

    public void sendWelcomeMessage(User user) {
        String body = EmailController.WELCOME_BODY.replaceAll("\\{1\\}", user.getName());
        sendMessage(user.getEmail(), EmailController.WELCOME_SUBJECT, body);
    }

    private void sendMessage(String recipientEmail, String subject, String emailText) {

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            message.setSubject(subject);
            message.setText(emailText);

            // Send message
            Transport.send(message);
            System.out.println("Sent message to '" + recipientEmail + "' successfully...");
        } catch (MessagingException mex) {
            System.out.println("Error sending email to '" + recipientEmail + "' - " + mex);
        }
    }

    /**
     * Authentication to Google account setup for smtp
     */
    private class Auth extends Authenticator {

        public Auth() {
            super();
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(EmailController.SENDER_EMAIL, EmailController.PASSWORD);
        }
    }
}

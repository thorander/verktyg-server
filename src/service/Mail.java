package service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Mail {

    private static Properties props = new Properties();
    private static final String username = "testverktyg@gmail.com";
    private static final String password = "testverkty";
    private static Session session;

    public static void setup() {

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });


    }

    public static void sendWelcomeEmail(String mail, String name){
/*        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("markus3832@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Welcome to BBB");
            message.setText("Dear " + name + ","
                    + "\n\n You are now registered on BBBs test tool.");

            Transport.send(message);

            System.out.println("Message sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static void sendNewTestMail(String mail, String name){
/*        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("markus3832@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Automated message from BBB");
            message.setText("Dear " + name + ","
                    + "\n\n You have a new test to do. Don't dawdle!");

            Transport.send(message);

            System.out.println("Message sent");

        } catch (MessagingException e) {
            System.out.println(e);
        }*/
    }
}

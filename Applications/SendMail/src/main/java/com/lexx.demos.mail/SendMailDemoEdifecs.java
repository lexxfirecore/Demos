package com.lexxstudy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailDemoEdifecs {

    static String mailUsername = "lexxdemo@gmail.com";
    static String mailTo = mailUsername;
    static String mailFrom = mailUsername;
    static String mailHost = "smtp.gmail.com";

    static String mailPassword = "lexxdemo123";
    static String mailSubject = "subject";
    static String mailText = "email text.... " + new SimpleDateFormat("HH:mm:ss dd.MMM.yyyy").format(new Date());


    static String mailUsernameEdfx = "c-alexandru.corghencea@edifecs.com";
    static String mailToEdfx = "c-alexandru.corghencea@edifecs.com";
    static String mailFromEdfx = "c-alexandru.corghencea@edifecs.com";
    static String mailHostEdfx = "smtp.edifecs.com";
    static String mailPasswordEdfx = "Mayak203";


    public static void main(String[] args) {



//        sendMessageEdifecs();

        sendMessageGoogle();

 //       sendMessageEdifecsSSL();

    }


    private static void sendMessageEdifecs() {

        System.out.println("Mail send Edifecs .... ");

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", mailHostEdfx);
        properties.put("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

//        properties.setProperty("mail.debug", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsernameEdfx, mailPasswordEdfx);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFromEdfx));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailToEdfx));
            message.setSubject(mailSubject);
            message.setText(mailText);

            Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Mail send Edifecs message sent successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


    private static void sendMessageGoogle() {


        System.out.println("Mail send sendMessageGoogle .... ");

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", mailHost);
        properties.setProperty("mail.transport.protocol", "smtp");
//        properties.setProperty("mail.smtp.user", mailUsername);
//        properties.setProperty("mail.smtp.password", mailPassword);

        boolean tls = true;

        properties.setProperty("mail.smtp.auth", "true");
        if (tls) {
            properties.setProperty("mail.smtp.port", "587");  // port TLS 587
            properties.setProperty("mail.smtp.starttls.enable", "true");

        } else {
            properties.setProperty("mail.smtp.port", "465");  // port ssl 465
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        }

        // mail debug
        properties.setProperty("mail.debug", "true");


//        Session session = Session.getInstance(properties);

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUsername, mailPassword);
                    }
                });


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            message.setSubject(mailSubject);
            message.setText(mailText);

            Transport transport = session.getTransport();
            transport.connect(mailHost, mailUsername, mailPassword);
//            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Mail send sendMessageGoogle successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


    private static void sendMessageEdifecsSSL() {

        System.out.println("Mail send sendMessageEdfx SSL .... ");

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", mailHostEdfx);
        properties.setProperty("mail.transport.protocol", "smtp");

        boolean tls = true;

        properties.setProperty("mail.smtp.auth", "true");
        if (tls) {
            properties.setProperty("mail.smtp.port", "587");  // port tls 587
            properties.setProperty("mail.smtp.starttls.enable", "true");

        } else {
            properties.setProperty("mail.smtp.port", "465");  // port ssl 465
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        }

        // mail debug
        properties.setProperty("mail.debug", "true");


        Session session = Session.getInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFromEdfx));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailToEdfx));
            message.setSubject(mailSubject);
            message.setText(mailText);

            Transport transport = session.getTransport();
            transport.connect(mailHostEdfx, mailFromEdfx, "Mayak203");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Mail send sendMessageGoogle successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


}
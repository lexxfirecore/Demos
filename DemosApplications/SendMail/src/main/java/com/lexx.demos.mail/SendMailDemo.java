package com.lexx.demos.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailDemo {

    static String mailUsername = "lexxdemo@gmail.com";
    static String mailTo = mailUsername;
    static String mailFrom = mailUsername;
    static String mailHost = "smtp.gmail.com";

    static String mailPassword;
    static String mailSubject = "subject";
    static String mailText = "email text.... " + new SimpleDateFormat("HH:mm:ss dd.MMM.yyyy").format(new Date());


    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the mail password for username: " + mailUsername);
        mailPassword = reader.nextLine();

        System.out.println("***** SST/TLS :");
        sendMessageGoogle(true);
        System.out.println("***** STARTTLS :");
        sendMessageGoogle(false);
    }

    private static void sendMessageGoogle(boolean ssl) {

        System.out.println("Mail send sendMessageGoogle .... ");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", mailHost);
        properties.setProperty("mail.transport.protocol", "smtp");

        properties.setProperty("mail.smtp.auth", "true");
        if (ssl) {
            properties.setProperty("mail.smtp.port", "465");  // port ssl 465
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        } else {
            properties.setProperty("mail.smtp.port", "587");  // port TLS 587
            properties.setProperty("mail.smtp.starttls.enable", "true");
        }

        // mail debug
        properties.setProperty("mail.debug", "true");


        Session session = Session.getInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUsername, mailPassword);
                    }
                });


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            message.setSubject(mailSubject + "\n mail.smtp.ssl.enable=" + properties.getProperty("mail.smtp.ssl.enable"));
            message.setText(mailText + "\n" + properties.getProperty("mail.smtp.port") + "\n ssl=" + ssl);

            Transport transport = session.getTransport();
            transport.connect(mailHost, mailUsername, mailPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Mail send sendMessageGoogle successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }




}
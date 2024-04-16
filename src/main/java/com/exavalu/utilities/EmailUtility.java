package com.exavalu.utilities;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.exavalu.pojos.PropertyValues;

public class EmailUtility {

    public static void sendVerificationEmail(String recipientEmail, float verificationCode, String emailType, PropertyValues propertyValues) {
        
    	String host = "smtp.gmail.com"; // Assuming this is your SMTP server host
    	String port = "587"; // Correct port for TLS
    	String mailFrom = propertyValues.getEmailAddress();;
    	final String password = propertyValues.getEmailPassword(); 

    	// Message info
    	String verificationSubject = "Please verify your email address";
    	String verificationUrl = "http://localhost:8080/budgetbaker/VerifyUser?code=" + (int) verificationCode;
    	String verificationText = "To verify your email address, click the following link: " + verificationUrl;
    	String passwordResetSubject = "Reset your Password";
    	String passwordResetUrl = "http://localhost:8080/budgetbaker/ResetPassword?emailAddress=" + recipientEmail  ;
    	String passwordResetText = "Enter your new passwords here:" + passwordResetUrl;
    	// Set properties
    	Properties properties = new Properties();
    	properties.put("mail.smtp.host", host);
    	properties.put("mail.smtp.port", port); // This line was missing
    	properties.put("mail.smtp.auth", "true");
    	properties.put("mail.smtp.starttls.enable", "true"); // This line was commented out in your code

    	// Create session with authenticator
    	Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
    	    protected PasswordAuthentication getPasswordAuthentication() {
    	        return new PasswordAuthentication(mailFrom, password);
    	    }
    	});
    	
        try {
            // Create a MimeMessage object
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            if (emailType == "verify")	{
            	message.setSubject(verificationSubject);
            	message.setText(verificationText);
            } else if (emailType=="resetPassword")	{
            	message.setSubject(passwordResetSubject);
        		message.setText(passwordResetText);
            }
            	
            // Send message
            Transport.send(message);
            System.out.println("Verification email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
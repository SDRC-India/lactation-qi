package org.sdrc.lactation.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 * This class contains a method which will be used for sending mails in the lactation project.
 *
 */

@Component
public class EmailService {
	
	private static final Logger log = LogManager.getLogger(EmailService.class);
	
	@Autowired
	ConfigurableEnvironment configurableEnvironment;
	
	/**
	 * This method will send an email, use this component throughout the lactation project. 
	 * It has been configured for multi-purpose use
	 * @param to - specify the email address which you want to keep in to (only one email in to)
	 * @param cc - specify multiple email as comma separated string.
	 * @param subject - specify the subject of the mail
	 * @param text - specify the body of the mail
	 * @param attachmentPath - specify the attachement path
	 * 
	 */
	
	public void sendEmail(String to, String cc, String subject, String text, String attachmentPath){

		// credentials of the account through which the email will be sent
		final String username = configurableEnvironment.getProperty(Constants.EMAIL);
		final String pass = configurableEnvironment.getProperty(Constants.EMAIL_PASSWORD);

		// configuring the mail properties.
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, pass);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			
			//execute this block of code if cc argument has some value.
			if(cc != null){
				String[] recipientList = cc.split(",");
				InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
				int counter = 0;
				for (String recipient : recipientList) {
				    recipientAddress[counter] = new InternetAddress(recipient.trim());
				    counter++;
				}
				message.setRecipients(Message.RecipientType.CC, recipientAddress);
			}
			
			message.setSubject(subject);
			
			// execute the following block of code if attachment argument has some value.
			if(attachmentPath != null){
				String[] fileName = attachmentPath.split("/");
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(text);
				
				// Create a multipart message
				Multipart multipart = new MimeMultipart();
	
				// Set text message part
				multipart.addBodyPart(messageBodyPart);
	
				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachmentPath);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName[fileName.length -1]);
				multipart.addBodyPart(messageBodyPart);

				// Send the complete message parts
				message.setContent(multipart);
			}else{
				message.setText(text);
			}
			
			Transport.send(message);

		} catch (MessagingException e) {
			log.error("Exception occured while sending email - " + e.getMessage());
		}
	
	}

}
